package com.fanxan.serviceauth.service;


import com.fanxan.serviceauth.model.dto.response.UserDetailsImpl;
import com.fanxan.serviceauth.model.entity.Role;
import com.fanxan.serviceauth.model.entity.User;
import com.fanxan.serviceauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User with username = " + username + " not exist!"));
        Collection<GrantedAuthority> roles = getUserAuthority(user.getRoles());
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), roles);
    }

    private Collection<GrantedAuthority> getUserAuthority(Collection<Role> userRoles) {
        Collection<GrantedAuthority> roles = new ArrayList<>();
        userRoles.forEach(role -> roles.add(new SimpleGrantedAuthority(role.getRoleName())));

        return new ArrayList<>(roles);
    }
}
