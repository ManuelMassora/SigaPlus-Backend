# SigaPlus Backend

> **Integrated Academic Management System (SIGA+)** — A Java/Spring Boot backend for managing academic operations including students, enrollments, courses, grades, and institutional workflows.

---

## Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Running with Maven](#running-with-maven)
  - [Running with the Maven Wrapper](#running-with-the-maven-wrapper)
- [Environment Variables](#environment-variables)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)
- [Roadmap](#roadmap)
- [Author](#author)

---

## Overview

**SigaPlus** is a backend system designed to power academic management platforms for schools and universities. It handles the full lifecycle of academic operations — from student registration and enrollment to course management, grade tracking, and reporting.

Built with **Java** and **Spring Boot** following clean, layered architecture principles (Controller → Service → Repository), SigaPlus is designed to be robust, maintainable, and ready for institutional-scale deployment.

Core responsibilities:
- Student and staff profile management
- Course and curriculum management
- Enrollment and academic year lifecycle
- Grade recording and transcript generation
- Role-based access control (students, teachers, administrators)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17+ |
| Framework | Spring Boot |
| Build Tool | Maven (via Maven Wrapper) |
| ORM | Spring Data JPA / Hibernate |
| Database | PostgreSQL / MySQL |
| Security | Spring Security + JWT |
| Architecture | Layered (Controller → Service → Repository) |
| Testing | JUnit 5 + Mockito |

---

## Project Structure

```
SigaPlus-Backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/sigaplus/      # Application source code
│   │   │       ├── controller/    # REST controllers (API endpoints)
│   │   │       ├── service/       # Business logic layer
│   │   │       ├── repository/    # Data access layer (JPA repositories)
│   │   │       ├── model/         # Domain entities
│   │   │       ├── dto/           # Data Transfer Objects
│   │   │       ├── security/      # JWT, authentication filters
│   │   │       └── config/        # Application configuration beans
│   │   └── resources/
│   │       ├── application.properties   # Main configuration
│   │       └── application-dev.properties
│   └── test/
│       └── java/                  # Unit and integration tests
├── .mvn/wrapper/                  # Maven wrapper configuration
├── pom.xml                        # Maven dependencies and build config
├── mvnw                           # Maven wrapper script (Unix)
├── mvnw.cmd                       # Maven wrapper script (Windows)
└── .gitignore
```

---

## Getting Started

### Prerequisites

- [Java 17+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/) *(optional — the project includes the Maven Wrapper)*
- A running instance of PostgreSQL or MySQL
- [Git](https://git-scm.com/)

### Running with Maven

```bash
# Clone the repository
git clone https://github.com/ManuelMassora/SigaPlus-Backend.git
cd SigaPlus-Backend

# Configure your environment (see section below)
cp src/main/resources/application.properties src/main/resources/application-local.properties
# Edit application-local.properties with your DB credentials

# Build and run
mvn spring-boot:run
```

### Running with the Maven Wrapper

No need to have Maven installed globally — use the bundled wrapper:

```bash
# Unix / macOS / Linux
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The API will be available at `http://localhost:8080`.

To build a production JAR:

```bash
./mvnw clean package -DskipTests
java -jar target/sigaplus-*.jar
```

---

## Environment Variables

Configure your database and security settings in `src/main/resources/application.properties`:

```properties
# Server
server.port=8080
spring.profiles.active=dev

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/sigaplus
spring.datasource.username=your_db_user
spring.datasource.password=your_db_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000

# JPA
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

> Never commit real credentials. Use environment variables or a secrets manager in production.

---

## API Endpoints

> Full API documentation coming soon (Swagger/OpenAPI). Below is a high-level overview of the planned resource structure.

### Authentication
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Register a new user |
| `POST` | `/api/v1/auth/login` | Login and receive JWT token |

### Students
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/students` | List all students |
| `POST` | `/api/v1/students` | Register a new student |
| `GET` | `/api/v1/students/{id}` | Get student details |
| `PUT` | `/api/v1/students/{id}` | Update student information |
| `DELETE` | `/api/v1/students/{id}` | Remove a student |

### Courses
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/courses` | List all courses |
| `POST` | `/api/v1/courses` | Create a new course |
| `GET` | `/api/v1/courses/{id}` | Get course details |

### Enrollments
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/enrollments` | Enroll a student in a course |
| `GET` | `/api/v1/enrollments/{studentId}` | Get enrollments for a student |

### Grades
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/grades` | Record a grade |
| `GET` | `/api/v1/grades/{studentId}` | Get grades for a student |

---

## Running Tests

```bash
# Run all tests
./mvnw test

# Run with detailed output
./mvnw test -Dsurefire.useFile=false

# Run a specific test class
./mvnw test -Dtest=StudentServiceTest
```

---

## Roadmap

- [ ] Complete authentication and role-based access control (ADMIN, TEACHER, STUDENT)
- [ ] Full student lifecycle management
- [ ] Course and curriculum management
- [ ] Grade recording and GPA calculation
- [ ] Academic year and semester management
- [ ] Transcript and report generation
- [ ] Swagger/OpenAPI documentation
- [ ] Docker containerization
- [ ] CI/CD pipeline setup

---

## Author

**Manuel Massora** — Backend Engineer  
Maputo, Mozambique

- GitHub: [@ManuelMassora](https://github.com/ManuelMassora)
- LinkedIn: [manuelt-massora-5bb417375](https://linkedin.com/in/manuelt-massora-5bb417375/)
- Email: manuelmassora75@gmail.com

---

> Built with Java · Spring Boot · Maven
