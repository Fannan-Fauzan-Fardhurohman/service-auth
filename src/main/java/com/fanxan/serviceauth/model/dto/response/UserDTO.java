package com.fanxan.serviceauth.model.dto.response;


import com.fanxan.serviceauth.model.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String kelurahan;
    private String username;
    private String namaLengkap;
    private String jenisKelamin;
    private String tempatLahir;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date tanggalLahir;

    private Boolean active;
    private String password;
    private String email;
    private String photo;
    private String phone;
    private Collection<Role> roles;
    private String roleNames;
    @JsonIgnore
    private String deviceId;
    private String typeOTP;
}
