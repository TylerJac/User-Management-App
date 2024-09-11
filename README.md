# User Management Application

This is a Spring Boot-based User Management Application with role-based access control, user registration, and CRUD functionalities. It supports user registration, login, role-based access control (ADMIN and USER roles), and global exception handling.


## Prerequisites

Before running the application, ensure you have the following installed:

- Java 17 or higher
- Maven 3.6 or higher

## Setting Up the Application

###  How to Run

- run this file: UserManagementAppApplication.java
- Then use postman to access this url: https://localhost:8443/register
- to register you'll need to add params in the url then post, ex: https://localhost:8443/register?roleName=ADMIN
- only roles that you can use are USER and ADMIN
- Then head to https://localhost:8443/login
- with the username and password you should get a token after posting
- then you can test out each end point https://localhost:8443/users/all (ADMIN, GET)
- https://localhost:8443/user/details (USER and ADMIN, GET)
- https://localhost:8443/user/update (USER and ADMIN, POST)
- https://localhost:8443/users/all/{id} (ADMIN for editing any account, POST)
- https://localhost:8443/users/delete/{id} (ADMIN, POST)

## Getting the CSRF Token and Manually Adding It to Headers in Postman
### Login to Get the CSRF Token:

- Step 1: Make a GET request to any protected resource ex: https://localhost:8443/login
- Step 2: After making the request, check the Cookies tab in the Postman response. You should see a cookie called XSRF-TOKEN.
  ### Manually Add the CSRF Token to Headers:

- Step 1: Copy the XSRF-TOKEN from the Cookies section.
- Step 2: In your next request (e.g., POST https://localhost:8443/user/update), go to the Headers section and add the following:
- Key: X-XSRF-TOKEN
- Value: The value of the XSRF-TOKEN cookie you copied from the previous step.