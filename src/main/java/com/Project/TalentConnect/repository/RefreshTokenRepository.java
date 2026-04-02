package com.Project.TalentConnect.repository;

import com.Project.TalentConnect.entity.RefreshToken;
import com.Project.TalentConnect.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(UserEntity user);
}
