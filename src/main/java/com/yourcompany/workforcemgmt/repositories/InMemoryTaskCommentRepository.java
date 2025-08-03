package com.yourcompany.workforcemgmt.repositories;

import com.yourcompany.workforcemgmt.model.TaskComment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTaskCommentRepository implements TaskCommentRepository {
    private final Map<Long, List<TaskComment>> store = new ConcurrentHashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);

    @Override
    public List<TaskComment> findByTaskId(Long taskId) {
        return store.getOrDefault(taskId, new ArrayList<>());
    }

    @Override
    public TaskComment save(TaskComment comment) {
        comment.setId(idGen.getAndIncrement());
        store.computeIfAbsent(comment.getTaskId(), k -> new ArrayList<>()).add(comment);
        return comment;
    }
}

