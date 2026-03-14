# Spring Cloud Microservices - Product & Coupon System

This repository contains a distributed system built with **Java 17** and **Spring Boot 3.1.4**, demonstrating core microservices patterns for a Master's degree project.

## 🛠 Architecture & Tech Stack

* **Service Discovery:** Netflix Eureka (Naming Server)
* **Centralized Config:** Spring Cloud Config Server
* **API Gateway:** Spring Cloud Gateway (Routing)
* **Resilience:** Resilience4j (Circuit Breaker & Fallback)
* **Database:** H2 (In-memory) / MySQL
* **Communication:** Synchronous via RestTemplate

## 🚀 Services & Ports

| Service | Port | Description |
| :--- | :--- | :--- |
| **Naming Server** | `8761` | Eureka Server for service registration. |
| **Config Server** | `8888` | Centralized external configuration. |
| **API Gateway** | `8080` | Entry point for all requests. |
| **Product Service** | `9090` | Manages products and calculates discounts. |
| **Coupon Service** | `9091` | Manages discount coupons. |

## ⚙️ Implemented Features (Requirements 3.1 - 3.3)

### 1. Infrastructure & Interaction
Implemented **Service Discovery** and **API Gateway**. Services register themselves automatically, and the Gateway routes traffic based on predicates:
- `/productapi/**` -> Product Service
- `/couponapi/**` -> Coupon Service

### 2. Fault Tolerance (Resilience4j)
The Product Service is protected by a **Circuit Breaker**. If the Coupon Service is unavailable, a fallback mechanism ensures the product is still created/processed without the discount, preventing system-wide failure.

## 🚦 How to Run

1.  **Naming Server:** Start first to allow service registration.
2.  **Config Server:** Start second to provide configurations.
3.  **Microservices:** Start Coupon and Product services.
4.  **API Gateway:** Start last to begin routing traffic.

---

## 🧪 Testing the Integration

**Create a Coupon:**
`POST http://localhost:8080/couponapi/coupons`
```json
{ "code": "SUPERSALE", "discount": 10.0, "expDate": "12/12/2026" }