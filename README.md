
📝 To-Do Backend API

A Spring Boot backend application for managing To-Do tasks with JWT authentication, role-based access control (Admin & User), and PostgreSQL as the database.

🚀 Features

🔐 JWT Authentication & Authorization

👤 Role-based access:

Admin: Can manage all users & tasks.

User: Can manage only their own tasks.

📌 CRUD operations on To-Do tasks (Create, Read, Update, Delete)

🗄 PostgreSQL database integration

⚡ RESTful API with JSON responses

🛡 Secure endpoints with Spring Security

🛠 Tech Stack

Spring Boot 3 (REST API, Security, JPA)

PostgreSQL (Database)

JWT (Authentication & Authorization)

Spring Data JPA (ORM)

Maven (Build tool)
📂 Project Structure

src/main/java/com/example/todo
│── config/          # Security & JWT configuration
│── controller/      # REST Controllers
│── dto/             # Data Transfer Objects
│── entity/          # JPA Entities (User, Role, Task)
│── exception/       # Custom exception handling
│── repository/      # Spring Data JPA Repositories
│── security/        # JWT Utils, Filters, Services
│── service/         # Business logic
│── TodoApplication  # Main class

⚙️ Setup & Installation

1️⃣ Clone the repository

git clone https://github.com/your-username/todo-backend.git
cd todo-backend

2️⃣ Configure PostgreSQL

Create a database in PostgreSQL:

CREATE DATABASE todo_db;

Update application.properties or application.yml:

spring.datasource.url=jdbc:postgresql://localhost:5432/todo_db
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# JWT Secret
jwt.secret=your_jwt_secret_key
jwt.expiration=3600000

3️⃣ Run the application

mvn spring-boot:run

🔑 Authentication & Roles

Default Roles

ADMIN – Full access to all APIs (manage users & tasks).

USER – Can manage only their own tasks.


API Authentication

Register or login to receive a JWT token.

Pass the token in the Authorization header:


Authorization: Bearer <your_token>


---

📌 API Endpoints

Auth

Method	Endpoint	Description	Role

POST	/api/auth/register	Register new user	Public
POST	/api/auth/login	Login & get token	Public


Users (Admin only)

Method	Endpoint	Description	Role

GET	/api/users	Get all users	ADMIN
GET	/api/users/{id}	Get user by ID	ADMIN
DELETE	/api/users/{id}	Delete user	ADMIN


Tasks

Method	Endpoint	Description	Role

GET	/api/tasks	Get all tasks (Admin: all users, User: own)	ADMIN / USER
POST	/api/tasks	Create a new task	USER
PUT	/api/tasks/{id}	Update a task (own only)	USER
DELETE	/api/tasks/{id}	Delete a task (own only)	USER



---

🧪 Testing with Postman

1. Register a new user → /api/auth/register


2. Login → /api/auth/login (copy JWT token)


3. Use JWT in Authorization: Bearer <token> header for further requests.




---

👨‍💻 Future Improvements

✅ Refresh Token implementation

✅ Pagination & Filtering for tasks

✅ Email verification & password reset

✅ Docker support

