package com.fanxan.serviceauth.service;

import com.fanxan.serviceauth.mapper.UserMapper;
import com.fanxan.serviceauth.model.dto.request.CustomerLoginRegisterPayload;
import com.fanxan.serviceauth.model.dto.request.LoginRequest;
import com.fanxan.serviceauth.model.dto.response.GetUserDTO;
import com.fanxan.serviceauth.model.dto.response.JwtResponse;
import com.fanxan.serviceauth.model.dto.response.UserDTO;
import com.fanxan.serviceauth.model.dto.response.UserDetailsImpl;
import com.fanxan.serviceauth.model.entity.CustomerVerificationPin;
import com.fanxan.serviceauth.model.entity.RefreshToken;
import com.fanxan.serviceauth.model.entity.Role;
import com.fanxan.serviceauth.model.entity.User;
import com.fanxan.serviceauth.repository.RoleRepository;
import com.fanxan.serviceauth.repository.UserRepository;
import com.fanxan.serviceauth.utils.JwtUtils;
import com.fanxan.serviceauth.utils.enumeration.VerificationEventEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final CustomerVerificationService customerVerificationService;

    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;

    private final RabbitTemplate rabbitTemplate;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, CustomerVerificationService customerVerificationService, UserMapper userMapper, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshTokenService refreshTokenService, RabbitTemplate rabbitTemplate) {
        this.userRepository = userRepository;
        this.customerVerificationService = customerVerificationService;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Transactional
    public GetUserDTO register(UserDTO userDTO) throws Exception {
        User user = userMapper.toEntity(userDTO);
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userDTO.getTanggalLahir() != null) {
            user.setDate(userDTO.getTanggalLahir());
        }

        user.setActive(false);

        List<Role> roles = roleRepository.findByRoleNameIn(Collections.singleton(userDTO.getRoleNames()));
        user.setRoles(roles);

        ensureDeviceIdExists(userDTO);

        CustomerVerificationPin verificationPin = null;
        try {
            verificationPin = createVerificationPin(
                    "REGISTER",
                    userDTO.getDeviceId(),
                    userDTO.getTypeOTP(),
                    userDTO
            );
        } catch (Exception e) {
            log.info(e.getMessage());
        }


        User savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);
        log.info("Saved user: {} , name : {}", savedUser, savedUser.getNamaLengkap());
        rabbitTemplate.convertAndSend("auth","AUTH", savedUser);
        return userMapper.toDto(savedUser);
    }

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles);
        log.info("jwt response :: {}", jwtResponse);
        return jwtResponse;
    }


    private void ensureDeviceIdExists(UserDTO payload) {
        if (Objects.isNull(payload.getDeviceId()) || StringUtils.isEmpty(payload.getDeviceId())) {
            payload.setDeviceId(UUID.randomUUID().toString());
        }
    }

    private CustomerVerificationPin createVerificationPin(
            String verificationEventCode,
            String deviceId,
            String typeOTP,
            UserDTO payload
    ) throws Exception {
        log.info("verification event code {}", verificationEventCode);
        try {
            return customerVerificationService.createOrUpdate(
                    verificationEventCode, deviceId, typeOTP, payload
            );
        } catch (Exception e) {
            log.error("Failed to create verification pin");
            throw e;
        }
    }

}