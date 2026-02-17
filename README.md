# Smart Finance Manager 💰

A production-style full-stack **Finance Management SaaS** application built using **Spring Boot**, **MySQL**, and **React** with **JWT-based authentication** and **role-based access control**.

## 🚀 Key Features

- 🔐 JWT Authentication & Authorization
- 👥 Role-Based Access (USER / ADMIN)
- 💰 Transaction Management (CRUD)
- 📄 Pagination & Date Filtering
- 📊 Monthly Income vs Expense Charts
- 📥 CSV Export
- 🔄 Auto Logout on Token Expiry
- ⚙️ Global Exception Handling
- 🧠 Field-Level Validation Mapping
- 🛡️ Multi-User Data Isolation

## 🏗 Architecture

### Backend

- Spring Boot
- Spring Security (JWT)
- JPA / Hibernate
- MySQL
- Layered architecture: **Controller → Service → Repository**

### Frontend

- React (Vite)
- Tailwind CSS
- Axios Interceptors
- Recharts
- Centralized AuthContext

## 🔐 Security Highlights

- Stateless JWT authentication
- Client-side token lifecycle management
- Role-based endpoint protection
- Automatic logout on token expiration
- Secure user-level data filtering

## 📦 Future Improvements

- Dockerization
- Deployment (AWS / Railway / Render)
- Admin dashboard
- Refresh token system
- Unit testing
