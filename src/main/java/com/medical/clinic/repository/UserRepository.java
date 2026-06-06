package com.medical.clinic.repository;

import com.medical.clinic.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;



public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByEmailVerificationToken(String token);

    Optional<User> findByRefreshToken(String refreshToken);
}