package com.fanxan.serviceauth.mapper;

import com.fanxan.serviceauth.model.dto.response.GetUserDTO;
import com.fanxan.serviceauth.model.dto.response.UserDTO;
import com.fanxan.serviceauth.model.entity.Role;
import com.fanxan.serviceauth.model.entity.User;
import com.fanxan.serviceauth.utils.enumeration.JenisKelaminEnumDTO;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
@Component
public class UserMapper {

    public User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setKelurahan(userDTO.getKelurahan());
        user.setUsername(userDTO.getUsername());
        user.setNamaLengkap(userDTO.getNamaLengkap());

        if (userDTO.getJenisKelamin() != null) {
            user.setJenisKelamin(JenisKelaminEnumDTO.valueOf(userDTO.getJenisKelamin()));
        }

        user.setTempatLahir(userDTO.getTempatLahir());
        user.setDate(userDTO.getTanggalLahir());
        user.setActive(userDTO.getActive());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setPhoto(userDTO.getPhoto());
        user.setPhone(userDTO.getPhone());

        if (userDTO.getRoles() != null) {
            Collection<Role> roles = userDTO.getRoles().stream()
                    .map(roleName -> {
                        Role role = new Role();
                        role.setRoleName(roleName.getRoleName());
                        return role;
                    })
                    .collect(Collectors.toList());
            user.setRoles(roles);
        }

        return user;
    }

    public GetUserDTO toDto(User user) {
        GetUserDTO dto = new GetUserDTO();
        dto.setId(user.getId());
        dto.setKelurahan(user.getKelurahan());
        dto.setUsername(user.getUsername());
        dto.setNamaLengkap(user.getNamaLengkap());
        dto.setJenisKelamin(user.getJenisKelamin());
        dto.setTempatLahir(user.getTempatLahir());
        dto.setTanggalLahir(user.getDate());
        dto.setActive(user.getActive());
        dto.setEmail(user.getEmail());
        dto.setPhoto(user.getPhoto());
        dto.setPhone(user.getPhone());

        dto.setRoles(user.getRoles().stream()
                .findFirst()
                .map(Role::getRoleName)
                .orElse("ROLE_USER"));

        return dto;
    }

}
