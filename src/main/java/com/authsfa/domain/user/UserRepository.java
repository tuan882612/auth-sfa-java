package com.authsfa.domain.user;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {
    @Query("INSERT INTO users (user_id, email, password, last_login, created) " +
            "VALUES (:userId, :email, :password, :lastLogin, :created) RETURNING * ;")
    Mono<User> save(UUID userId, String email, byte[] password, LocalDateTime lastLogin, LocalDateTime created);

    Mono<User> findByEmail(String email);

    @Query("UPDATE users SET last_login = :lastLogin WHERE user_id = :userId RETURNING * ;")
    Mono<User> updateLastLogin(UUID userId, LocalDateTime lastLogin);
}
