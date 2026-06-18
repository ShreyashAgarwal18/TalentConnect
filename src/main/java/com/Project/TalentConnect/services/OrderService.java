package com.Project.TalentConnect.services;

import com.Project.TalentConnect.DTO.OrderRequestDto;
import com.Project.TalentConnect.DTO.OrderResponseDto;
import com.Project.TalentConnect.entity.GigEntity;
import com.Project.TalentConnect.entity.OrderEntity;
import com.Project.TalentConnect.entity.OrderStatus;
import com.Project.TalentConnect.entity.UserEntity;
import com.Project.TalentConnect.exception.ResourceNotFoundException;
import com.Project.TalentConnect.repository.GigRepository;
import com.Project.TalentConnect.repository.OrderRepository;
import com.Project.TalentConnect.repository.UserRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.Project.TalentConnect.exception.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final GigRepository gigRepository;


    //place order
    @Transactional
    public OrderResponseDto placeOrder(OrderRequestDto request, String email){

        UserEntity client = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with email: " + email));

        GigEntity gig = gigRepository.findById(request.getGigId())
                .orElseThrow(() -> new ResourceNotFoundException("Gig not found with id: " + request.getGigId()));

        OrderEntity order = OrderEntity.builder()
                .client(client)
                .gig(gig)
                .totalAmount(request.getTotalAmount())
                .deadline(request.getDeadline())
                .status(OrderStatus.PENDING)
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

    //get order by email
    public List<OrderResponseDto> getOrdersByEmail(String email){

        UserEntity client = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return orderRepository.findByClient(client)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    //update Order status
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, OrderStatus status, String callerEmail){
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));


        if(!order.getClient().getEmail().equals(callerEmail)){
                throw new BadRequestException("You are not authorized to update this order");
        }        

        order.setStatus(status);
        return mapToResponse(orderRepository.save(order));
    }

    //delete order
    @Transactional
    public void deleteOrder(Long orderId, String callerEmail) {
        OrderEntity order = orderRepository.findById(orderId)
                                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));


       if(!order.getClient().getEmail().equals(callerEmail)){
           throw new BadRequestException("You are not authorized to delete this order");
       }

       orderRepository.delete(order);
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
                .deadline(order.getDeadline())
                .build();
    }

}



