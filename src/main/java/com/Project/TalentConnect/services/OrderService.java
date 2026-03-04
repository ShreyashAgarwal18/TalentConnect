package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.OrderResponseDto;
import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.OrderEntity;
import com.Project.TalentConnect.entity.OrderStatus;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.repository.GigRepository;
import com.Project.TalentConnect.repository.OrderRepository;
import com.Project.TalentConnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GigRepository gigRepository;
    private final ModelMapper modelMapper;

    //place order
    public OrderResponseDto placeOrder(Long gigId, Long clientId){

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

        OrderEntity savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder);
    }

    //get all orders
    public List<OrderResponseDto> getAllOrders(){

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //get order by client
    public List<OrderResponseDto> getOrderByClient(Long clientId){
        UserEntity client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not Found"));

        return orderRepository.findByClient(client)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    //update Order status
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status){
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not Found"));

        order.setStatus(status);

        OrderEntity updatedOrder = orderRepository.save(order);

        return mapToResponse(updatedOrder);
    }

    //delete order
    public void deleteOrder(Long orderId){
        if(!orderRepository.existsById(orderId)){
            throw new RuntimeException("Order not found");
        }

        orderRepository.deleteById(orderId);
    }

    private OrderResponseDto mapToResponse(OrderEntity order){
        return OrderResponseDto.builder()
                .id(order.getId())
                .gigId(order.getGig().getId())
                .gigTitle(order.getGig().getTitle())
                .clientId(order.getClient().getId())
                .clientName(order.getClient().getName())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .build();
    }
}


