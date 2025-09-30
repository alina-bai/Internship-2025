# Internship Project

This repository contains my internship projects, structured by weeks.  
Each week builds upon the previous one, gradually forming a complete Spring Boot application.

### **#Week 1:**
Environment Setup & Project Initialization
This week focuses on establishing the core development environment and generating the foundational Spring Boot project that will be used throughout the entire development process. 
The primary goal is to have a fully functional, minimal Spring Boot application running locally, ready for subsequent development phases.
- Created a basic Spring Boot project using Spring Initializr.(https://start.spring.io/)
- Implemented a simple REST controller with a "Hello World" endpoint.
- To open in browser:  http://localhost:8080/hello

### **#Week 2:** Version Control, Project Structure, and Initial Database Integration
This week focuses on integrating robust version control, establishing a logical project structure, and laying the groundwork for data persistence with an in-memory database and a foundational entity. 
The goal is to have the project managed under Git, organized into logical packages, and capable of interacting with a basic database entity.
- .gitignore file was already in my project, just added .env, .gitattributes, .gitignore, pom.xml
- created README.md to describe all the steps week by week.
-created 4 packages: controller, model, repository, service 
 * relocated Helloworld class in CONTROLLER package
-H2 DB configuration done 
 * opened src/main/resources/application.properties - in here copied code from google 
 * than checked if it works on localhost:8080/h2-console
 * created users.java class in model package, added JPA annotations
-In the repository package added an interface UserRepository

### **#Week 3:** User Registration & Login (Backend)
This week is dedicated to building the foundational backend components for user authentication. The primary goal is to implement the business logic required to manage user data,
create secure endpoints for user registration and login, and integrate a robust password hashing mechanism to protect user credentials. By the end of this week, the application will have functional, albeit not fully secured,
API endpoints for creating new users and verifying their credentials.
