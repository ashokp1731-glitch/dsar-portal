# DSAR Portal MVP

This project demonstrates a simple Data Subject Access Request (DSAR) portal.

Tech Stack:
- Backend: Spring Boot
- Frontend: Vue 3
- Database: H2 In-Memory
- Authentication: Basic Auth

Features:
- Customers can submit DSAR requests
- Admins can view requests
- Admins can update request status
- Audit logs track actions

Run Backend:
cd backend/dsar-backend
mvn spring-boot:run

Run Frontend:
cd frontend/dsar-frontend
npm install
npm run dev

URLs:
Frontend → http://localhost:5173  
Backend → http://localhost:8080  
H2 Console → http://localhost:8080/h2-console
