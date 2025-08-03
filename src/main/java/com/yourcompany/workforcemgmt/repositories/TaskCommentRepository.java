package com.yourcompany.workforcemgmt.repositories;

import com.yourcompany.workforcemgmt.model.TaskComment;

import java.util.List;

public interface TaskCommentRepository {
    List<TaskComment> findByTaskId(Long taskId);
    TaskComment save(TaskComment comment);
}

