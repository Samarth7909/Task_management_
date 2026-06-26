import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { Task } from '../../models/task.model';

@Component({
  selector: 'app-task-list',
  templateUrl: './task-list.component.html',
  styleUrls: ['./task-list.component.css']
})
export class TaskListComponent implements OnChanges {

  @Input() tasks: Task[] = [];
  @Input() isLoading = false;

  @Output() markCompleted = new EventEmitter<number>();
  @Output() deleteTask    = new EventEmitter<number>();

  filteredTasks: Task[] = [];

  private _searchQuery = '';
  private _statusFilter: 'ALL' | 'PENDING' | 'COMPLETED' = 'ALL';

  get searchQuery()  { return this._searchQuery; }
  set searchQuery(v: string) { this._searchQuery = v; this.applyFilters(); }

  get statusFilter() { return this._statusFilter; }
  set statusFilter(v: 'ALL' | 'PENDING' | 'COMPLETED') { this._statusFilter = v; this.applyFilters(); }

  ngOnChanges(_changes: SimpleChanges): void { this.applyFilters(); }

  private applyFilters(): void {
    this.filteredTasks = this.tasks.filter(t => {
      const matchesSearch = !this._searchQuery ||
        t.name.toLowerCase().includes(this._searchQuery.toLowerCase()) ||
        (t.description || '').toLowerCase().includes(this._searchQuery.toLowerCase());
      const matchesStatus = this._statusFilter === 'ALL' || t.status === this._statusFilter;
      return matchesSearch && matchesStatus;
    });
  }

  formatDate(dateStr: string): string {
    return new Date(dateStr).toLocaleDateString('en-GB', { day: '2-digit', month: 'short', year: 'numeric' });
  }

  onMarkCompleted(id: number): void { this.markCompleted.emit(id); }
  onDelete(id: number): void        { this.deleteTask.emit(id); }
}
