package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class AuthController {

    // Temporary in-memory storage (in production, use database)
    private final Map<String, String> users = new HashMap<>() {{
        put("admin", "admin123");
        put("test", "test123");
    }};

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        // Validate input
        if (username == null || username.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "아이디를 입력해주세요."));
        }

        if (password == null || password.trim().isEmpty()) {
            return ResponseEntity.badRequest()
                .body(Map.of("message", "비밀번호를 입력해주세요."));
        }

        // Check credentials
        if (users.containsKey(username) && users.get(username).equals(password)) {
            // Generate simple token (in production, use JWT)
            String token = UUID.randomUUID().toString();

            LoginResponse response = new LoginResponse(
                token,
                username,
                "로그인 성공"
            );

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(401)
                .body(Map.of("message", "아이디 또는 비밀번호가 올바르지 않습니다."));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(Map.of("message", "로그아웃 되었습니다."));
    }

    @GetMapping("/check")
    public ResponseEntity<?> check(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token != null && !token.trim().isEmpty()) {
            return ResponseEntity.ok(Map.of("authenticated", true));
        }
        return ResponseEntity.ok(Map.of("authenticated", false));
    }
}
