package com.Project.TalentConnect.controllers;

import com.Project.TalentConnect.DTO.OrderRequestDto;
import com.Project.TalentConnect.DTO.OrderResponseDto;
import com.Project.TalentConnect.entity.OrderStatus;
import com.Project.TalentConnect.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    //place order
    @PostMapping
    public ResponseEntity<OrderResponseDto> placeOrder(@Valid @RequestBody OrderRequestDto request){
        OrderResponseDto response = orderService.placeOrder(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //get all orders
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(){

        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    //get orders by client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<OrderResponseDto>> getOrderByClient(@PathVariable Long clientId){
        List<OrderResponseDto> orders = orderService.getOrderByClient(clientId);
        return ResponseEntity.ok(orders);
    }

    //update order
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId,
                                         @RequestParam OrderStatus status){
        OrderResponseDto response = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(response);
    }

    //delete order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long orderId){
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}

