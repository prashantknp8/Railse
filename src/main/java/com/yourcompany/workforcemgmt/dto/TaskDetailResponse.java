package com.yourcompany.workforcemgmt.dto;

import com.yourcompany.workforcemgmt.model.TaskActivity;
import com.yourcompany.workforcemgmt.model.TaskComment;
import lombok.Data;

import java.util.List;

@Data
public class TaskDetailResponse {
    private TaskManagementDto task;
    private List<TaskComment> comments;
    private List<TaskActivity> activityHistory;
}
