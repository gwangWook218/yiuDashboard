package com.yiuDashboard.service;

import com.yiuDashboard.entity.LoginRequest;
import com.yiuDashboard.entity.RegisterRequest;
import com.yiuDashboard.entity.User;
import com.yiuDashboard.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public boolean checkLoginIdDuplicate(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public void register(RegisterRequest registerRequest) {
        if (userRepository.existsByLoginId(registerRequest.getLoginId())) {
            return;
        }
        registerRequest.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        userRepository.save(registerRequest.toEntity());
    }

    public User login(LoginRequest loginRequest) {
        User findUser = userRepository.findByLoginId(loginRequest.getLoginId());

        if (findUser == null) {
            return null;
        }

        if (!bCryptPasswordEncoder.matches(loginRequest.getPassword(), findUser.getPassword())) {
            return null;
        }

        return findUser;
    }

    public User getLoginUserById(String loginId) {
        if (loginId == null) return null;
        return userRepository.findByLoginId(loginId);
    }
}
