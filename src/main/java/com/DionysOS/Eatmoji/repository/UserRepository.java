package com.DionysOS.Eatmoji.repository;

import com.DionysOS.Eatmoji.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    boolean existsByEmail(String email);  // 이메일 중복 체크용

    Optional<User> findByEmail(String email); // 로그인 시에 사용할 이메일 조회
}