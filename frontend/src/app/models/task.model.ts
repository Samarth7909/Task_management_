// Mirrors the Java Task.TaskStatus enum
export type TaskStatus = 'PENDING' | 'COMPLETED';

// Mirrors the Java Task entity
export interface Task {
  id: number;
  name: string;
  description: string;
  status: TaskStatus;
  createdDate: string; // ISO 8601 from backend
}

// Mirrors the Java DashboardSummary model
export interface DashboardSummary {
  totalTasks: number;
  pendingTasks: number;
  completedTasks: number;
}

// Mirrors the Java TaskRequest DTO
export interface TaskRequest {
  name: string;
  description?: string;
}
