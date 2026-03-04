package com.Project.TalentConnect.DTO;

import com.Project.TalentConnect.entity.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderResponseDto{

    private Long id;

    private Long gigId;
    private String gigTitle;

    private Long clientId;
    private String clientName;

    private double totalAmount;

    private OrderStatus status;
    private LocalDateTime orderDate;
}


