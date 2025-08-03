package com.yourcompany.workforcemgmt.repositories;

import com.yourcompany.workforcemgmt.model.TaskActivity;
import com.yourcompany.workforcemgmt.model.TaskComment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryTaskActivityRepository implements TaskActivityRepository{
        private final Map<Long, List<TaskActivity>> store = new ConcurrentHashMap<>();
        private final AtomicLong idGen = new AtomicLong(1);

        @Override
        public List<TaskActivity> findByTaskId(Long taskId) {
            return store.getOrDefault(taskId, new ArrayList<>());
        }

        @Override
        public TaskActivity save(TaskActivity activity) {
            activity.setId(idGen.getAndIncrement());
            store.computeIfAbsent(activity.getTaskId(), k -> new ArrayList<>()).add(activity);
            return activity;
        }
}
