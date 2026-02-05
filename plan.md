# Task Tracker - План разработки

## 🎯 Milestone 1: Инфраструктура и Auth Service

### 1.1 Docker окружение
- [x] Создать docker-compose.yml
- [x] Настроить PostgreSQL для Auth Service
- [x] Настроить MongoDB для Task Service
- [x] Настроить Kafka + Zookeeper
- [x] Проверить что все контейнеры запускаются (`docker-compose up -d`)

### 1.2 Auth Service - База
- [x] Создать структуру модуля auth-service
- [x] Настроить application.yml (БД, Kafka, JWT)
- [x] Создать Flyway миграцию V1__create_users_table.sql
- [x] Создать Entity User
- [x] Создать UserRepository
- [x] Запустить Auth Service и проверить что миграция применилась

### 1.3 Auth Service - JWT
- [x] Создать JwtService (generateToken, validateToken, extractUserId, extractEmail)
- [x] Настроить jwt.secret и jwt.expiration в application.yml
- [ ] Написать unit тест для JwtService

### 1.4 Auth Service - Security
- [x] Создать SecurityConfig (BCryptPasswordEncoder, AuthenticationManager)
- [x] Создать CustomUserDetailsService
- [x] Создать JwtAuthenticationFilter
- [x] Настроить публичные endpoints (/api/auth/**)

### 1.5 Auth Service - DTO
- [x] Создать RegisterRequest (email, password, confirmPassword с валидацией)
- [x] Создать LoginRequest (email, password с валидацией)
- [x] Создать UserResponse
- [x] Создать ErrorResponse
- [ ] Создать TokenValidationResponse (для Task Service)

### 1.6 Auth Service - Exceptions
- [x] Создать UserAlreadyExistsException
- [x] Создать InvalidCredentialsException
- [x] Создать UserNotFoundException
- [x] Создать GlobalExceptionHandler

### 1.7 Auth Service - Business Logic
- [x] Создать AuthService (signUp, signIn)
- [x] Проверка confirmPassword в signUp
- [x] Проверка уникальности email
- [x] Хэширование пароля через BCrypt
- [ ] Kafka: публикация UserRegisteredEvent после регистрации

### 1.8 Auth Service - Controllers
- [x] POST /api/auth/sign-up (регистрация + JWT в заголовке)
- [x] POST /api/auth/sign-in (логин + JWT в заголовке)
- [ ] GET /api/auth/me (получить текущего пользователя)
- [ ] GET /api/auth/validate-token (для Task Service)

### 1.9 Auth Service - Тестирование
- [x] Postman: регистрация нового пользователя
- [x] Postman: логин существующего пользователя
- [x] Postman: регистрация с существующим email (409 Conflict)
- [x] Postman: логин с неверным паролем (401 Unauthorized)
- [x] Postman: GET /me с JWT токеном
- [x] Проверить что User создан в PostgreSQL

---

## 🎯 Milestone 2: Task Service

### 2.1 Task Service - База
- [ ] Создать структуру модуля task-service
- [ ] Настроить application.yml (MongoDB, Kafka, Auth Service URL)
- [ ] Создать Document Task (MongoDB entity)
- [ ] Создать TaskRepository (MongoRepository)

### 2.2 Task Service - DTO
- [ ] Создать CreateTaskRequest (title, description с валидацией)
- [ ] Создать UpdateTaskRequest
- [ ] Создать TaskResponse
- [ ] Создать ErrorResponse (аналогично Auth Service)

### 2.3 Task Service - Exceptions
- [ ] Создать TaskNotFoundException
- [ ] Создать UnauthorizedException
- [ ] Создать ForbiddenException
- [ ] Создать GlobalExceptionHandler

### 2.4 Task Service - JWT Integration
- [ ] Создать AuthClientService (вызов Auth Service для валидации JWT)
- [ ] Создать JwtAuthenticationFilter (вызывает Auth Service)
- [ ] Настроить SecurityConfig
- [ ] Обработка ошибок при недоступности Auth Service

### 2.5 Task Service - Business Logic
- [ ] Создать TaskService (CRUD операции)
- [ ] createTask(userId, title, description)
- [ ] getUserTasks(userId)
- [ ] getTaskById(taskId, userId) - проверка ownership
- [ ] updateTask(taskId, userId, data) - проверка ownership
- [ ] deleteTask(taskId, userId) - проверка ownership
- [ ] completeTask(taskId, userId)
- [ ] Kafka: публикация TaskCompletedEvent

### 2.6 Task Service - Controllers
- [ ] GET /api/tasks (получить все задачи пользователя)
- [ ] POST /api/tasks (создать задачу)
- [ ] GET /api/tasks/{id} (получить задачу)
- [ ] PUT /api/tasks/{id} (обновить задачу)
- [ ] DELETE /api/tasks/{id} (удалить задачу)
- [ ] PATCH /api/tasks/{id}/complete (пометить выполненной)

### 2.7 Task Service - Тестирование
- [ ] Postman: создать задачу с JWT
- [ ] Postman: получить все задачи пользователя
- [ ] Postman: обновить задачу
- [ ] Postman: удалить задачу
- [ ] Postman: попытка удалить чужую задачу (403 Forbidden)
- [ ] Postman: запрос без JWT (401 Unauthorized)
- [ ] Проверить что задачи сохраняются в MongoDB

---

## 🎯 Milestone 3: API Gateway

### 3.1 API Gateway - Настройка
- [ ] Создать структуру модуля api-gateway
- [ ] Настроить application.yml (порт 8080, routes)
- [ ] Настроить route для Auth Service (/auth/** → 8081)
- [ ] Настроить route для Task Service (/tasks/** → 8082)
- [ ] Настроить CORS (allowedOrigins, allowedMethods)

### 3.2 API Gateway - Тестирование
- [ ] Postman: POST http://localhost:8080/auth/sign-up
- [ ] Postman: POST http://localhost:8080/auth/sign-in
- [ ] Postman: GET http://localhost:8080/tasks
- [ ] Postman: POST http://localhost:8080/tasks
- [ ] Проверить что Gateway корректно роутит запросы

---

## 🎯 Milestone 4: Email Sender Service

### 4.1 Email Sender - Настройка
- [ ] Создать структуру модуля email-sender
- [ ] Настроить application.yml (Kafka, Mailjet SMTP)
- [ ] Зарегистрировать аккаунт на Mailjet
- [ ] Получить API Key и Secret Key
- [ ] Настроить Spring Mail с Mailjet

### 4.2 Email Sender - Events
- [ ] Создать UserRegisteredEvent (userId, email)
- [ ] Создать DailyReportEvent (email, completedCount, pendingCount)

### 4.3 Email Sender - Kafka Listeners
- [ ] Создать UserEventListener
- [ ] @KafkaListener для топика "user-events"
- [ ] Обработка UserRegisteredEvent

### 4.4 Email Sender - Email Service
- [ ] Создать EmailService
- [ ] sendWelcomeEmail(email) - HTML шаблон
- [ ] sendDailyReportEmail(email, report) - HTML шаблон
- [ ] Обработка ошибок отправки

### 4.5 Email Sender - Тестирование
- [ ] Зарегистрировать нового пользователя через Auth Service
- [ ] Проверить что welcome email пришёл на почту
- [ ] Проверить логи Email Sender (Kafka событие получено)

---

## 🎯 Milestone 5: Scheduler Service

### 5.1 Scheduler - Настройка
- [ ] Создать структуру модуля scheduler
- [ ] Настроить application.yml (PostgreSQL, MongoDB, Kafka)
- [ ] Подключение к auth_db (PostgreSQL)
- [ ] Подключение к task_db (MongoDB)

### 5.2 Scheduler - Entities
- [ ] Создать User entity (для чтения из PostgreSQL)
- [ ] Создать Task document (для чтения из MongoDB)
- [ ] Создать UserRepository (JPA)
- [ ] Создать TaskRepository (MongoRepository)

### 5.3 Scheduler - Daily Report
- [ ] Создать DailyReportScheduler
- [ ] @Scheduled(cron = "0 0 0 * * *") - каждую полночь
- [ ] Получить список всех пользователей
- [ ] Для каждого пользователя найти задачи (completed за 24ч, pending)
- [ ] Публикация DailyReportEvent в Kafka

### 5.4 Scheduler - Тестирование
- [ ] Изменить cron на каждую минуту для тестирования
- [ ] Создать задачи и пометить несколько как выполненные
- [ ] Запустить Scheduler
- [ ] Проверить что события публикуются в Kafka
- [ ] Проверить что email с отчётом приходит

---

## 🎯 Milestone 6: Frontend (HTML + JavaScript)

### 6.1 Frontend - Структура
- [ ] Создать директорию frontend/
- [ ] index.html (landing page)
- [ ] login.html (страница логина)
- [ ] register.html (страница регистрации)
- [ ] dashboard.html (список задач)
- [ ] Создать styles.css
- [ ] Подключить Bootstrap 5

### 6.2 Frontend - Регистрация
- [ ] Форма регистрации (email, password, confirmPassword)
- [ ] Валидация на клиенте
- [ ] Отправка POST /auth/sign-up
- [ ] Сохранение JWT в localStorage
- [ ] Редирект на dashboard.html

### 6.3 Frontend - Логин
- [ ] Форма логина (email, password)
- [ ] Отправка POST /auth/sign-in
- [ ] Сохранение JWT в localStorage
- [ ] Редирект на dashboard.html

### 6.4 Frontend - Dashboard
- [ ] Проверка JWT при загрузке (GET /auth/me)
- [ ] Если нет JWT → редирект на login.html
- [ ] Загрузка задач (GET /tasks с Authorization header)
- [ ] Отображение списка задач
- [ ] Форма создания новой задачи
- [ ] Кнопка "Mark as completed"
- [ ] Кнопка "Delete"
- [ ] Кнопка "Logout" (удаление JWT из localStorage)

### 6.5 Frontend - Тестирование
- [ ] Регистрация нового пользователя через UI
- [ ] Логин через UI
- [ ] Создание задачи через UI
- [ ] Отметка задачи как выполненной
- [ ] Удаление задачи
- [ ] Logout и проверка что редиректит на login

---

## 🎯 Milestone 7: Docker Compose для сервисов

### 7.1 Dockerfile для каждого сервиса
- [ ] Создать Dockerfile для auth-service
- [ ] Создать Dockerfile для task-service
- [ ] Создать Dockerfile для email-sender
- [ ] Создать Dockerfile для scheduler
- [ ] Создать Dockerfile для api-gateway
- [ ] Создать Dockerfile для frontend (Nginx)

### 7.2 Docker Compose - полная конфигурация
- [ ] Обновить docker-compose.yml (добавить все сервисы)
- [ ] Настроить depends_on между сервисами
- [ ] Настроить networks
- [ ] Настроить environment variables
- [ ] Создать .env.example файл

### 7.3 Тестирование в Docker
- [ ] docker-compose up --build
- [ ] Проверить что все контейнеры запустились
- [ ] Проверить логи каждого сервиса
- [ ] Полный E2E тест через Frontend
- [ ] Проверить что email приходит

---

## 🎯 Milestone 8: Дополнительные фичи (опционально)

### 8.1 Расширенные возможности Task
- [ ] Добавить поле priority (low, medium, high)
- [ ] Добавить поле dueDate
- [ ] Добавить массив tags
- [ ] Добавить subtasks (вложенные задачи)
- [ ] Добавить comments к задачам

### 8.2 Outbox Pattern (продвинутое)
- [ ] Flyway миграция V2__create_outbox_table.sql
- [ ] Создать OutboxEvent entity
- [ ] Создать OutboxEventRepository
- [ ] Создать OutboxService
- [ ] Создать OutboxPublisher (@Scheduled)
- [ ] Обновить UserService для сохранения в outbox
- [ ] Тестирование с отключенным Kafka

### 8.3 Мониторинг и логирование
- [ ] Добавить Spring Boot Actuator
- [ ] Настроить health endpoints
- [ ] Добавить Prometheus metrics
- [ ] Настроить structured logging (JSON logs)
- [ ] Добавить correlation ID для трейсинга запросов

### 8.4 Тестирование
- [ ] Unit тесты для AuthService
- [ ] Unit тесты для TaskService
- [ ] Integration тесты для Auth API
- [ ] Integration тесты для Task API
- [ ] Testcontainers для PostgreSQL
- [ ] Testcontainers для MongoDB
- [ ] Testcontainers для Kafka

### 8.5 CI/CD
- [ ] Создать GitHub Actions workflow
- [ ] Build и тестирование при push
- [ ] Docker image build и push в registry
- [ ] Автоматический deploy (опционально)

---

## 🎯 Milestone 9: Документация и завершение

### 9.1 README.md
- [ ] Описание проекта
- [ ] Архитектурная диаграмма
- [ ] Технологический стек
- [ ] Инструкции по запуску (локально)
- [ ] Инструкции по запуску (Docker)
- [ ] API документация (endpoints)
- [ ] Примеры curl запросов

### 9.2 Диаграммы
- [ ] Архитектурная диаграмма (все сервисы)
- [ ] Диаграмма последовательности (User registration flow)
- [ ] Диаграмма последовательности (Task creation flow)
- [ ] Database schema (PostgreSQL)
- [ ] Database schema (MongoDB)

### 9.3 Презентация проекта
- [ ] Подготовить демо
- [ ] Записать видео демонстрацию (опционально)
- [ ] Подготовить слайды с архитектурой
- [ ] Описать challenges и решения

---

## 🎓 Что вы изучите

- ✅ Микросервисная архитектура
- ✅ Spring Boot 3.x
- ✅ Spring Security + JWT
- ✅ PostgreSQL + Flyway migrations
- ✅ MongoDB (polyglot persistence)
- ✅ Apache Kafka (event-driven architecture)
- ✅ Docker & Docker Compose
- ✅ API Gateway pattern
- ✅ Distributed systems patterns (Outbox, Circuit Breaker)
- ✅ RESTful API design
- ✅ Exception handling в микросервисах
- ✅ Асинхронная обработка
- ✅ Scheduled tasks

---
