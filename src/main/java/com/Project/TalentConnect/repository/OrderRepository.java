package com.Project.TalentConnect.repository;

import com.Project.TalentConnect.entity.Order;
import com.Project.TalentConnect.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
    List<Order> findByClient(UserEntity client);
}
