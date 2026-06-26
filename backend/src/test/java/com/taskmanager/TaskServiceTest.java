package com.taskmanager;

import com.taskmanager.model.DashboardSummary;
import com.taskmanager.model.Task;
import com.taskmanager.model.Task.TaskStatus;
import com.taskmanager.model.TaskRequest;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskService = new TaskService(new TaskRepository());
    }

    @Test
    void getAllTasks_shouldReturnSampleTasksOnStartup() {
        List<Task> tasks = taskService.getAllTasks();
        assertFalse(tasks.isEmpty());
        assertEquals(2, tasks.size());
    }

    @Test
    void createTask_shouldAddTaskWithPendingStatus() {
        TaskRequest request = new TaskRequest();
        request.setName("New Task");

        Task created = taskService.createTask(request);

        assertEquals(TaskStatus.PENDING, created.getStatus());
        assertEquals("New Task", created.getName());
        assertNotNull(created.getId());
        assertNotNull(created.getCreatedDate());
    }

    @Test
    void createTask_shouldTrimWhitespaceFromName() {
        TaskRequest request = new TaskRequest();
        request.setName("  Trimmed Task  ");

        Task created = taskService.createTask(request);

        assertEquals("Trimmed Task", created.getName());
    }

    @Test
    void updateTaskStatus_shouldMarkTaskAsCompleted() {
        TaskRequest request = new TaskRequest();
        request.setName("Task to complete");
        Task created = taskService.createTask(request);

        Optional<Task> updated = taskService.updateTaskStatus(created.getId(), TaskStatus.COMPLETED);

        assertTrue(updated.isPresent());
        assertEquals(TaskStatus.COMPLETED, updated.get().getStatus());
    }

    @Test
    void updateTaskStatus_shouldReturnEmptyForNonExistentId() {
        Optional<Task> result = taskService.updateTaskStatus(9999L, TaskStatus.COMPLETED);
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteTask_shouldRemoveTaskFromList() {
        int sizeBefore = taskService.getAllTasks().size();
        long existingId = taskService.getAllTasks().get(0).getId();

        boolean deleted = taskService.deleteTask(existingId);

        assertTrue(deleted);
        assertEquals(sizeBefore - 1, taskService.getAllTasks().size());
    }

    @Test
    void deleteTask_shouldReturnFalseForNonExistentId() {
        boolean result = taskService.deleteTask(9999L);
        assertFalse(result);
    }

    @Test
    void getDashboardSummary_shouldReturnCorrectCounts() {
        DashboardSummary summary = taskService.getDashboardSummary();

        assertEquals(2, summary.getTotalTasks());
        assertEquals(1, summary.getPendingTasks());
        assertEquals(1, summary.getCompletedTasks());
    }
}
