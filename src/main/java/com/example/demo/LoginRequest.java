package com.example.demo;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    private boolean remember;
}
