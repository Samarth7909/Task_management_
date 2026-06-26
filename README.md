# Task Manager — Full Stack Application

A full-stack Task Management application built with **Angular 17** (frontend) and **Java Spring Boot 3** (backend), using an in-memory List as storage — no database required.

---

## Approach

### Architecture Overview

The application follows a clean **client-server architecture**:

```
Browser (Angular SPA)
        │
        │  HTTP (REST API)
        ▼
Spring Boot Backend (port 8080)
        │
        │  In-memory List<Task>
        ▼
  TaskRepository (ArrayList)
```

The Angular frontend communicates with the backend exclusively through a REST API. A **dev-server proxy** (`proxy.conf.json`) forwards all `/api/*` requests from `localhost:4200` to `localhost:8080`, so both environments use the same relative URL `/api`.

---

### Backend — State & Structure

**State** is held entirely in a single `ArrayList<Task>` inside `TaskRepository`. An `AtomicLong` counter acts as the auto-increment ID sequence. The list is pre-seeded with 2 sample tasks on startup. All state is in-memory — restarting the server resets it.

**Layered structure** follows the standard Spring Boot pattern:

```
backend/src/main/java/com/taskmanager/
├── TaskManagerApplication.java   # Spring Boot entry point
├── config/
│   └── CorsConfig.java           # CORS — allows Angular origin
├── controller/
│   └── TaskController.java       # REST endpoints, validation error handler
├── service/
│   └── TaskService.java          # Business logic, name trimming
├── repository/
│   └── TaskRepository.java       # In-memory List<Task>, sample data seeding
└── model/
    ├── Task.java                  # Entity — id, name, description, status, createdDate
    ├── TaskRequest.java           # DTO — validated input (@NotBlank, @Size)
    └── DashboardSummary.java      # Response — totalTasks, pendingTasks, completedTasks
```

**Validation** is enforced at the controller boundary using Bean Validation (`@NotBlank`, `@Size`). A `@ExceptionHandler` converts validation failures into a structured `400` JSON response with per-field error messages.

---

### Frontend — State & Structure

**State** lives in `AppComponent` — the single root component that owns:
- `tasks: Task[]` — the full task list fetched from the backend
- `summary: DashboardSummary` — dashboard counts
- `isLoading: boolean` — loading flag for the task table
- `successMessage / errorMessage` — auto-dismissing toast notifications

Both `tasks` and `summary` are always loaded together using `forkJoin()` so the dashboard and table are always in sync.

**Component tree:**

```
AppComponent                  (state owner, HTTP orchestration)
├── DashboardComponent        (pure display, receives summary via @Input)
├── TaskFormComponent         (reactive form, emits TaskRequest via @Output)
└── TaskListComponent         (receives tasks via @Input, emits actions via @Output)
    ├── Search input          (filters by name + description)
    └── Status filter buttons (ALL / PENDING / COMPLETED)
```

**Data flow** is strictly top-down:
- `AppComponent` fetches data and passes it down via `@Input`
- Child components emit events via `@Output`
- `AppComponent` handles all API calls and refreshes state after every mutation

**Search & Filter** are handled locally inside `TaskListComponent` without any extra HTTP calls. Filtering is memoized — it only recomputes when `tasks`, `searchQuery`, or `statusFilter` actually changes (via `ngOnChanges` + property setters), not on every change-detection cycle.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | Angular 17, TypeScript, Reactive Forms, RxJS |
| Backend | Java 17, Spring Boot 3.2, Spring Web, Bean Validation |
| Storage | In-memory `ArrayList` (no database) |
| Build | Maven (backend), Angular CLI / npm (frontend) |
| Testing | JUnit 5 (backend), Karma + Jasmine (frontend) |
| PWA | Angular Service Worker, Web App Manifest |

---

## Project Structure

```
task-manager-scaffold/
├── backend/                          # Spring Boot REST API
│   ├── src/main/java/com/taskmanager/
│   │   ├── TaskManagerApplication.java
│   │   ├── config/CorsConfig.java
│   │   ├── controller/TaskController.java
│   │   ├── service/TaskService.java
│   │   ├── repository/TaskRepository.java
│   │   └── model/
│   │       ├── Task.java
│   │       ├── TaskRequest.java
│   │       └── DashboardSummary.java
│   ├── src/test/java/com/taskmanager/
│   │   └── TaskServiceTest.java      # 8 unit tests
│   └── pom.xml
│
├── frontend/                         # Angular SPA
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/
│   │   │   │   ├── dashboard/        # Summary cards
│   │   │   │   ├── task-form/        # Create task form
│   │   │   │   └── task-list/        # Table with search & filter
│   │   │   ├── models/task.model.ts  # TypeScript interfaces
│   │   │   ├── services/task.service.ts  # HTTP client
│   │   │   ├── app.component.ts      # Root — state & orchestration
│   │   │   └── app.module.ts
│   │   ├── environments/             # Dev & prod API URLs
│   │   ├── manifest.webmanifest      # PWA manifest
│   │   └── polyfills.ts
│   ├── ngsw-config.json              # Service worker cache config
│   ├── proxy.conf.json               # Dev proxy → localhost:8080
│   └── angular.json
│
├── start-backend.bat                 # One-click backend start (Windows)
├── start-frontend.bat                # One-click frontend start (Windows)
├── .gitignore
└── README.md
```

---

## Running the Project

### Prerequisites
- Java 17+
- Maven 3.6+
- Node.js 18+ and npm

### Step 1 — Start the Backend

**Windows** — double-click `start-backend.bat`, or:

```bash
cd backend
mvn spring-boot:run
```

Backend starts at `http://localhost:8080`

### Step 2 — Start the Frontend

**Windows** — double-click `start-frontend.bat`, or:

```bash
cd frontend
npm install
npm start
```

Frontend starts at `http://localhost:4200`

### Step 3 — Open in Browser

```
http://localhost:4200
```

---

## API Endpoints

| Method | Endpoint | Description | Status Codes |
|---|---|---|---|
| GET | `/api/tasks` | Get all tasks | 200 |
| POST | `/api/tasks` | Create a task | 201, 400 |
| PATCH | `/api/tasks/{id}/status` | Mark task as completed | 200, 400, 404 |
| DELETE | `/api/tasks/{id}` | Delete a task | 204, 404 |
| GET | `/api/tasks/dashboard` | Dashboard summary | 200 |

---

## Features

- **Create Task** — with name (required, max 100 chars) and description (optional, max 500 chars)
- **View All Tasks** — table with task name, description, status badge, and created date
- **Mark as Completed** — one-click status update (only shown for PENDING tasks)
- **Delete Task** — with confirmation prompt
- **Search** — filters tasks by name and description in real time
- **Filter by Status** — ALL / PENDING / COMPLETED toggle buttons
- **Dashboard** — live Total / Pending / Completed counts
- **Validation** — inline error messages on the form (frontend + backend)
- **Responsive UI** — mobile-friendly table collapses to card layout on small screens
- **PWA** — installable, caches app shell and API responses for offline viewing
- **Sample Data** — 2 tasks pre-loaded on every backend start

---

## Running Tests

### Backend (JUnit 5)
```bash
cd backend
mvn test
```
8 tests covering: getAllTasks, createTask (status + trim), updateTaskStatus, deleteTask, getDashboardSummary.

### Frontend (Karma + Jasmine)
```bash
cd frontend
npm test
```
