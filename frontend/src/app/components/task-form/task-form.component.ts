import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TaskRequest } from '../../models/task.model';

@Component({
  selector: 'app-task-form',
  templateUrl: './task-form.component.html',
  styleUrls: ['./task-form.component.css']
})
export class TaskFormComponent {

  @Output() taskCreated = new EventEmitter<TaskRequest>();

  taskForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.taskForm = this.fb.group({
      name:        ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', Validators.maxLength(500)]
    });
  }

  get nameControl()        { return this.taskForm.get('name')!; }
  get descriptionControl() { return this.taskForm.get('description')!; }

  onSubmit(): void {
    if (this.taskForm.invalid) { this.taskForm.markAllAsTouched(); return; }
    const { name, description } = this.taskForm.value;
    this.taskCreated.emit({ name: name.trim(), ...(description ? { description } : {}) });
    this.taskForm.reset();
  }

  onReset(): void {
    this.taskForm.reset();
  }
}
