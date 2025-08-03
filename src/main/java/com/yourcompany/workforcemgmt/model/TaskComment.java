package com.yourcompany.workforcemgmt.model;

import lombok.Data;

@Data
public class TaskComment {
    private Long id;
    private Long taskId;
    private Long userId;         // who made the comment
    private String commentText;
    private Long timestamp;      // epoch millis
}

