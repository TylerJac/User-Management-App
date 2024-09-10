# User Management Application

This is a Spring Boot-based User Management Application with role-based access control, user registration, and CRUD functionalities. It supports user registration, login, role-based access control (ADMIN and USER roles), and global exception handling.


## Prerequisites

Before running the application, ensure you have the following installed:

- Java 17 or higher
- Maven 3.6 or higher

## Setting Up the Application

###  How to Run

- run this file: UserManagementAppApplication.java
- Then use postman to access this url: http://localhost:8080/register
- to register you'll need to add params in the url then post, ex: http://localhost:8080/register?roleName=ADMIN
- only roles that you can use are USER and ADMIN
- Then head to http://localhost:8080/login
- with the username and password you should get a token after posting
- then you can test out each end point http://localhost:8080/users/all (ADMIN, GET)
- http://localhost:8080/user/details (USER and ADMIN, GET)
- http://localhost:8080/user/update (USER and ADMIN, POST)
- http://localhost:8080/users/all/{id} (ADMIN for editing any account, POST)
- http://localhost:8080/users/delete/{id} (ADMIN, POST)
