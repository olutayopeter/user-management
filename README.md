# User Management

This is a User Management REST API built using Java and Spring Boot. The API provides functionality for  Clients to 
1. register new users by providing a username, email address, and password. 
2. validate the input data.
3. authenticate user from the username and password provided. 
The API uses JSON Web Tokens (JWT) for authentication and authorization.



## Authentication

The API is built using a microservice architecture, with the following services:


The following technologies were used to build this API:

- Spring security
- JPA / Hibernate
- Springdocv2
- Java version 19
- Spring Boot version 3.7.7
- MySQL database

## Setup

1. Unzip the user-management zip file and copy the folder into any Java IDE but preferably Intellij
2. Then start your IDE and open the project from your IDE
3. Update the database connection from the application.properties i.e change the database username,password,url and port to what you have on your system
4. Start the service from the main class called UserManagementApplication

## Authentication and Authorization

The API uses JSON Web Tokens (JWT) for authentication and authorization. When a user is authenticated after he/she has registered, they receive a JWT that must be included in the header of all subsequent requests. The JWT is verified on the server side to ensure that the user is authenticated and authorized to perform the requested action.

## Swagger Documentation
http://localhost:8080/swagger-ui.html

## Postman collection

I have included postman collection of the test conducted
link: https://documenter.getpostman.com/view/3227660/2s9YC2ytCW#c65f292e-2a75-4821-bfc4-ab8e4bffb0c8
