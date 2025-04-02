package com.fanxan.serviceauth.model.entity;


import com.fanxan.serviceauth.utils.AppConstant;
import com.fanxan.serviceauth.utils.enumeration.DataStatus;
import com.nimbusds.jose.shaded.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.sql.Timestamp;

@Entity
@Table(name = "customer_verification_pin")
@Data
@NoArgsConstructor
public class CustomerVerificationPin  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "token")
    private String token;

    @Column(name = "token_hash")
    private String tokenHash;

    @Column(name = "event")
    private String event;

    @Column(name = "event_data", length = 255)
    private String eventData;

    @Column(name = "pin")
    private String pin;

    @Column(name = "pin_hash")
    private String pinHash;

    @Column(name = "expired_at")
    protected Timestamp expiredAt;

    @Column(name = "last_type")
    private String lastType;

    @Expose
    @Column(name = "status", columnDefinition = "INT")
    protected int status = DataStatus.ACTIVE.getValue();

    @CreatedBy
    @Column(name = "created_by", updatable = false)
    protected String createdBy = AppConstant.SYSTEM_ID;

    @CreationTimestamp
    @Column(nullable = false, updatable = false, name = "created_at")
    protected Timestamp createdAt;

    @LastModifiedBy
    @Column(name = "updated_by")
    protected String updatedBy;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    protected Timestamp updatedAt;

    @PreUpdate
    protected void onPreUpdate() {

    }
}