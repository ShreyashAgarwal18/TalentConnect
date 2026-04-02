package com.Project.TalentConnect.repository;

import com.Project.TalentConnect.entity.OrderEntity;
import com.Project.TalentConnect.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long>{
    List<OrderEntity> findByClient(UserEntity client);
    List<OrderEntity> findByClientEmail(String email);
}
