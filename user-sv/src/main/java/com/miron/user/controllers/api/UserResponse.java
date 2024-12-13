package com.miron.user.controllers.api;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private int id;
    private String username;
    private int balance;
}
