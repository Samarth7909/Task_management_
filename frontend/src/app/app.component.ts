import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { Task, DashboardSummary, TaskRequest } from './models/task.model';
import { TaskService } from './services/task.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  tasks: Task[] = [];
  summary: DashboardSummary = { totalTasks: 0, pendingTasks: 0, completedTasks: 0 };
  isLoading = false;
  errorMessage = '';
  successMessage = '';

  constructor(private taskService: TaskService) {}

  ngOnInit(): void {
    this.refresh();
  }

  onTaskCreated(request: TaskRequest): void {
    this.taskService.createTask(request).subscribe({
      next: () => { this.showSuccess('Task created successfully!'); this.refresh(); },
      error: (err) => {
        const msg = err?.error?.name || err?.error?.message
          || (typeof err?.error === 'string' ? err.error : null)
          || 'Failed to create task.';
        this.showError(msg);
      }
    });
  }

  onMarkCompleted(taskId: number): void {
    this.taskService.updateTaskStatus(taskId, 'COMPLETED').subscribe({
      next: () => { this.showSuccess('Task marked as completed!'); this.refresh(); },
      error: () => this.showError('Failed to update task status.')
    });
  }

  onDeleteTask(taskId: number): void {
    if (!confirm('Are you sure you want to delete this task?')) return;
    this.taskService.deleteTask(taskId).subscribe({
      next: () => { this.showSuccess('Task deleted.'); this.refresh(); },
      error: () => this.showError('Failed to delete task.')
    });
  }

  private refresh(): void {
    this.isLoading = true;
    forkJoin({
      tasks:   this.taskService.getAllTasks(),
      summary: this.taskService.getDashboard()
    }).subscribe({
      next: ({ tasks, summary }) => {
        this.tasks   = tasks;
        this.summary = summary;
        this.isLoading = false;
      },
      error: () => { this.showError('Failed to load tasks.'); this.isLoading = false; }
    });
  }

  private showSuccess(msg: string): void {
    this.successMessage = msg;
    this.errorMessage = '';
    setTimeout(() => this.successMessage = '', 3000);
  }

  private showError(msg: string): void {
    this.errorMessage = msg;
    this.successMessage = '';
    setTimeout(() => this.errorMessage = '', 4000);
  }
}
