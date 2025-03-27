package com.fanxan.serviceauth.repository;

import com.fanxan.serviceauth.model.entity.RefreshToken;
import com.fanxan.serviceauth.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    @Override
    Optional<RefreshToken> findById(UUID id);

    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);

}
