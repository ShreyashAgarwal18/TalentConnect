package com.Project.TalentConnect.controllers;

import com.Project.TalentConnect.DTO.OrderRequestDto;
import com.Project.TalentConnect.DTO.OrderResponseDto;
import com.Project.TalentConnect.entity.OrderStatus;
import com.Project.TalentConnect.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    //place order
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@Valid @RequestBody OrderRequestDto request, Authentication authentication){
        String email = authentication.getName();
        OrderResponseDto response = orderService.placeOrder(request,email);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //get all orders
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(){

        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    //get orders by client
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders(Authentication authentication){
        String email = authentication.getName();
        List<OrderResponseDto> orders = orderService.getOrdersByEmail(email);
        return ResponseEntity.ok(orders);
    }

    //update order
    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId,
                                         @RequestParam OrderStatus status){
        OrderResponseDto response = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(response);
    }

    //delete order
    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}

