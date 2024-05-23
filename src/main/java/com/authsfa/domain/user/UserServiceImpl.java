package com.authsfa.domain.user;

import com.authsfa.config.security.JwtUtil;
import com.authsfa.web.dto.UserRes;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


/**
 * Implementation of the UserService interface, providing methods for authenticating users and registering new users.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Authenticates a user with the given email and password.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return a Mono emitting the user if the authentication is successful, otherwise an error
     */
    @Override
    public Mono<UserRes> loginUser(String email, String password) {
        return userRepository.findByEmail(email)
            .flatMap(user -> {
                if (passwordEncoder.matches(password, new String(user.getPassword()))) {
                    return userRepository.updateLastLogin(user.getUserId(), LocalDateTime.now())
                        .map(updatedUser -> new UserRes(
                            updatedUser.getUserId().toString(),
                            updatedUser.getEmail(),
                            jwtUtil.generateToken(updatedUser.getUserId().toString(), updatedUser.getEmail())));
                }
                return Mono.error(new RuntimeException("Invalid email or password"));
            })
            .switchIfEmpty(Mono.error(new RuntimeException("Invalid email or password")));
    }

    /**
     * Registers a new user with the given email and password.
     *
     * @param email the email of the user
     * @param password the password of the user
     * @return a Mono emitting the user if the registration is successful, otherwise an error
     */
    @Override
    public Mono<UserRes> registerUser(String email, String password) {
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User(email, hashedPassword.getBytes());
        return userRepository.save(
                newUser.getUserId(),
                newUser.getEmail(),
                newUser.getPassword(),
                newUser.getLastLogin(),
                newUser.getCreated())
            .map(user -> new UserRes(
                user.getUserId().toString(),
                user.getEmail(),
                jwtUtil.generateToken(user.getUserId().toString(), user.getEmail())))
            .onErrorResume(DataIntegrityViolationException.class, e -> {
                if (e.getMessage().contains("users_email_key")) {
                    return Mono.error(new DuplicateKeyException("User with email " + email + " already exists"));
                }
                logger.error("Failed to register user: ", e);
                return Mono.error(new RuntimeException("Failed to register user"));
            });
    }

}
