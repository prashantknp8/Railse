package com.yourcompany.workforcemgmt.controller;


import com.yourcompany.workforcemgmt.dto.*;
import com.yourcompany.workforcemgmt.model.TaskActivity;
import com.yourcompany.workforcemgmt.model.TaskComment;
import com.yourcompany.workforcemgmt.model.enums.Priority;
import com.yourcompany.workforcemgmt.model.response.Response;
import com.yourcompany.workforcemgmt.service.TaskManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/task-mgmt")
public class TaskManagementController {


    private final TaskManagementService taskManagementService;


    public TaskManagementController(TaskManagementService taskManagementService) {
        this.taskManagementService = taskManagementService;
    }


    @GetMapping("/{id}")
    public Response<TaskManagementDto> getTaskById(@PathVariable Long id) {
        return new Response<>(taskManagementService.findTaskById(id));
    }


    @PostMapping("/create")
    public Response<List<TaskManagementDto>> createTasks(@RequestBody CreateTaskRequest request) {
        return new Response<>(taskManagementService.createTasks(request));
    }


    @PostMapping("/update")
    public Response<List<TaskManagementDto>> updateTasks(@RequestBody UpdateTaskRequest request) {
        return new Response<>(taskManagementService.updateTasks(request));
    }


    @PostMapping("/assign-by-ref")
    public Response<String> assignByReference(@RequestBody AssignByReferenceRequest request) {
        return new Response<>(taskManagementService.assignByReference(request));
    }


    @PostMapping("/fetch-by-date/v2")
    public Response<List<TaskManagementDto>> fetchByDate(@RequestBody TaskFetchByDateRequest request) {
        return new Response<>(taskManagementService.fetchTasksByDate(request));
    }

    @PutMapping("/update-priority")
    public Response<TaskManagementDto> updatePriority(@RequestBody UpdatePriorityRequest request) {

        return new Response<>(taskManagementService.updateTaskPriority(request));
    }

    @GetMapping("/by-priority")
    public Response<List<TaskManagementDto>> getTasksByPriority(@RequestParam Priority priority) {

        return new Response<>(taskManagementService.getTasksByPriority(priority));
    }

    @PostMapping("/comment/{taskId}")
    public ResponseEntity<Response> addComment(
            @PathVariable Long taskId,
            @RequestBody CommentRequest commentRequest
    ) {
        Long userId = commentRequest.getUserId();
        String commentText = commentRequest.getCommentText();

        taskManagementService.addComment(taskId, userId, commentText);
        taskManagementService.logActivity(taskId, "User " + userId + " added a comment");

        Response response = new Response(taskManagementService.getComments(taskId));
        return ResponseEntity.ok(response);
    }



    @GetMapping("/task/{taskId}")
    public ResponseEntity<TaskDetailResponse> getTaskDetails(@PathVariable Long taskId) {
        TaskManagementDto taskDto = taskManagementService.getTaskById(taskId);
        List<TaskComment> comments = taskManagementService.getComments(taskId);
        List<TaskActivity> history = taskManagementService.getActivityHistory(taskId);

        // Optional: sort by timestamp
        comments.sort(Comparator.comparingLong(TaskComment::getTimestamp));
        history.sort(Comparator.comparingLong(TaskActivity::getTimestamp));

        TaskDetailResponse response = new TaskDetailResponse();
        response.setTask(taskDto);
        response.setComments(comments);
        response.setActivityHistory(history);

        return ResponseEntity.ok(response);
    }



}


