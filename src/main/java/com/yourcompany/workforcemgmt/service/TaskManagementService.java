package com.yourcompany.workforcemgmt.service;


import com.yourcompany.workforcemgmt.dto.*;
import com.yourcompany.workforcemgmt.model.TaskActivity;
import com.yourcompany.workforcemgmt.model.TaskComment;
import com.yourcompany.workforcemgmt.model.enums.Priority;

import java.util.List;

public interface TaskManagementService {
    List<TaskManagementDto> createTasks(CreateTaskRequest request);
    List<TaskManagementDto> updateTasks(UpdateTaskRequest request);
    String assignByReference(AssignByReferenceRequest request);
    List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request);
    TaskManagementDto findTaskById(Long id);
    public List<TaskManagementDto> getTasksByPriority(Priority priority);

    TaskManagementDto updateTaskPriority(UpdatePriorityRequest request);

    public void addComment(Long taskId, Long userId, String text) ;
    public void logActivity(Long taskId, String message) ;
    public TaskManagementDto getTaskById(Long taskId);
    public List<TaskComment> getComments(Long taskId);
    public List<TaskActivity> getActivityHistory(Long taskId);


}
