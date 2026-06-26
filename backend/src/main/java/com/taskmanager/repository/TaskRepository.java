package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.Task.TaskStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class TaskRepository {

    private final List<Task> tasks = new CopyOnWriteArrayList<>();
    private final java.util.concurrent.atomic.AtomicLong idSequence = new java.util.concurrent.atomic.AtomicLong(1);

    public TaskRepository() {
        tasks.add(new Task(idSequence.getAndIncrement(), "Setup project", "Initialize Spring Boot project", TaskStatus.COMPLETED, LocalDateTime.now().minusDays(2)));
        tasks.add(new Task(idSequence.getAndIncrement(), "Write tests", "Add unit tests for service layer", TaskStatus.PENDING, LocalDateTime.now().minusDays(1)));
    }

    public List<Task> findAll() {
        return List.copyOf(tasks);
    }

    public Optional<Task> findById(Long id) {
        return tasks.stream().filter(t -> t.getId().equals(id)).findFirst();
    }

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(idSequence.getAndIncrement());
            tasks.add(task);
        }
        return task;
    }

    public boolean deleteById(Long id) {
        return tasks.removeIf(t -> t.getId().equals(id));
    }

    public long countByStatus(TaskStatus status) {
        return tasks.stream().filter(t -> t.getStatus() == status).count();
    }
}
