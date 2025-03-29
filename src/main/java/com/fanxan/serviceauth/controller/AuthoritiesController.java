package com.fanxan.serviceauth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AuthoritiesController {

    @PreAuthorize("hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public static String admin() {
        return "only for admin";
    }

    @GetMapping("/whoami")
    public String whoAmI(Authentication authentication) {
        return "User: " + authentication.getName() + " | Authorities: " + authentication.getAuthorities();
    }


}
