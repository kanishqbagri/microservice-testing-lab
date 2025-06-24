# 🧪 Microservice Testing Lab

A hands-on architecture built to demonstrate **testing strategies** across a Java Spring Boot microservices ecosystem. This project showcases real-world patterns for unit testing, API testing, security, contract, chaos, and more.

---

## 🏗️ Project Structure

This lab is composed of multiple independently containerized microservices:

| Service             | Port  | Description                          |
|---------------------|-------|--------------------------------------|
| `user-service`      | 8081  | Handles user registration & auth     |
| `product-service`   | 8082  | Manages product catalog              |
| `order-service`     | 8083  | Processes and tracks orders          |
| `notification-service` | 8084  | Sends user notifications             |
| `gateway-service`   | 8080  | API Gateway / Reverse proxy          |

Infrastructure includes:
- **PostgreSQL** for persistent storage
- **RabbitMQ** for async messaging
- **Docker Compose** for orchestration

---

## 🔍 Goals

This lab aims to showcase how to layer tests across a microservice system:

- ✅ Unit Testing with JUnit & Mockito
- ✅ API Testing with REST Assured / Postman / Karate
- ✅ Security Testing (auth flows, role checks)
- ✅ Contract Testing with Pact
- ✅ UI Testing with Selenium / Playwright
- ✅ Chaos Testing with Chaos Monkey / Gremlin
- ✅ Load Testing with JMeter / k6
- ✅ Integration & E2E Testing across services

---

## 🚀 Getting Started

### Prerequisites

- Docker + Docker Compose installed
- Java 17+
- Maven

### Run All Services

```bash
./mvnw clean package -DskipTests
docker compose up --build

# 🧪 Microservice Testing Lab

A hands-on architecture built to demonstrate **testing strategies** across a Java Spring Boot microservices ecosystem. This project showcases real-world patterns for unit testing, API testing, security, contract, chaos, and more.

---

## 🏗️ Project Structure

This lab is composed of multiple independently containerized microservices:

| Service             | Port  | Description                          |
|---------------------|-------|--------------------------------------|
| `user-service`      | 8081  | Handles user registration & auth     |
| `product-service`   | 8082  | Manages product catalog              |
| `order-service`     | 8083  | Processes and tracks orders          |
| `notification-service` | 8084  | Sends user notifications             |
| `gateway-service`   | 8080  | API Gateway / Reverse proxy          |

Infrastructure includes:
- **PostgreSQL** for persistent storage
- **RabbitMQ** for async messaging
- **Docker Compose** for orchestration

---

## 🔍 Goals

This lab aims to showcase how to layer tests across a microservice system:

- ✅ Unit Testing with JUnit & Mockito
- ✅ API Testing with REST Assured / Postman / Karate
- ✅ Security Testing (auth flows, role checks)
- ✅ Contract Testing with Pact
- ✅ UI Testing with Selenium / Playwright
- ✅ Chaos Testing with Chaos Monkey / Gremlin
- ✅ Load Testing with JMeter / k6
- ✅ Integration & E2E Testing across services

---

## 🚀 Getting Started

### Prerequisites

- Docker + Docker Compose installed
- Java 17+
- Maven

### Run All Services

```bash
./mvnw clean package -DskipTests
docker compose up --build

