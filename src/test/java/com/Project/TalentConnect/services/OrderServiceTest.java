package com.Project.TalentConnect.services;

import com.Project.TalentConnect.entity.*;
import com.Project.TalentConnect.exception.BadRequestException;
import com.Project.TalentConnect.exception.ResourceNotFoundException;
import com.Project.TalentConnect.repository.GigRepository;
import com.Project.TalentConnect.repository.OrderRepository;
import com.Project.TalentConnect.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock private OrderRepository orderRepository;
    @Mock private UserRepository userRepository;
    @Mock private GigRepository gigRepository;

    @InjectMocks private OrderService orderService;

    @Test
    void updateOrderStatus_ShouldThrow_WhenCallerIsNotOwner() {
        UserEntity owner = new UserEntity();
        owner.setEmail("owner@test.com");

        OrderEntity order = new OrderEntity();
        order.setClient(owner);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,
            () -> orderService.updateOrderStatus(1L, OrderStatus.COMPLETED, "hacker@test.com"));
    }

    @Test
    void deleteOrder_ShouldThrow_WhenOrderNotFound() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
            () -> orderService.deleteOrder(99L, "anyone@test.com"));
    }

    @Test
    void deleteOrder_ShouldThrow_WhenCallerIsNotOwner() {
        UserEntity owner = new UserEntity();
        owner.setEmail("owner@test.com");

        OrderEntity order = new OrderEntity();
        order.setClient(owner);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(BadRequestException.class,
            () -> orderService.deleteOrder(1L, "hacker@test.com"));
    }
}
