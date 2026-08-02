package com.Project.TalentConnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.Project.TalentConnect.entity.ReviewEntity;
import java.util.List;


public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>{

    boolean existsByOrderId(Long orderId);

    List<ReviewEntity> findByGigId(Long  gigId);

    @Query("SELECT r FROM ReviewEntity r WHERE r.gig.freelancer.id = :freelancerId")
    List<ReviewEntity> findByFreelancerId(@Param("freelancerId") Long freeLancerId);

    @Query("SELECT AVG(r.rating) FROM ReviewEntity r WHERE r.gig.id = :gigId")
    Double findAverageRatingByGigId(@Param("gigId") Long gigId);

} 
