package com.Project.TalentConnect.services;

import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.OrderEntity;
import com.Project.TalentConnect.entity.OrderStatus;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.repository.GigRepository;
import com.Project.TalentConnect.repository.OrderRepository;
import com.Project.TalentConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GigRepository gigRepository;

    //place order
    public OrderEntity placeOrder(Long gigId, Long clientId){

        UserEntity client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        GigEntity gig = gigRepository.findById(gigId)
                .orElseThrow(() -> new RuntimeException("Gig not found"));

        OrderEntity order = OrderEntity.builder()
                .client(client)
                .gig(gig)
                .totalAmount(gig.getPrice())
                .status(OrderStatus.PENDING)
                .orderDate(LocalDateTime.now())
                .build();

        return orderRepository.save(order);
    }

    //get all orders
    public List<OrderEntity> getAllOrders(){
        return orderRepository.findAll();
    }

    //get order by client
    public List<OrderEntity> getOrderByClient(Long clientId){
        UserEntity client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not Found"));

        return orderRepository.findByClient(client);
    }

    //update Order status
    public OrderEntity updateOrderStatus(Long orderId, OrderStatus status){
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not Found"));

        order.setStatus(status);
        return orderRepository.save(order);
    }

    //delete order
    public void deleteOrder(Long orderId){
        if(!orderRepository.existsById(orderId)){
            throw new RuntimeException("Order not found");
        }

        orderRepository.deleteById(orderId);
    }
}

