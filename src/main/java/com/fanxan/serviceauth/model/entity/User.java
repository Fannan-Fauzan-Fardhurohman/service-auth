package com.fanxan.serviceauth.model.entity;

import com.fanxan.serviceauth.utils.enumeration.JenisKelaminEnumDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "kelurahan", length = 150)
    private String kelurahan;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "nama_lengkap", length = 100)
    private String namaLengkap;

    @Enumerated(EnumType.STRING)
    @Column(name = "jenis_kelamin", columnDefinition = "ENUM('L','P')", nullable = false)
    private JenisKelaminEnumDTO jenisKelamin;

    @Column(name = "tempat_lahir", length = 50)
    private String tempatLahir;

    @Column(name = "tanggal_lahir")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "email", length = 100, unique = true)
    private String email;

    @Column(name = "photo", length = 255)
    private String photo;

    @Column(name = "phone", length = 15, unique = true)
    private String phone;

    @Version
    private Long version;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Collection<Role> roles = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreatedBy
    @Column(name = "created_by", length = 50)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "updated_by", length = 50)
    private String updatedBy;

    @Column(name = "deleted_by", length = 50)
    private String deletedBy;

    public User deleted() {
        deletedAt = LocalDateTime.now();
        return this;
    }
}