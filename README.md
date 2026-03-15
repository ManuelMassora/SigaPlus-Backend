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



###################################################################



# SigaPlus Backend

> **Sistema Integrado de Gestão Académica (SIGA+)** — Backend em Java/Spring Boot para gerir operações académicas incluindo estudantes, matrículas, cursos, notas e fluxos institucionais.

---

## Índice

- [Visão Geral](#visão-geral)
- [Stack Tecnológica](#stack-tecnológica)
- [Estrutura do Projecto](#estrutura-do-projecto)
- [Como Começar](#como-começar)
  - [Pré-requisitos](#pré-requisitos)
  - [Executar com Maven](#executar-com-maven)
  - [Executar com o Maven Wrapper](#executar-com-o-maven-wrapper)
- [Variáveis de Ambiente](#variáveis-de-ambiente)
- [Endpoints da API](#endpoints-da-api)
- [Executar Testes](#executar-testes)
- [Roadmap](#roadmap)
- [Autor](#autor)

---

## Visão Geral

**SigaPlus** é um sistema backend desenhado para alimentar plataformas de gestão académica em escolas e universidades. Gere o ciclo de vida completo das operações académicas — desde o registo de estudantes e matrículas até à gestão de cursos, lançamento de notas e geração de relatórios.

Construído com **Java** e **Spring Boot** seguindo uma arquitectura em camadas limpa (Controller → Service → Repository), o SigaPlus foi desenhado para ser robusto, fácil de manter e pronto para deployment em escala institucional.

Responsabilidades principais:
- Gestão de perfis de estudantes e funcionários
- Gestão de cursos e currículos
- Ciclo de vida de matrículas e anos lectivos
- Lançamento e consulta de notas
- Controlo de acesso baseado em funções (estudantes, docentes, administradores)

---

## Stack Tecnológica

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17+ |
| Framework | Spring Boot |
| Build Tool | Maven (via Maven Wrapper) |
| ORM | Spring Data JPA / Hibernate |
| Base de Dados | PostgreSQL / MySQL |
| Segurança | Spring Security + JWT |
| Arquitectura | Camadas (Controller → Service → Repository) |
| Testes | JUnit 5 + Mockito |

---

## Estrutura do Projecto

```
SigaPlus-Backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/sigaplus/         # Código-fonte da aplicação
│   │   │       ├── controller/       # Controllers REST (endpoints da API)
│   │   │       ├── service/          # Camada de lógica de negócio
│   │   │       ├── repository/       # Camada de acesso a dados (JPA)
│   │   │       ├── model/            # Entidades de domínio
│   │   │       ├── dto/              # Data Transfer Objects
│   │   │       ├── security/         # JWT, filtros de autenticação
│   │   │       └── config/           # Beans de configuração
│   │   └── resources/
│   │       ├── application.properties        # Configuração principal
│   │       └── application-dev.properties    # Configuração de desenvolvimento
│   └── test/
│       └── java/                     # Testes unitários e de integração
├── .mvn/wrapper/                     # Configuração do Maven Wrapper
├── pom.xml                           # Dependências e configuração de build Maven
├── mvnw                              # Script Maven Wrapper (Unix)
├── mvnw.cmd                          # Script Maven Wrapper (Windows)
└── .gitignore
```

---

## Como Começar

### Pré-requisitos

- [Java 17+](https://adoptium.net/)
- [Maven 3.8+](https://maven.apache.org/) *(opcional — o projecto inclui o Maven Wrapper)*
- Uma instância a correr de PostgreSQL ou MySQL
- [Git](https://git-scm.com/)

### Executar com Maven

```bash
# Clonar o repositório
git clone https://github.com/ManuelMassora/SigaPlus-Backend.git
cd SigaPlus-Backend

# Configurar o ambiente (ver secção abaixo)
cp src/main/resources/application.properties src/main/resources/application-local.properties
# Editar application-local.properties com as credenciais da base de dados

# Compilar e executar
mvn spring-boot:run
```

### Executar com o Maven Wrapper

Não é necessário ter o Maven instalado globalmente — utiliza o wrapper incluído:

```bash
# Unix / macOS / Linux
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

A API ficará disponível em `http://localhost:8080`.

Para gerar um JAR de produção:

```bash
./mvnw clean package -DskipTests
java -jar target/sigaplus-*.jar
```

---

## Variáveis de Ambiente

Configura a base de dados e as definições de segurança em `src/main/resources/application.properties`:

```properties
# Servidor
server.port=8080
spring.profiles.active=dev

# Base de Dados
spring.datasource.url=jdbc:postgresql://localhost:5432/sigaplus
spring.datasource.username=o_teu_utilizador
spring.datasource.password=a_tua_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false

# JWT
jwt.secret=a_tua_chave_secreta_jwt
jwt.expiration=86400000

# JPA
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

> Nunca faças commit de credenciais reais. Usa variáveis de ambiente ou um gestor de segredos em produção.

---

## Endpoints da API

> Documentação completa da API em breve (Swagger/OpenAPI). Abaixo está uma visão geral de alto nível da estrutura de recursos planeada.

### Autenticação
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/auth/register` | Registar um novo utilizador |
| `POST` | `/api/v1/auth/login` | Autenticar e receber token JWT |

### Estudantes
| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/v1/students` | Listar todos os estudantes |
| `POST` | `/api/v1/students` | Registar um novo estudante |
| `GET` | `/api/v1/students/{id}` | Obter detalhes de um estudante |
| `PUT` | `/api/v1/students/{id}` | Actualizar informações do estudante |
| `DELETE` | `/api/v1/students/{id}` | Remover um estudante |

### Cursos
| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/v1/courses` | Listar todos os cursos |
| `POST` | `/api/v1/courses` | Criar um novo curso |
| `GET` | `/api/v1/courses/{id}` | Obter detalhes de um curso |

### Matrículas
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/enrollments` | Matricular um estudante num curso |
| `GET` | `/api/v1/enrollments/{studentId}` | Obter matrículas de um estudante |

### Notas
| Método | Endpoint | Descrição |
|---|---|---|
| `POST` | `/api/v1/grades` | Lançar uma nota |
| `GET` | `/api/v1/grades/{studentId}` | Consultar notas de um estudante |

---

## Executar Testes

```bash
# Executar todos os testes
./mvnw test

# Executar com saída detalhada
./mvnw test -Dsurefire.useFile=false

# Executar uma classe de teste específica
./mvnw test -Dtest=StudentServiceTest
```

---

## Roadmap

- [ ] Autenticação completa e controlo de acesso por função (ADMIN, DOCENTE, ESTUDANTE)
- [ ] Gestão completa do ciclo de vida do estudante
- [ ] Gestão de cursos e currículos
- [ ] Lançamento de notas e cálculo de média
- [ ] Gestão de anos lectivos e semestres
- [ ] Geração de pautas e declarações académicas
- [ ] Documentação Swagger/OpenAPI
- [ ] Contenorização com Docker
- [ ] Configuração de pipeline CI/CD

---

## Autor

**Manuel Massora** — Backend Engineer  
Maputo, Moçambique

- GitHub: [@ManuelMassora](https://github.com/ManuelMassora)
- LinkedIn: [manuelt-massora-5bb417375](https://linkedin.com/in/manuelt-massora-5bb417375/)
- Email: manuelmassora75@gmail.com

---

> Construído com Java · Spring Boot · Maven
