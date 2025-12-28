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

### **#Week 4:** Testing, CI/CD, and Basic Frontend Integration
Phase 1: Foundation & Setup

This week focused on establishing a solid development workflow by adding unit testing, CI/CD automation, and a minimal frontend layer for user registration.
The backend authentication logic created earlier now became fully testable, automated, and connected to a simple UI.

🔥 Completed Work This Week
1. Unit Testing Setup

Added JUnit 5 and Mockito to the project dependencies (Maven/Gradle).

Ensured proper test scopes (testImplementation, etc.).

Key outcomes:
✔ Test framework working
✔ Able to run isolated unit tests for services and controllers

2. UserService Unit Tests

Created UserServiceImplTest.java covering:

registerUser() → successful registration + duplicate username

findByUsername() → existing + non-existing users

Mocked:

UserRepository

PasswordEncoder

Key outcomes:
✔ Business logic tested
✔ Password encoding and repository interactions verified

3. AuthController Unit Tests

Created AuthControllerTest.java using MockMvc:

Tested /api/auth/register

Tested /api/auth/login

Verified:

200 OK for valid requests

400 Bad Request for invalid data

401 Unauthorized for wrong credentials

Key outcomes:
✔ API behavior validated
✔ Authentication flow tested end-to-end

4. CI/CD Pipeline (GitHub Actions)

Added a basic CI workflow that triggers on:

push

pull_request

Pipeline steps:

Checkout code

Set up JDK

Build project (mvn clean install)

Run tests

Key outcomes:
✔ Every commit now automatically runs tests
✔ CI ensures project stability

5. Basic Frontend (Thymeleaf)

Added Thymeleaf dependency

Created index.html and register.html

Implemented a simple registration form with:

Basic CSS

Navigation

Form submission to /api/auth/register

Key outcomes:
✔ Fully working registration UI
✔ Backend registration connected to HTML form

Week 4 Deliverable

A fully tested backend with:

✅ UserService tests
✅ AuthController tests
✅ Working CI/CD pipeline
✅ Basic frontend (index + register pages)
✅ Better validation and error handling for authentication

### **#Week 1:**Advanced Frontend Integration & Validation
Phase 1: Foundation & Setup

Week 5 expanded the frontend logic, improved form behavior, added validation, and enhanced the backend error-handling system.

🔥 Completed Work This Week
1. Thymeleaf Integration

Confirmed Thymeleaf compatibility with Spring Boot

Added download & rendering for templates via:

FrontendController.java

GET / → index.html

GET /register → register.html

Key outcomes:
✔ Fully structured view layer
✔ Clean routing for frontend pages

2. Improved Registration Form

Enhanced register.html:

Linked form to /api/auth/register (POST)

Added Bootstrap/Tailwind/basic CSS (optional)

Added form field bindings with Thymeleaf

Key outcomes:
✔ Functional registration UI
✔ Server + client validation

3. Client-Side Validation

Implemented simple JS validation for:

Empty fields

Password length rules

Error messages displayed instantly

Key outcomes:
✔ Better user experience
✔ Reduced invalid API calls

4. Backend Validation (JSR-303 Bean Validation)

Added validation annotations to UserRegistrationDto

Updated AuthController.register() to use:

@Valid

BindingResult

Returned clear 400 responses for invalid input

Key outcomes:
✔ Strong backend validation
✔ Safe and correct data passed to service layer

5. Showing Validation Errors in Templates

register.html now displays server-side errors:

Invalid fields

Duplicate usernames

Password validation errors

Key outcomes:
✔ User receives meaningful feedback
✔ Clean error UI

6. Success Messages

Added redirect + flash attributes:

After successful registration → show success message

Improved user flow and clarity

7. Refactored AuthController Error Handling

Added centralized exception handling with:

@ControllerAdvice

@ExceptionHandler

All errors now follow consistent JSON/API structure

Key outcomes:
✔ Standardized error responses
✔ Cleaner controller code

Week 5 Deliverable

By the end of Week 5, the application included:

✅ Fully interactive registration UI
✅ Client-side + server-side validation
✅ Proper error display in HTML
✅ Centralized exception handling
✅ Clean frontend-backend integration

### **#Week 6-7:**  Secure AI Chat Integration (Backend + Frontend)
Phase 2: Core Functionality – Tutoring Interaction

This week introduces the secure AI-powered chat system, connecting the React frontend with the Spring Boot backend and the external AI API (Gemini/OpenAI).
The main goals were:

create a backend endpoint to send messages to AI

protect the chat endpoint using JWT

connect the React chat UI with the backend

ensure only authenticated users can access the chat

write backend and frontend tests

This was the foundation of the application's intelligence and user interaction layer.

📌 What Was Built This Week
1. Global CORS Configuration (Backend)

Implemented a global CorsConfig to allow requests from the React dev server:

Allowed origins: http://localhost:3000

Allowed methods: GET, POST, PUT, DELETE, OPTIONS

Exposed headers for JWT authentication

Added WebMvcConfigurer bean

🔍 Key Concept:
CORS controls which websites can talk to your backend. Incorrect CORS = frontend cannot access API.

2. Secure API Key Management

Added AI API key into application.properties

Accessed it using @Value

Added application.properties to .gitignore so secrets never enter GitHub

For production: API key will be loaded via environment variables

🔍 Key Concept:
Never store API keys on frontend or public repositories. Backend must be the only place that knows the key.

3. ChatController – New Chat Endpoint

Created a new POST endpoint:

POST /api/chat


Accepts user's message in a ChatRequestDto

Delegates logic to ChatService

Returns structured response (ChatResponseDto)

🔍 Key Concept:
Controllers should be thin — only forward data to the service layer.

4. Securing /api/chat with Spring Security

Updated SecurityConfig:

/api/auth/** → public

/api/chat → authenticated only

JWT filter placed before username/password filter

Stateless sessions (perfect for token-based apps)

🔍 Key Concept:
Every chat message requires a valid JWT → users cannot access chat without logging in.

5. Unit Tests for ChatController

Using MockMvc with Spring Security:

Tested:

401 Unauthorized if no JWT provided

200 OK for valid JWT

400 / invalid input validation

🔍 Key Concept:
MockMvc simulates real HTTP requests to your API.

6. Unit Tests for ChatService

Mocked external API call using Mockito:

Simulated AI response

Tested request-body creation

Verified error handling

Ensured service logic works without calling real API

🔍 Key Concept:
External APIs must never be tested live — tests should be stable and offline.

7. Protected Routing (Frontend)

Created a ProtectedRoute:

Checks JWT token in localStorage

If no token → redirects to /login

Shows message: "You must be logged in to access the chat"

🔍 Key Concept:
Frontend security = routing + token validation.

8. Chat Component UI (Frontend)

Built a complete chat interface:

Messages area

Text input + Send button

Layout using Tailwind

Scroll-to-bottom logic

Distinct UI for user vs AI messages

🔍 Key Concept:
React state manages message history dynamically.

9. Authenticated API Integration

Chat component now:

Sends POST /api/chat including JWT in Authorization: Bearer ...

Uses useState for messages

Shows user’s message immediately (optimistic UI)

Adds AI reply when backend responds

🔍 Key Concept:
Axios must always attach JWT to secure endpoints.

10. Dynamic Message Rendering

Every message rendered with unique key

Scrolls down automatically on new messages

React efficiently re-renders only changed elements

🔍 Key Concept:
Keys improve React’s performance and prevent re-render bugs.

11. Unit & Integration Tests (Frontend)

Created tests with Jest + React Testing Library:

Tests include:

Rendering chat UI

Simulating typing and sending messages

Mocking Axios

Verifying Authorization header is included

Ensuring AI response appears on screen

🔍 Key Concept:
Mocking avoids calling real backend during tests.

🎯 Deliverables for Week 6

By the end of Week 6, your application had:

✅ A fully working, authenticated AI chat
✅ Backend protection with Spring Security + JWT
✅ Secure API key handling
✅ React ProtectedRoute
✅ Full chat interface
✅ Integration with backend AI endpoint
✅ Unit and integration tests for backend and frontend
🧠 Key Concepts Learned This Week
Concept	Meaning
CORS	Security rule for cross-origin requests between frontend & backend
DTO	Clean, structured objects for communication between layers
JWT Authentication	Token-based security for protecting endpoints
MockMvc	Tool to test Spring controllers via HTTP simulation
Mockito	Library to mock external dependencies (e.g., API clients)
ProtectedRoute	React component that guards pages for logged-in users
Optimistic UI Update	Rendering user's message before backend response
Axios with JWT	Automatically attaching token for secure calls
📦 Summary

Week 6-7 laid the foundation of the entire AI experience in your app:

✔ Secure
✔ Fully tested
✔ Well structured
✔ Real-time chat interaction implemented
✔ Clean integration between frontend and backend


# **Week 8 & 9 — Persistent Chat, History Sidebar & Backend Testing**

Goal: Transform the simple stateless chat into a fully persistent, multi-session AI chat system with saved history, session switching, and backend unit tests.

✅ Overview of What Was Built This Week
During Week 8, we upgraded the entire chat architecture:
⭐ Backend
Introduced Chat Sessions (ChatSession entity)
Enabled saving every message (ChatMessage entity)
Implemented session switching
Implemented chat history endpoint
Added security (restricted endpoints)
Added unit tests for repositories & service layer

⭐ Frontend
Implemented Sidebar with chat history
Clicking a session now loads its messages
Sending a message either continues existing session or automatically creates a new one
Added loading states, auto-scroll, UI polishing

Typed endpoints using TypeScript DTOs
This turned the project into a real-world, scalable, multi-chat AI application.

🧠 1. Backend Data Model
ChatSession.java
Represents a single chat session.
Fields:
*id
*title
*createdAt
*updatedAt
*user
*messages (relation)
Purpose:
✔ Stores basic metadata about a chat
✔ Allows a user to create multiple chats
✔ Orders chats by last activity (for sidebar)

ChatMessage.java
Represents one message in a conversation.
Fields:
id
session
role: "user" | "ai"
content
createdAt
Purpose:
✔ Saves both user and AI replies
✔ Allows full reconstruction of the conversation

📡 2. Chat Endpoints Added
POST /api/chat
Sends a message to AI.
Backend decides:
Create a new session OR continue existing session
Response format:
{
"response": "AI text",
"chatSessionId": 12
}

GET /api/chat/sessions

Returns all chat sessions of the current user.

Used by ChatSidebar.

GET /api/chat/sessions/{sessionId}

Returns all messages for a given chat session.

Used when switching between chats.

🔐 3. Security Improvements
Updated SecurityConfig to:
Allow unauthenticated access only to /api/auth/**
Require valid JWT for /api/chat/**
Enable CORS so frontend can talk to backend
Register JWTAuthFilter before Spring login filter
This ensures chat data is accessible only by the correct user.

🧩 4. Frontend Updates
ChatSidebar.tsx
Loads session list
Highlights active session
Calls parent when a session is selected
Chat.tsx
Stores active session in state
Loads messages when switching sessions
Sends new messages with chatSessionId
Updates chat window with previous history
Auto-scrolls to bottom

TypeScript DTOs
ChatSessionSummary
ChatMessageDto

These ensure strong typing and no bugs.

🧪 5. Test Coverage
added tests for:
✔ ChatMessageRepositoryTest
Save messages
Load messages
Messages linked to session

✔ ChatSessionRepositoryTest
Save sessions
Query sessions by user
Sort by updatedAt

✔ ChatServiceTest
Handling empty message
Mocking Gemini API
Ensuring JSON response format
This ensures persistence and logic work even after future changes.

⚙️ 6. Core Architectural Concepts (Explained Simply)
1. Persistent Chat
Every message goes into PostgreSQL → sessions persist permanently.

2. Session Switching
Frontend can open any historical chat and continue it.
Backend ensures the session belongs to the logged-in user.

3. DTO Mapping
Entities → DTOs → JSON → React
This keeps backend logic clean and frontend readable.

4. Backend Testing used:
@DataJpaTest for repositories
Mocking for service AI calls
This is how real companies test backend logic.

🚀 Result of Week 8
By the end of Week 8, your app supports:
✔ Persistent chat
✔ Session history
✔ Switching between chats
✔ Loading full history
✔ Modern UI (sidebar + chat view)
✔ Secure backend
✔ Unit tests for correctness

This is now a production-grade AI chat system.


**📘 Week 10 — Chat Persistence (Spring Boot)**
📌 Overview
In Week 10, chat persistence was implemented for a Spring Boot application.
The goal was to store chat conversations in a database and securely retrieve user-specific chat history using JWT authentication.

🎯 Objectives
Persist chat sessions and messages using JPA
Associate chats with authenticated users
Retrieve chat history and messages
Restrict access to user-owned chat sessions
Follow layered backend architecture

📦 Data Model
ChatSession
Represents a single chat conversation
Linked to one authenticated user
Contains multiple chat messages

ChatMessage
Represents a single message
Role-based (user / ai)
Supports large text content

Relationships
User → ChatSession (One-to-Many)
ChatSession → ChatMessage (One-to-Many)

🔁 Core Functionality
Create new chat sessions
Continue existing chat sessions
Store user and AI messages
Generate chat titles automatically
Retrieve chat history
Retrieve messages by session
Validate chat ownership

🔐 Security
JWT-based authentication
Authenticated user resolved via Principal
Backend-level ownership validation
Users can only access their own chats

🧪 Verification
Chat sessions persist correctly
Messages stored in correct order
Unauthorized access is blocked
Database relations created properly

📚 What I Learned
JPA entity relationships
Chat persistence design
DTO usage for API safety
Transactional service logic
Secure data access with JWT
Clean backend layering
✅ Result
Chat history persistence successfully implemented and secured.
The backend is ready for further extensions (courses, sections, pagination).