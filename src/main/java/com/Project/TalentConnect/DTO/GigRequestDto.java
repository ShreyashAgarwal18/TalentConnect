package com.Project.TalentConnect.DTO;

import com.Project.TalentConnect.entity.GigStatus;
import lombok.Data;

@Data
public class GigRequestDto {
    private String title;
    private String description;
    private double price;
    private String category;
    private GigStatus status;
}

