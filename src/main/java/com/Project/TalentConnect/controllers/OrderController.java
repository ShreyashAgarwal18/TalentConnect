package com.Project.TalentConnect.controllers;

import com.Project.TalentConnect.DTO.OrderResponseDto;
import com.Project.TalentConnect.entity.OrderEntity;
import com.Project.TalentConnect.entity.OrderStatus;
import com.Project.TalentConnect.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    //place order
    @PostMapping("/gig/{gigId}/client/{clientId}")
    public OrderResponseDto placeOrder(@PathVariable Long gigId,
                                       @PathVariable Long clientId){
        return orderService.placeOrder(gigId, clientId);
    }

    //get all orders
    @GetMapping
    public List<OrderResponseDto> getAllOrders(){
        return orderService.getAllOrders();
    }

    //get orders by client
    @GetMapping("/client/{clientId}")
    public List<OrderResponseDto> getOrderByClient(@PathVariable Long clientId){
        return orderService.getOrderByClient(clientId);
    }

    //update order
    @PatchMapping("/{orderId}/status")
    public OrderResponseDto updateOrderStatus(@PathVariable Long orderId,
                                         @RequestParam OrderStatus status){
        return orderService.updateOrderStatus(orderId, status);
    }

    //delete order
    @DeleteMapping("/{orderId}")
    public String deleteOrder(@PathVariable Long orderId){
        orderService.deleteOrder(orderId);
        return "Order deleted successfully";
    }
}
