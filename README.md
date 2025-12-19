# 📝 Todo Application Backend

A secure, scalable Todo Management REST API built with Spring Boot, designed to support a modern React frontend. The backend handles authentication, task management, user profiles, and image uploads with a clean layered architecture.

---

## 📖 Table of Contents
- Features
- Tech Stack
- Project Structure
- Application Flow
- Setup & Installation
- API Endpoints
    - Authentication Controller
    - Todo Controller
    - Profile Controller
- Database Schema
- Security & JWT
- Image Upload (Cloudinary)
- Error Handling
- Contributing
- License

---

## 🚀 Features

### User Authentication
- Register & login using JWT-based authentication
- Secure protected APIs using Spring Security

### Task Management
- Create, update, delete todos
- Mark tasks as completed or incomplete
- Filter tasks by category, priority, and status

### User Profile
- View and update user profile
- Upload & delete profile avatar image
- Persistent profile data across sessions

### Secure Architecture
- Stateless authentication using JWT
- Centralized exception handling
- Layered architecture for maintainability

---

## 🛠 Tech Stack

### Backend
- Spring Boot 3
- Java 17+
- Spring Security
- JWT (JSON Web Tokens)
- Spring Data JPA
- Hibernate
- PostgreSQL
- Cloudinary (Image Storage)

### Tools & Libraries
- Lombok
- Maven
- SLF4J + Logback
- Validation API

---

# 📁 Project Structure – Todo Application Backend

Below is the complete backend project structure with **one-line explanations** for each package and class.

```text
com.example.todoapp                          # Root package of the Todo backend application
├── config
│   ├── ApplicationConfig                   # Central application-level bean configurations
│   ├── CloudinaryConfig                    # Cloudinary setup for profile image uploads
│   ├── CorsConfig                          # CORS configuration for frontend-backend communication
│   └── SecurityConfig                      # Spring Security configuration with JWT
│
├── controller
│   ├── AuthController                      # Handles user registration and login APIs
│   ├── ProfileController                   # Manages user profile and avatar endpoints
│   └── TodoController                      # Exposes CRUD APIs for todo management
│
├── dto
│   ├── AuthResponse                        # DTO for authentication response with JWT
│   ├── LoginRequest                        # DTO for login request payload
│   ├── RegisterRequest                     # DTO for user registration data
│   ├── TodoRequest                         # DTO for creating or updating todos
│   ├── TodoResponse                        # DTO for sending todo details to frontend
│   ├── ProfileResponse                     # DTO for returning user profile information
│   └── ProfileUpdateRequest                # DTO for updating user profile details
│
├── exception
│   ├── GlobalExceptionHandler               # Centralized exception handling across APIs
│   └── UserAlreadyExistsException           # Custom exception for duplicate user registration
│
├── model
│   ├── User                                # Entity representing application users
│   ├── UserProfile                         # Entity storing user profile and avatar details
│   └── Todo                                # Entity representing todo tasks
│
├── repository
│   ├── UserRepository                      # JPA repository for User entity
│   ├── UserProfileRepository               # JPA repository for UserProfile entity
│   └── TodoRepository                      # JPA repository for Todo entity
│
├── security
│   ├── JwtAuthenticationFilter             # Validates JWT token on each request
│   ├── JwtService                          # Generates and validates JWT tokens
│   └── CustomUserDetailsService            # Loads user details for Spring Security
│
├── service
│   ├── AuthService                         # Business logic for authentication operations
│   ├── UserService                         # Handles user-related business logic
│   ├── TodoService                         # Core business logic for todo operations
│   ├── ProfileService                      # Business logic for profile management
│   └── CloudinaryService                   # Handles image upload/delete with Cloudinary
│
├── util
│   └── ApiResponse                         # Standardized API response wrapper
│
└── TodoappApplication                      # Main Spring Boot application entry point
```

---

✅ This structure follows **clean layered architecture**  
✅ Improves **maintainability, scalability, and testability**  
✅ Ideal for **enterprise-grade Spring Boot applications**

---

## 🔄 Application Flow
1. User registers or logs in
2. JWT token is generated and returned
3. Frontend sends token in Authorization header
4. JwtAuthenticationFilter validates token
5. Authenticated user accesses protected APIs
6. Todos & profile data stored in PostgreSQL
7. Profile images stored in Cloudinary

---

## ⚙️ Setup & Installation

### Prerequisites
- Java 17+
- Maven
- PostgreSQL
- Cloudinary Account

### 1️⃣ Clone the Repository
```bash
git clone https://github.com/your-username/todoapp-backend.git
cd todoapp-backend
```

### 2️⃣ Configure Database
```sql
CREATE DATABASE todoapp;
```

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todoapp
spring.datasource.username=postgres
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 3️⃣ Configure Cloudinary
```properties
cloudinary.cloud-name=your_cloud_name
cloudinary.api-key=your_api_key
cloudinary.api-secret=your_api_secret
```

### 4️⃣ Run the Application
```bash
mvn clean install
mvn spring-boot:run
```

Backend runs at:
```
http://localhost:8091
```

---

## 🛠 API Endpoints

### 🔐 Authentication Controller
| Method | Endpoint | Description | Auth |
|------|---------|-------------|------|
| POST | /auth/register | Register new user | ❌ |
| POST | /auth/login | Login & get JWT | ❌ |

### 📝 Todo Controller
| Method | Endpoint | Description | Auth |
|------|---------|-------------|------|
| POST | /todos | Create todo | ✅ |
| GET | /todos | Get all todos | ✅ |
| GET | /todos/{id} | Get todo by ID | ✅ |
| PUT | /todos/{id} | Update todo | ✅ |
| DELETE | /todos/{id} | Delete todo | ✅ |
| PATCH | /todos/{id}/complete | Mark completed | ✅ |
| PATCH | /todos/{id}/incomplete | Mark incomplete | ✅ |
| GET | /todos/completed | Completed todos | ✅ |
| GET | /todos/incomplete | Incomplete todos | ✅ |
| GET | /todos/category/{category} | Filter by category | ✅ |
| GET | /todos/priority/{priority} | Filter by priority | ✅ |

### 👤 Profile Controller
| Method | Endpoint | Description | Auth |
|------|---------|-------------|------|
| GET | /profile | Get user profile | ✅ |
| PUT | /profile | Update full name | ✅ |
| POST | /profile/image | Upload profile image | ✅ |
| DELETE | /profile/image | Delete profile image | ✅ |

---

## 🗄 Database Schema

### Users Table
| Column | Type |
|------|------|
| id | BIGINT |
| full_name | VARCHAR |
| email | VARCHAR |
| password | VARCHAR |

### Todos Table
| Column | Type |
|------|------|
| id | BIGINT |
| title | VARCHAR |
| description | TEXT |
| completed | BOOLEAN |
| due_date | DATE |
| due_time | TIME |
| priority | VARCHAR |
| category | VARCHAR |
| user_id | BIGINT |

### User Profile Table
| Column | Type |
|------|------|
| id | BIGINT |
| avatar_url | VARCHAR |
| user_id | BIGINT |

---

## 🔒 Security & JWT
- JWT generated on login
- Token sent via `Authorization: Bearer <token>`
- Stateless authentication
- Custom JWT filter validates token on every request
- Unauthorized requests blocked automatically

---

## ☁️ Image Upload (Cloudinary)
- Images uploaded using multipart/form-data
- Stored securely in Cloudinary
- Only image URL stored in database
- Optimized for fast frontend rendering

---

## ⚠️ Error Handling
- GlobalExceptionHandler handles all exceptions
- Custom exceptions for user-related errors
- Consistent API response format using ApiResponse

---

## 🤝 Contributing
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push and open a Pull Request

---

## 📜 License
This project is licensed under the MIT License.
