package com.fanxan.serviceauth.model.dto.response;

import com.fanxan.serviceauth.utils.enumeration.JenisKelaminEnumDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserDTO {
    private Long id;
    private String kelurahan;
    private String username;
    private String namaLengkap;
    private JenisKelaminEnumDTO jenisKelamin;
    private String tempatLahir;
    private Date tanggalLahir;
    private Boolean active;
    private String email;
    private String photo;
    private String phone;
    private String roles;
}