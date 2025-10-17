package com.example.demo.service;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 사용자 관리 서비스
 * 사용자 CRUD 및 권한 관리 비즈니스 로직 처리
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Transactional
    public User createUser(String username, String password, String name, String email, Set<String> roleNames) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setEmail(email);
        user.setEnabled(true);

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
            roles.add(role);
        }
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
