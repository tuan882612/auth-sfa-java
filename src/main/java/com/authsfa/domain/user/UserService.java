package com.authsfa.domain.user;

import com.authsfa.web.dto.UserRes;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserRes> loginUser(String email, String password);
    Mono<UserRes> registerUser(String email, String password);
}
