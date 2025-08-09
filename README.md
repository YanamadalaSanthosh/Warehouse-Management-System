# Mini Warehouse Management System (WMS)

Welcome to the Mini Warehouse Management System (WMS), a full-stack web application built from the ground up using Java, Spring Boot, and MySQL. This project demonstrates a complete, real-world system for managing warehouse operations, featuring a secure REST API for programmatic access and a dynamic Thymeleaf web UI for human users.

The system is designed with a clean, conventional Spring Boot architecture, separating concerns between the data, business logic, and presentation layers. It includes role-based security, data persistence, and a suite of features for managing inventory, orders, and users.

<!-- Optional: To add a screenshot, upload an image to a site like Imgur and paste the link here. -->
<!-- Optional: To add a screenshot, upload an image to a site like Imgur and paste the link here. -->
[Dashboard Screenshot]
<img width="1920" height="1080" alt="Screenshot 2025-08-09 150224" src="https://github.com/user-attachments/assets/743847ce-7548-4200-a8eb-7c6675ddedef" />





---

## ✨ Core Features

This project is packed with features that mirror a real-world warehouse application:

#### **🛡️ Security & Authentication**
*   **Role-Based Access Control (RBAC):** Two user roles (`ADMIN`, `WORKER`) with distinct permissions.
*   **Dual Security Chains:**
    *   **Stateless JWT Authentication** for the REST API (`/api/**`), perfect for integration with other services or frontend frameworks.
    *   **Stateful Session-Based Authentication** with a traditional login form for the web UI.
*   **Protected Endpoints & UI Components:** Actions like creating users or deleting items are restricted to `ADMIN` users at both the API and UI levels using method-level security and Thymeleaf Security tags.

#### **📦 Inventory Management**
*   **Full CRUD Functionality:** Admins can create, read, update, and delete inventory items through the web UI.
*   **Stock Control:** All users can update stock levels through dedicated "Receive" and "Deduct" actions, ensuring every quantity change is logged.
*   **Auditing:** A persistent `inventory_logs` table tracks every stock movement, including what changed, by how much, when, and by whom.

#### **🧾 Order & Shipping Management**
*   **Order Creation:** Users can create customer orders, which automatically validate stock availability and deduct the required quantity from inventory.
*   **Fulfillment Workflow:** A complete workflow to manage an order's lifecycle from `PENDING` -> `PICKED` -> `SHIPPED` via the UI.
*   **Efficient Picking Lists:** Generate a picking list for any order, with items automatically sorted by their physical warehouse `location` to create an efficient picking route for workers.

#### **👤 User Management (Admin-only)**
*   A secure UI for administrators to view all registered users, create new users with specific roles, and delete users while preserving data integrity.

#### **📊 Reporting & Data Export**
*   **Live Dashboard:** A dynamic dashboard providing at-a-glance metrics, including total inventory counts, a prominent "Low Stock Alert," and a list of the most recent orders.
*   **Low Stock Reports:** A dedicated page to view all items that are at or below their reorder threshold.
*   **CSV Export:** Administrators can export the entire inventory list to a `.csv` file directly from the UI for use in external applications like Excel or Google Sheets.

#### **🛠️ Developer Experience**
*   **API Documentation:** Integrated **Swagger UI** provides live, interactive documentation for all REST API endpoints.
*   **Global Exception Handling:** A custom, user-friendly error page replaces generic server errors, providing a more professional user experience.
*   **Data Persistence:** Uses a MySQL database with a configuration that persists data between application restarts.

---

## 🔧 Tech Stack

*   **Backend:** Java 17, Spring Boot 3
*   **Database:** MySQL
*   **ORM:** Spring Data JPA / Hibernate
*   **Security:** Spring Security 6 (with JWT & Web Sessions)
*   **Frontend:** Thymeleaf (for Server-Side Rendering)
*   **API Docs:** SpringDoc OpenAPI (Swagger 3)
*   **CSV Library:** OpenCSV
*   **Build Tool:** Maven

---

## 📂 Project Structure

The project follows a standard Maven and Spring Boot structure, organized by feature and layer to promote modularity and maintainability.

```
src
└── main
    ├── java
    │   └── com
    │       └── wms
    │           ├── config          // Spring Security, JWT, OpenAPI, and Global Exception Handling.
    │           ├── controller      // Handles HTTP requests (both REST API and Web UI).
    │           ├── dto             // Data Transfer Objects for clean API requests/responses.
    │           ├── entity          // JPA entities representing database tables.
    │           ├── repository      // Spring Data JPA repositories for database access.
    │           └── service         // Contains the core business logic of the application.
    │
    └── resources
        ├── static              // For static assets like CSS and JavaScript files.
        ├── templates           // For Thymeleaf HTML templates.
        │   └── fragments       // Reusable UI components (e.g., navbar).
        ├── application.properties  // Main application configuration.
        └── data.sql            // Initial data script for populating a fresh database.
```

---

## 🚀 Getting Started

Follow these instructions to get the project running on your local machine.

### **Prerequisites**
*   JDK 17 or later
*   Apache Maven
*   MySQL Server (with a database schema created)
*   An IDE like IntelliJ or VS Code (with Lombok support enabled)

### **Installation & Setup**

1.  **Clone the Repository**
    ```bash
    git clone https://github.com/your-username/your-repo-name.git
    cd your-repo-name
    ```

2.  **Database Setup**
    -   Make sure your MySQL server is running.
    -   Connect to MySQL and create the database schema:
        ```sql
        CREATE DATABASE wms_db;
        ```

3.  **Configure the Application**
    -   Open the `src/main/resources/application.properties` file.
    -   Update the `spring.datasource.username` and `spring.datasource.password` properties to match your MySQL credentials.
    -   For development, `ddl-auto` is set to `create-drop`. To persist data between restarts, change it to `update`.

4.  **Run the Application**
    -   Open a terminal in the project's root directory.
    -   Use Maven to build and run the project:
        ```bash
        mvn spring-boot:run
        ```
    -   The application will start on `http://localhost:8080`.
    -   On the first run, Hibernate will create all the necessary tables, and the `data.sql` script will populate them with sample data.

---

## ⚙️ How to Use the Application

The application is pre-populated with two default users. The password for both is **`password`**.

*   **Username**: `admin`, **Role**: `ADMIN`
*   **Username**: `worker`, **Role**: `WORKER`

### **Using the Web UI**

1.  Open your web browser and navigate to the login page:
    `http://localhost:8080/login`
2.  Log in with either the `admin` or `worker` credentials to access the dashboard and other features.

### **Using the REST API**

1.  Access the interactive Swagger UI documentation here:
    `http://localhost:8080/`
2.  Use the `/api/auth/signin` endpoint to get a JWT token.
3.  Click the `Authorize` button at the top of the Swagger page and paste your token (e.g., `eyJhbG...`) into the `Value` field to access the locked API endpoints.

### ** OUTPUTS**
<img width="1919" height="864" alt="Screenshot 2025-08-09 150845" src="https://github.com/user-attachments/assets/284cd566-756e-4e2d-89ec-05d473e57b9e" />
<img width="1920" height="1080" alt="Screenshot 2025-08-09 150855" src="https://github.com/user-attachments/assets/ba7e5f28-35f7-43ef-9dee-bf0e032c4327" />
<img width="1920" height="1080" alt="Screenshot 2025-08-09 150915" src="https://github.com/user-attachments/assets/b6f9b964-1b83-4449-bbec-9067bd3c0ab4" />
<img width="1919" height="682" alt="Screenshot 2025-08-09 150928" src="https://github.com/user-attachments/assets/9c0f5d38-6398-428f-9607-e84059256da5" />
<img width="1920" height="1080" alt="Screenshot 2025-08-09 150956" src="https://github.com/user-attachments/assets/90b4f7ee-c822-4eff-b1c8-4bcbe3c2efb8" />
<img width="1919" height="973" alt="Screenshot 2025-08-09 151029" src="https://github.com/user-attachments/assets/3708588e-ac0f-4c93-9934-49fe35efe071" />
<img width="1919" height="616" alt="Screenshot 2025-08-09 151100" src="https://github.com/user-attachments/assets/0a69e525-1359-4691-b300-47dbaae55e6d" />















