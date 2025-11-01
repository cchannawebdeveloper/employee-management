# Employee Management System - Spring Boot REST API

A complete RESTful API for employee management built with Spring Boot 3.5.3, Java 21, and PostgreSQL.

## Features

- ✅ Full CRUD operations for Employee management
- ✅ PostgreSQL database with JPA/Hibernate
- ✅ Request validation with Bean Validation
- ✅ Global exception handling with @ControllerAdvice
- ✅ DTO pattern for data transfer
- ✅ CORS configuration for frontend integration
- ✅ Logging with SLF4J and Logback
- ✅ Health check endpoints with Spring Boot Actuator
- ✅ Database indexing for performance optimization

## Technology Stack

- **Java**: 21
- **Spring Boot**: 3.5.3
- **Database**: PostgreSQL
- **Build Tool**: Gradle
- **ORM**: Spring Data JPA (Hibernate)
- **Validation**: Jakarta Bean Validation
- **Logging**: SLF4J + Logback

## Project Structure

```
employee-management/
├── src/main/java/com/example/employee/
│   ├── EmployeeManagementApplication.java
│   ├── config/
│   │   └── CorsConfig.java
│   ├── controller/
│   │   └── EmployeeController.java
│   ├── dto/
│   │   ├── EmployeeDTO.java
│   │   └── ErrorResponse.java
│   ├── entity/
│   │   └── Employee.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   └── DuplicateEmailException.java
│   ├── repository/
│   │   └── EmployeeRepository.java
│   ├── service/
│   │   └── EmployeeService.java
│   └── util/
│       └── EmployeeMapper.java
├── src/main/resources/
│   └── application.yml
├── build.gradle
└── README.md
```

## Prerequisites

- Java 21 or higher
- PostgreSQL 12 or higher
- Gradle 8.x (or use the Gradle Wrapper)

## Database Setup

1. Install PostgreSQL if not already installed
2. Create a database named `employee_db`:

```sql
CREATE DATABASE employee_db;
```

3. Update database credentials in `src/main/resources/application.yml` if needed:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/employee_db
    username: postgres
    password: postgres
```

## Running the Application

### Using Gradle Wrapper (Recommended)

```bash
# On Linux/Mac
./gradlew bootRun

# On Windows
gradlew.bat bootRun
```

### Using Gradle Command

```bash
gradle bootRun
```

### Building and Running JAR

```bash
# Build the application
./gradlew build

# Run the JAR file
java -jar build/libs/employee-management-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080`

## API Endpoints

### Base URL: `http://localhost:8080/api/employees`

| Method | Endpoint | Description | Request Body |
|--------|----------|-------------|--------------|
| GET | `/api/employees` | Get all employees | - |
| GET | `/api/employees/{id}` | Get employee by ID | - |
| POST | `/api/employees` | Create new employee | EmployeeDTO |
| PUT | `/api/employees/{id}` | Update employee | EmployeeDTO |
| DELETE | `/api/employees/{id}` | Delete employee | - |

### Employee DTO Structure

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "role": "Software Engineer"
}
```

## API Testing Examples

### 1. Create Employee (POST)

```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "role": "Software Engineer"
  }'
```

### 2. Get All Employees (GET)

```bash
curl http://localhost:8080/api/employees
```

### 3. Get Employee by ID (GET)

```bash
curl http://localhost:8080/api/employees/1
```

### 4. Update Employee (PUT)

```bash
curl -X PUT http://localhost:8080/api/employees/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Smith",
    "email": "john.smith@example.com",
    "role": "Senior Software Engineer"
  }'
```

### 5. Delete Employee (DELETE)

```bash
curl -X DELETE http://localhost:8080/api/employees/1
```

## Validation Rules

- **Name**: Required, 2-100 characters
- **Email**: Required, valid email format, unique, max 100 characters
- **Role**: Required, 2-50 characters

## Error Handling

The API returns structured error responses:

```json
{
  "timestamp": "2025-11-01T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with id: 1",
  "path": "/api/employees/1"
}
```

### HTTP Status Codes

- `200 OK` - Successful GET/PUT request
- `201 Created` - Successful POST request
- `204 No Content` - Successful DELETE request
- `400 Bad Request` - Validation errors
- `404 Not Found` - Resource not found
- `409 Conflict` - Duplicate email
- `500 Internal Server Error` - Server errors

## Health Check

Access health endpoint: `http://localhost:8080/actuator/health`

## Configuration

### Application Properties

The application uses `application.yml` for configuration. Key settings:

- **Server Port**: 8080
- **Database**: PostgreSQL (localhost:5432/employee_db)
- **JPA**: Auto-create/update schema
- **Logging**: DEBUG level for application, INFO for root

### Environment-Based Configuration

Create additional configuration files for different environments:
- `application-dev.yml` - Development
- `application-prod.yml` - Production

Run with specific profile:
```bash
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## Performance Optimizations

- Database indexes on `email` and `role` columns
- HikariCP connection pooling (max 10, min 5 connections)
- Transaction management with `@Transactional`
- Read-only transactions for query operations

## Logging

Logs are configured in `application.yml`:
- Application logs: DEBUG level
- SQL queries: DEBUG level (formatted)
- Console output with timestamps

## Testing the Application

You can use tools like:
- **cURL** (command line)
- **Postman** (GUI)
- **Thunder Client** (VS Code extension)
- **HTTPie** (command line)

## Troubleshooting

### Database Connection Issues

1. Ensure PostgreSQL is running:
```bash
# Check status
sudo systemctl status postgresql

# Start if not running
sudo systemctl start postgresql
```

2. Verify database exists and credentials are correct

### Port Already in Use

Change the port in `application.yml`:
```yaml
server:
  port: 8081
```

## Future Enhancements

- [ ] Pagination and sorting
- [ ] Search and filtering
- [ ] JWT authentication
- [ ] API documentation with Swagger/OpenAPI
- [ ] Unit and integration tests
- [ ] Docker containerization
- [ ] CI/CD pipeline

## License

This project is open source and available under the MIT License.

## Contact

For questions or support, please contact the development team.