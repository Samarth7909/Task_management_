package com.taskmanager.service;

import com.taskmanager.model.DashboardSummary;
import com.taskmanager.model.Task;
import com.taskmanager.model.Task.TaskStatus;
import com.taskmanager.model.TaskRequest;
import com.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(TaskRequest request) {
        Task task = new Task();
        task.setName(request.getName().trim());
        task.setDescription(request.getDescription() != null ? request.getDescription().trim() : null);
        task.setStatus(TaskStatus.PENDING);
        task.setCreatedDate(LocalDateTime.now());
        return taskRepository.save(task);
    }

    public Optional<Task> updateTaskStatus(Long id, TaskStatus status) {
        return taskRepository.findById(id).map(task -> {
            task.setStatus(status);
            return task;
        });
    }

    public boolean deleteTask(Long id) {
        return taskRepository.deleteById(id);
    }

    public DashboardSummary getDashboardSummary() {
        long total = taskRepository.findAll().size();
        long pending = taskRepository.countByStatus(TaskStatus.PENDING);
        long completed = taskRepository.countByStatus(TaskStatus.COMPLETED);
        return new DashboardSummary(total, pending, completed);
    }
}
