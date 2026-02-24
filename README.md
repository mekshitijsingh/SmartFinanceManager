# 💰 Smart Finance Manager

A production-grade full-stack Financial Management SaaS application built using **Spring Boot**, **React**, **PostgreSQL**, **Docker**, **Render**, and **Vercel** with secure **JWT-based authentication** and **role-based access control**.

## 🔗 Live Demo

- Frontend: https://smart-finance-manager-tawny.vercel.app
- Backend API: https://smartfinancemanager1.onrender.com

## 🚀 Key Features

- 🔐 JWT-based stateless authentication
- 👥 Role-Based Access Control (USER / ADMIN)
- 💰 Income & Expense management (CRUD)
- 📄 Pagination and date-range filtering
- 📊 Monthly income vs expense analytics
- 📥 CSV export functionality
- 🔄 Automatic logout on token expiry
- ⚙️ Global exception handling
- 🧠 Field-level validation error mapping
- 🛡️ Multi-user data isolation

## 🏗 Architecture

### Backend

- Spring Boot
- Spring Security (JWT)
- JPA / Hibernate
- PostgreSQL (cloud hosted)
- Layered architecture: Controller → Service → Repository
- Dockerized for containerized deployment

### Frontend

- React (Vite)
- Tailwind CSS
- Axios with interceptors
- Recharts for data visualization
- Centralized authentication using Context API

## 🔐 Security Highlights

- Stateless JWT authentication
- Role-based endpoint protection
- Secure password hashing with BCrypt
- Client-side token lifecycle management
- CORS configuration for secure cross-origin deployment
- Environment-based configuration for production secrets

## 🚀 Deployment

- Backend containerized using Docker
- Deployed on Render with managed PostgreSQL
- Frontend deployed on Vercel
- Production environment variables configured securely
- CORS configured for cross-origin communication

## 🧠 Intelligent Features

- Dynamic analytics dashboard
- Real-time income/expense visualization
- Intelligent tax-saving recommendation logic based on spending patterns

## 📦 Future Improvements

- Refresh token implementation
- Admin management dashboard
- Budget prediction module
- Dark mode & mobile UI optimization
- Unit & integration testing
