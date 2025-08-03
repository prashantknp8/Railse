package com.yourcompany.workforcemgmt.dto;

import lombok.Data;

@Data
public class CommentRequest {
    private Long userId;
    private String commentText;
}

