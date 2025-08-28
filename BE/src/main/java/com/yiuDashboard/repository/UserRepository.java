package com.yiuDashboard.repository;

import com.yiuDashboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 로그인 ID를 갖는 객체 반환
    User findByLoginId(String loginId);

    // 로그인 ID를 갖는 객체가 존재하는지 => 존재하면 true 리턴 (ID 중복 검사 시 필요)
    boolean existsByLoginId(String loginId);

//    아이디 찾기
    Optional<User> findByEmail(String email);
}
