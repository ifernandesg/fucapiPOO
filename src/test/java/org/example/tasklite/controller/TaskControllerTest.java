package org.example.tasklite.controller;

import org.example.tasklite.model.Task;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.example.tasklite.service.TaskService;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTask() throws Exception {
        Task task = new Task(null, "Test", "Desc", "pendente", LocalDate.now());
        Task created = new Task(UUID.randomUUID(), "Test", "Desc", "pendente", LocalDate.now());

        when(taskService.createTask(any(Task.class))).thenReturn(created);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Test"));
    }

    @Test
    void shouldGetAllTasks() throws Exception {
        Task task = new Task(UUID.randomUUID(), "List", "All", "pendente", LocalDate.now());
        when(taskService.getAllTasks()).thenReturn(Collections.singletonList(task));

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("List"));
    }
}
