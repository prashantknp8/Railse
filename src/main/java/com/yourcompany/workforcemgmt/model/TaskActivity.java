package com.yourcompany.workforcemgmt.model;

import lombok.Data;

@Data
public class TaskActivity {
    private Long id;
    private Long taskId;
    private String message;     // e.g., "User A changed priority to HIGH"
    private Long timestamp;     // epoch millis
}
