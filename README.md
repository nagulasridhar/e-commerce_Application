# 🛒E-Commerce Microservices Application

An e-commerce backend application in **Java** designed to support core services for an online store.

This repository includes modules such as:
- **productservice** – APIs related to product catalog and inventory
- **paymentService** – Payment processing service integrated with Razorpay and Stripe
- Project compiled output (*in `out/production`*) and IDE configs

> _This project serves as a foundation for building a fully functional e-commerce platform with support for products, orders, users and payment integration._

---

## 🚀 Features

✔ Modular service structure  
✔ Product service for managing products  
✔ Payment service for processing transactions  
✔ Built using Java (expandable for Spring Boot / microservices)  
✔ Ready to integrate with frontend or mobile apps

---

## 📁 Repository Structure

.
├── .idea/ # IDE configuration
├── out/production/Myproject # Compiled output (Java .class files)
├── paymentService/ # Payment related service code
├── productservice/ # Product related service code
├── README.md


---

## 🛠️ Tech Stack

✔ Java  
✔ Spring Boot, REST APIs  
✔ Build tools: Maven  
✔ Database: PostgreSQL 

---

## 🧱 Prerequisites

Before you begin, ensure you have installed:
- Java JDK 11+  
- Maven   
- IDE (IntelliJ)

---

## 📦 Installation & Setup

**1. Clone the repository**
   ```bash
   git clone https://github.com/nagulasridhar/e-commerce_Application.git
   cd e-commerce_Application
   ```
**2. Open in your IDE**

- Import as a Maven project.

- Build & Run

- Using terminal:
  ```bash
  mvn clean install
  mvn spring-boot:run
  ```
