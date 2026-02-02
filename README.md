auth-service:
    /api/auth/sign-in
    /api/auth/sign-up -> email notification
    /api/auth/sign-out -> clear cookies and add token to redis blacklist
    /api/user/me
    Entities: 
        User: id, email, password
task-service:
    /api/tasks
    /api/task POST
    /api/task DELETE
    /api/task UPDATE
    Entities:
        Task: id, header, description, color, created_at, updated_at, completed_at;
        save in MongoDB
    Email notification every 1h



