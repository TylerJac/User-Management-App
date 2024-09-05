# User Management Application

This is a Spring Boot-based User Management Application with role-based access control, user registration, and CRUD functionalities. It supports user registration, login, role-based access control (ADMIN and USER roles), and global exception handling.

## Features

- User registration with role assignment
- Login with Spring Security
- Role-based access control for `ADMIN` and `USER`
- CRUD operations for users (accessible by admins)
- Password encryption with BCrypt
- Global exception handling
- Custom field validation for username and password

## Prerequisites

Before running the application, ensure you have the following installed:

- Java 17 or higher
- Maven 3.6 or higher
- A supported database (default: H2, an in-memory database) or any relational database (MySQL, PostgreSQL, etc.)

## Setting Up the Application

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/user-management-app.git
cd user-management-app
```

### 2. Setting up database
Since it's an h2 database and it's stored in memory it shouldn't need any config but I haven't h2 before so just in case here's the sql for setting up the tables:
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BOOLEAN DEFAULT TRUE
);
CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);


### 3. How to Run

run this file: UserManagementAppApplication.java
then head to: http://localhost:8080/login
