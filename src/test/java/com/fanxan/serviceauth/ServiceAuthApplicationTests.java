package com.fanxan.serviceauth;

import com.fanxan.serviceauth.model.dto.response.UserDetailsImpl;
import com.fanxan.serviceauth.model.entity.User;
import com.fanxan.serviceauth.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@SpringBootTest
class ServiceAuthApplicationTests {

    ServiceAuthApplicationTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    void contextLoads() {
    }

    private static UserRepository userRepository = null;

    @Value("${app.jwt.secret}")
    private static String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private static int jwtExpirationMs;


    private static SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJwtToken(UserDetailsImpl userPrincipal) {
        return generateTokenFromUsername(userPrincipal.getUsername());
    }

    public static String generateTokenFromUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return Jwts.builder()
                .header().add("type", "JWT").and()
                .subject(username)
                .issuedAt(new Date())
                .issuer("Mimin")
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .claim("email", userOptional.get().getEmail())
                .claim("jk", userOptional.get().getJenisKelamin())
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public static boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean testTokenValidity() {
        String username = "exampleUser8";
        String token = generateTokenFromUsername(username);
        System.out.println("Generated token: " + token);
        boolean isValid = validateJwtToken(token);
        System.out.println("Token valid: " + isValid);
        return isValid;
    }

    public static void main(String[] args) {
        testTokenValidity();
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32]; // 256 bits
        random.nextBytes(keyBytes);
        String encodedKey = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Use this key: " + encodedKey);
        System.out.println("token valid :: " + testTokenValidity());
    }

}
