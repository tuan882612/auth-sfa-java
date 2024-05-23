package com.authsfa.web.controller;

import com.authsfa.domain.user.UserService;
import com.authsfa.web.dto.UserReq;
import lombok.AllArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping(path="/auth")
public class UserController {
    private final UserService userSvc;

    @PostMapping(path="/login")
    public Mono<ResponseEntity<Void>> login() {
        return null;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody UserReq userReq) {
        return userSvc.registerUser(userReq.getEmail(), userReq.getPassword())
            .map(user -> ResponseEntity.status(HttpStatus.CREATED)
                    .header("X-Uid", user.getUserId())
                    .header("X-Email", user.getEmail())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + user.getToken())
                    .body("success"))
            .onErrorResume(DuplicateKeyException.class, _ -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists")))
            .onErrorResume(Exception.class, e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage())));

    }
}
