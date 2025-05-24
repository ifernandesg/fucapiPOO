package org.example.tasklite.service;

import org.example.tasklite.exception.TaskNotFoundException;
import org.example.tasklite.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setup() {
        taskService = new TaskService();
    }

    @Test
    void shouldCreateTask() {
        Task task = new Task(null, "Title", "Description", "pendente", LocalDate.now());
        Task created = taskService.createTask(task);
        assertNotNull(created.getId());
        assertEquals("Title", created.getTitle());
    }

    @Test
    void shouldUpdateExistingTask() {
        Task task = taskService.createTask(new Task(null, "Old", "Desc", "pendente", LocalDate.now()));
        UUID id = task.getId();
        Task updated = new Task(null, "New", "Updated", "concluída", LocalDate.now().plusDays(1));

        Task result = taskService.updateTask(id, updated);

        assertEquals("New", result.getTitle());
        assertEquals("Updated", result.getDescription());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonexistentTask() {
        UUID fakeId = UUID.randomUUID();
        Task fakeTask = new Task(null, "Fake", "None", "pendente", LocalDate.now());

        assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(fakeId, fakeTask);
        });
    }

    @Test
    void shouldDeleteTask() {
        Task task = taskService.createTask(new Task(null, "Delete", "Me", "pendente", LocalDate.now()));
        UUID id = task.getId();

        taskService.deleteTask(id);

        assertEquals(0, taskService.getAllTasks().size());
    }

    @Test
    void shouldFilterByStatus() {
        taskService.createTask(new Task(null, "T1", "Test", "pendente", LocalDate.now()));
        taskService.createTask(new Task(null, "T2", "Test", "concluída", LocalDate.now()));

        List<Task> filtered = taskService.filterByStatus("pendente");
        assertEquals(1, filtered.size());
        assertEquals("T1", filtered.get(0).getTitle());
    }
}
