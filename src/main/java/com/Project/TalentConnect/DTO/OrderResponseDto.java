package com.Project.TalentConnect.DTO;

import com.Project.TalentConnect.entity.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponseDto{

    private Long id;

    private Long gigId;
    private String gigTitle;

    private Long clientId;
    private String clientName;

    private double totalAmount;

    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime deadline;
}




