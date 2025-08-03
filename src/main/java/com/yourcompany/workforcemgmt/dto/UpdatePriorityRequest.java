package com.yourcompany.workforcemgmt.dto;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.yourcompany.workforcemgmt.model.enums.Priority;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdatePriorityRequest {
    private Long taskId;
    private Priority newPriority;
}