package com.authsfa.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRes {
    String email;
    @JsonProperty("user_id")
    String userId;
    String token;
}
