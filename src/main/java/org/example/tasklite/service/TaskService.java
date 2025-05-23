package org.example.tasklite.service;

import org.example.tasklite.model.Task;
import org.example.tasklite.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final Map<UUID, Task> tasks = new ConcurrentHashMap<>();

    public Task createTask(Task task) {
        UUID id = UUID.randomUUID();
        task.setId(id);
        tasks.put(id, task);
        return task;
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public List<Task> filterByStatus(String status) {
        return tasks.values().stream()
                .filter(t -> t.getStatus().equalsIgnoreCase(status))
                .collect(Collectors.toList());
    }

    public List<Task> sortByDueDate() {
        return tasks.values().stream()
                .sorted(Comparator.comparing(Task::getDueDate))
                .collect(Collectors.toList());
    }

    public Task updateTask(UUID id, Task updatedTask) {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException(id);
        }
        updatedTask.setId(id);
        tasks.put(id, updatedTask);
        return updatedTask;
    }

    public void deleteTask(UUID id) {
        if (!tasks.containsKey(id)) {
            throw new TaskNotFoundException(id);
        }
        tasks.remove(id);
    }
}
