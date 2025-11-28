# Industrial Production Simulator 🏭

> ⚠️ **Disclaimer:** This project was developed as part of the Integrated Project curriculum at **ISEP (Instituto Superior de Engenharia do Porto)**.
>
> To comply with university Intellectual Property and Academic Integrity policies, the **source code is kept private**. This repository serves as a showcase of the system architecture, technical challenges solved, and the technologies employed.

## 📖 Project Overview
The **Industrial Production Simulator** is a comprehensive software solution designed to manage, plan, and simulate operations on a factory floor. The system bridges the gap between high-level production management and low-level machine operation.

It solves the problem of scheduling production orders in a flexible industrial unit (Job-Shop), optimizing execution times, detecting bottlenecks, and managing raw material inventory.

## 🚀 Key Features

### 1. Production Planning & Simulation (Java)
* **Simulator Engine:** Implemented a priority-based simulation engine to process production orders.
* **Bottleneck Detection:** Algorithms to identify over-utilized workstations and optimize flow dependencies.
* **Data Structures:** Utilized Trees (AVL, BST) and Priority Queues to manage complex Bills of Materials (BOM) and operations.

### 2. Data Persistence & Business Logic (Oracle SQL)
* **Relational Design:** Designed a normalized database schema to handle Products, Components, Raw Materials, and Suppliers.
* **PL/SQL Automation:** Implemented stored procedures and triggers to enforce business rules (e.g., preventing circular dependencies in production trees).

### 3. Machine Integration (C & Systems)
* **Sensor Simulation:** Developed C modules to simulate machine hardware, reading temperature and humidity data.
* **Interoperability:** Integrated low-level C components with the high-level Java system for real-time machine status monitoring.

## 🛠️ Tech Stack

| Component | Technology |
| :--- | :--- |
| **Core Logic** | Java (OOP, Collections) |
| **Database** | Oracle SQL, PL/SQL |
| **Systems** | C (Sensor Simulation) |
| **Concepts** | Agile/Scrum, Data Structures, System Integration |

## 🧩 Architecture Highlights

The solution follows a modular architecture to separate concerns between the factory floor hardware and the management software:

* **Production Manager:** Handles the definition of products (BOM) and production schedules.
* **Plant Floor Manager:** visualizes machine status and layout.
* **Simulation Core:** Processes distinct production scenarios to estimate delivery dates and resource usage.

## 📬 Contact

If you are a recruiter or technical manager and wish to discuss the implementation details, architecture decisions, or my specific contributions to this project, please feel free to contact me.

* **LinkedIn:** [Amir Masnavi](https://www.linkedin.com/in/amir-masnavi-b1ab61293/)
* **Email:** a.masnavi1382@gmail.com
