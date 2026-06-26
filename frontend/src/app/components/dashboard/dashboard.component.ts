import { Component, Input } from '@angular/core';
import { DashboardSummary } from '../../models/task.model';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

  @Input() summary: DashboardSummary = {
    totalTasks: 0,
    pendingTasks: 0,
    completedTasks: 0
  };

}
