package com.fanxan.serviceauth.service;

import com.fanxan.serviceauth.mapper.UserMapper;
import com.fanxan.serviceauth.model.dto.response.GetUserDTO;
import com.fanxan.serviceauth.model.dto.response.UserDTO;
import com.fanxan.serviceauth.model.entity.Role;
import com.fanxan.serviceauth.model.entity.User;
import com.fanxan.serviceauth.repository.RoleRepository;
import com.fanxan.serviceauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public GetUserDTO save(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        if (userDTO.getTanggalLahir() != null) {
            user.setDate(userDTO.getTanggalLahir());
        }

        List<Role> roles = roleRepository.findByRoleNameIn(Collections.singleton(userDTO.getRoleNames()));
        user.setRoles(roles);

        // Save user to DB
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }
}