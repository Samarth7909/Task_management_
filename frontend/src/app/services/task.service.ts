import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Task, TaskStatus, DashboardSummary, TaskRequest } from '../models/task.model';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class TaskService {

  private readonly baseUrl = `${environment.apiUrl}/tasks`;

  constructor(private http: HttpClient) {}

  getAllTasks(): Observable<Task[]> {
    return this.http.get<Task[]>(this.baseUrl);
  }

  createTask(request: TaskRequest): Observable<Task> {
    return this.http.post<Task>(this.baseUrl, request);
  }

  updateTaskStatus(id: number, status: TaskStatus): Observable<Task> {
    return this.http.patch<Task>(`${this.baseUrl}/${id}/status`, null, { params: { status } });
  }

  deleteTask(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  getDashboard(): Observable<DashboardSummary> {
    return this.http.get<DashboardSummary>(`${this.baseUrl}/dashboard`);
  }
}
