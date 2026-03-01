package com.Project.TalentConnect.repository;

import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GigRepository extends JpaRepository<GigEntity, Long>{
    List<GigEntity> findByFreelancer(UserEntity freelancer);
    List<GigEntity> findByCategory(String category);
}
