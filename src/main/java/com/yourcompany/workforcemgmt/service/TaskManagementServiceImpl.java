package com.yourcompany.workforcemgmt.service;


import com.yourcompany.workforcemgmt.dto.*;
import com.yourcompany.workforcemgmt.exceptions.ResourceNotFoundException;
import com.yourcompany.workforcemgmt.mapper.ITaskManagementMapper;
import com.yourcompany.workforcemgmt.model.TaskActivity;
import com.yourcompany.workforcemgmt.model.TaskComment;
import com.yourcompany.workforcemgmt.model.TaskManagement;
import com.yourcompany.workforcemgmt.model.enums.Priority;
import com.yourcompany.workforcemgmt.model.enums.Task;
import com.yourcompany.workforcemgmt.model.enums.TaskStatus;
import com.yourcompany.workforcemgmt.repositories.TaskActivityRepository;
import com.yourcompany.workforcemgmt.repositories.TaskCommentRepository;
import com.yourcompany.workforcemgmt.repositories.TaskRepository;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class TaskManagementServiceImpl implements TaskManagementService {


    private final TaskRepository taskRepository;
    private final ITaskManagementMapper taskMapper;

    private final TaskActivityRepository activityRepository;
    private final TaskCommentRepository commentRepository;


    public TaskManagementServiceImpl(TaskRepository taskRepository, ITaskManagementMapper taskMapper,TaskCommentRepository commentRepository,TaskActivityRepository activityRepository) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.activityRepository= activityRepository;
        this.commentRepository= commentRepository;

    }


    @Override
    public TaskManagementDto findTaskById(Long id) {
        TaskManagement task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + id));
        return taskMapper.modelToDto(task);
    }

    @Override
    public List<TaskManagementDto> getTasksByPriority(Priority priority) {
        List<TaskManagement> tasks = taskRepository.findByPriority(priority);
        return taskMapper.modelListToDtoList(tasks);
    }


    @Override
    public TaskManagementDto updateTaskPriority(UpdatePriorityRequest request) {
        TaskManagement task = taskRepository.findById(request.getTaskId())
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setPriority(request.getNewPriority());
        TaskManagement updatedTask = taskRepository.save(task);

        return taskMapper.modelToDto(updatedTask);
    }

    @Override
    public void addComment(Long taskId, Long userId, String text) {
        TaskComment comment = new TaskComment();
        comment.setTaskId(taskId);
        comment.setUserId(userId);
        comment.setCommentText(text);
        comment.setTimestamp(System.currentTimeMillis());

        commentRepository.save(comment);
    }

    @Override
    public void logActivity(Long taskId, String message)
        {
            TaskActivity activity = new TaskActivity();
            activity.setTaskId(taskId);
            activity.setMessage(message);
            activity.setTimestamp(System.currentTimeMillis());

            activityRepository.save(activity);
        }

    @Override
    public TaskManagementDto getTaskById(Long taskId) {
        TaskManagement task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        return taskMapper.modelToDto(task);
    }

    @Override
    public List<TaskComment> getComments(Long taskId) {
        return commentRepository.findByTaskId(taskId);
    }

    @Override
    public List<TaskActivity> getActivityHistory(Long taskId) {
        return activityRepository.findByTaskId(taskId);
    }



    @Override
    public List<TaskManagementDto> createTasks(CreateTaskRequest createRequest) {
        List<TaskManagement> createdTasks = new ArrayList<>();
        for (CreateTaskRequest.RequestItem item : createRequest.getRequests()) {
            TaskManagement newTask = new TaskManagement();
            newTask.setReferenceId(item.getReferenceId());
            newTask.setReferenceType(item.getReferenceType());
            newTask.setTask(item.getTask());
            newTask.setAssigneeId(item.getAssigneeId());
            newTask.setPriority(item.getPriority());
            newTask.setTaskDeadlineTime(item.getTaskDeadlineTime());
            newTask.setStatus(TaskStatus.ASSIGNED);
            newTask.setDescription("New task created.");
            createdTasks.add(taskRepository.save(newTask));

            System.out.println("Incoming RequestItem: " + item);
        }
        return taskMapper.modelListToDtoList(createdTasks);
    }


    @Override
    public List<TaskManagementDto> updateTasks(UpdateTaskRequest updateRequest) {
        List<TaskManagement> updatedTasks = new ArrayList<>();
        for (UpdateTaskRequest.RequestItem item : updateRequest.getRequests()) {
            TaskManagement task = taskRepository.findById(item.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + item.getTaskId()));


            if (item.getTaskStatus() != null) {
                task.setStatus(item.getTaskStatus());
            }
            if (item.getDescription() != null) {
                task.setDescription(item.getDescription());
            }
            updatedTasks.add(taskRepository.save(task));
        }
        return taskMapper.modelListToDtoList(updatedTasks);
    }


    @Override
    public String assignByReference(AssignByReferenceRequest request) {
        List<Task> applicableTasks = Task.getTasksByReferenceType(request.getReferenceType());
        List<TaskManagement> existingTasks = taskRepository.findByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());
        List<Long> assignIds= taskRepository.findAssigneeIdsByReferenceIdAndReferenceType(request.getReferenceId(), request.getReferenceType());

        for (Task taskType : applicableTasks) {
            List<TaskManagement> tasksOfType = existingTasks.stream()
                    .filter(t -> t.getTask() == taskType && t.getStatus() != TaskStatus.COMPLETED )

                    .collect(Collectors.toList());


            // BUG #1 is here. It should assign one and cancel the rest.
            // Instead, it reassigns ALL of them.
            if (!tasksOfType.isEmpty()) {
                for (TaskManagement taskToUpdate : tasksOfType) {
                    Long id=taskToUpdate.getId();

                    if(!assignIds.contains(id))
                        taskToUpdate.setAssigneeId(request.getAssigneeId());

                        taskToUpdate.setStatus(TaskStatus.CANCELLED);

                    taskRepository.save(taskToUpdate);
                }
            } else {
                // Create a new task if none exist
                TaskManagement newTask = new TaskManagement();
                newTask.setReferenceId(request.getReferenceId());
                newTask.setReferenceType(request.getReferenceType());
                newTask.setTask(taskType);
                newTask.setAssigneeId(request.getAssigneeId());
                newTask.setStatus(TaskStatus.ASSIGNED);
                taskRepository.save(newTask);
            }
        }
        return "Tasks assigned successfully for reference " + request.getReferenceId();
    }


    @Override
    public List<TaskManagementDto> fetchTasksByDate(TaskFetchByDateRequest request) {
        List<TaskManagement> tasks = taskRepository.findByAssigneeIdIn(request.getAssigneeIds());


        // BUG #2 is here. It should filter out CANCELLED tasks but doesn't.
        List<TaskManagement> filteredTasks = tasks.stream()
                .filter(t -> t.getStatus() != TaskStatus.CANCELLED &&  t.getStatus() != TaskStatus.COMPLETED)
                .collect(Collectors.toList());


        return taskMapper.modelListToDtoList(filteredTasks);
    }
}



