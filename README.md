# Supermarket Management System

## Project Overview
This project is a supermarket management system developed as part of an academic course on Software Systems Analysis and Design. The system is designed to assist in managing various aspects of a supermarket chain, including employee management, inventory tracking, supplier relationships, and transportation logistics. The project aims to help a growing supermarket chain transition from manual processes to a fully integrated digital system.

## System Modules

### 1. Employee Management
- **Features:**
  - Store employee details such as name, ID, bank account information, salary, and employment start date.
  - Manage employee shifts based on availability, constraints, and role requirements.
  - Automatically assign shifts with support for role-specific permissions (e.g., only shift managers can handle refunds and team management).

### 2. Inventory Management
- **Features:**
  - Track stock levels for all products across stores and warehouses.
  - Set minimum stock levels and receive alerts when inventory runs low.
  - Categorize products by category, sub-category, and additional levels of detail.
  - Manage promotions, discounts, and special offers based on product categories and sales periods.
  - Monitor and manage expired or damaged products.

### 3. Supplier Management
- **Features:**
  - Maintain detailed supplier records including contact information, payment terms, and delivery schedules.
  - Track purchase orders, supplier agreements, and automatically generate orders based on stock needs.
  - Manage multiple suppliers for the same product, supporting price comparisons and optimized purchasing.

### 4. Transportation and Delivery Management
- **Features:**
  - Manage and schedule deliveries between suppliers, warehouses, and stores.
  - Optimize delivery routes and ensure compliance with vehicle weight limits and driver requirements.
  - Track deliveries using company trucks and ensure accurate documentation for each trip.
  - Handle multiple delivery scenarios, including sourcing from different suppliers or routing deliveries to multiple locations.

## Technologies Used
- **Java** for backend development.
- **Swing** for the graphical user interface (GUI).
- **SQLite** for database management.
- **JUnit** for unit and integration testing.

## Project Structure
- **Backend Module:** Manages core logic, service layers, and database interactions.
- **Frontend Module:** Provides a GUI interface for user interaction using Swing.
- **Test Module:** Contains unit and integration tests using JUnit to ensure functionality.

## How to Use
1. **Register** new employees, assign roles, and manage work shifts.
2. **Monitor** stock levels, manage orders, and apply discounts.
3. **Manage suppliers,** place orders, and track deliveries.
4. **Plan and track deliveries** while ensuring compliance with regulations.

## Design Documentation
Detailed design documents, including the class diagram and system architecture, can be found in the `docs` folder.

## License
* This project is developed as part of an academic course and is subject to course policies regarding code sharing and reuse.
