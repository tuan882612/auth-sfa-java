package com.authsfa.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Table("users")
public class User {
    @Id
    @JsonProperty("user_id")
    @Column("user_id")
    public UUID userId;
    public String email;
    public byte[] password;
    @JsonProperty("last_login")
    @Column("last_login")
    public LocalDateTime lastLogin;
    public LocalDateTime created;

    public User(String email, byte[] password) {
        this.userId = UUID.randomUUID();
        this.email = email;
        this.password = password;
        this.lastLogin = LocalDateTime.now();
        this.created = LocalDateTime.now();
    }
}
