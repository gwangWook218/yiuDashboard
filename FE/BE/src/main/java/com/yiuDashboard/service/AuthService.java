package com.yiuDashboard.service;

import com.yiuDashboard.entity.RegisterRequest;
import com.yiuDashboard.entity.RegisterResponse;
import com.yiuDashboard.entity.Role;
import com.yiuDashboard.entity.User;
import com.yiuDashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 사용 중인 아이디입니다.");
        }

        Role userRole = Role.valueOf(request.getRole());

        User newUser = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(userRole)
                .build();

        userRepository.save(newUser);

        return new RegisterResponse(
                newUser.getUsername(),
                newUser.getRole().name(),
                "회원가입 성공"
        );
    }
}
