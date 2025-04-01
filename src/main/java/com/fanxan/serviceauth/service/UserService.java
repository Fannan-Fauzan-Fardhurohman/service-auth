package com.fanxan.serviceauth.service;

import com.fanxan.serviceauth.mapper.UserMapper;
import com.fanxan.serviceauth.model.dto.request.LoginRequest;
import com.fanxan.serviceauth.model.dto.response.GetUserDTO;
import com.fanxan.serviceauth.model.dto.response.JwtResponse;
import com.fanxan.serviceauth.model.dto.response.UserDTO;
import com.fanxan.serviceauth.model.dto.response.UserDetailsImpl;
import com.fanxan.serviceauth.model.entity.RefreshToken;
import com.fanxan.serviceauth.model.entity.Role;
import com.fanxan.serviceauth.model.entity.User;
import com.fanxan.serviceauth.repository.RoleRepository;
import com.fanxan.serviceauth.repository.UserRepository;
import com.fanxan.serviceauth.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RefreshTokenService refreshTokenService;


    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, AuthenticationManager authenticationManager, JwtUtils jwtUtils, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    @Transactional
    public GetUserDTO register(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userDTO.getTanggalLahir() != null) {
            user.setDate(userDTO.getTanggalLahir());
        }

        List<Role> roles = roleRepository.findByRoleNameIn(Collections.singleton(userDTO.getRoleNames()));
        user.setRoles(roles);

        User savedUser = userRepository.save(user);

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
}