package com.Project.TalentConnect.repository;

import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GigRepository extends JpaRepository<GigEntity, Long>{
    List<GigEntity> findByFreelancer(UserEntity freelancer);
    List<GigEntity> findByCategory(String category);

    Page<GigEntity> findAll(Pageable pageable);

    @Query("""
            SELECT g FROM GigEntity g
            WHERE LOWER(g.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(g.description) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(g.category) LIKE LOWER(CONCAT('%', :keyword, '%'))
            """)
    List<GigEntity> search(@Param("keyword") String keyword);
}
