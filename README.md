# Vessel Manager Project

## Overview
The Vessel Manager is a Spring Boot application for managing vessels. It supports CRUD operations and querying vessels by color.

---

## Prerequisites
- Docker
- Docker Compose

---

## Setup Instructions

### 1. Clone the Repository
```bash
git clone https://github.com/shpigelgi/Vessel-Manager.git
cd Vessel-Manager
```

### 2. Build and Run with Docker Compose
```bash
docker-compose up --build
```
---

## API Endpoints

### Create a Vessel
```bash
curl -X POST http://localhost:8080/vessels \
     -H "Content-Type: application/json" \
     -d '{"type": "Cargo", "color": "Blue"}'
```

### Get All Vessels
```bash
curl -X GET http://localhost:8080/vessels
```

### Get All Vessels by Color
```bash
curl -X GET http://localhost:8080/vessels/color/{color}
```

### Get Vessel by ID
```bash
curl -X GET http://localhost:8080/vessels/{id}
```

### Update a Vessel
```bash
curl -X PUT http://localhost:8080/vessels/{id} \
     -H "Content-Type: application/json" \
     -d '{"type": "Tanker", "color": "Red"}'
```

### Delete a Vessel
```bash
curl -X DELETE http://localhost:8080/vessels/{id}
```

### Delete All Vessels
```bash
curl -X DELETE http://localhost:8080/vessels
```

---

For more information, check the repository or contact Gilad Shpigelman.

