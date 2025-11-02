# Docker Deployment Guide

Complete guide for running the Employee Management API using Docker and Docker Compose.

## 📋 Prerequisites

- Docker Desktop 20.10+ or Docker Engine 20.10+
- Docker Compose 2.0+

### Install Docker

**Windows/Mac:**
- Download and install [Docker Desktop](https://www.docker.com/products/docker-desktop)

**Linux (Ubuntu/Debian):**
```bash
# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Install Docker Compose
sudo apt-get update
sudo apt-get install docker-compose-plugin

# Add user to docker group (optional, to run without sudo)
sudo usermod -aG docker $USER
newgrp docker
```

### Verify Installation
```bash
docker --version
docker compose version
```

## 🚀 Quick Start (Recommended)

### Option 1: Using Docker Compose (Easiest)

```bash
# Build and start all services (app + database)
docker compose up -d

# View logs
docker compose logs -f

# Stop all services
docker compose down

# Stop and remove volumes (delete data)
docker compose down -v
```

That's it! The application will be available at:
- **Application**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs

### Option 2: Build and Run Manually

```bash
# 1. Start PostgreSQL
docker run -d \
  --name employee-postgres \
  -e POSTGRES_DB=employee_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  postgres:16-alpine

# 2. Build the application image
docker build -t employee-management:latest .

# 3. Run the application
docker run -d \
  --name employee-app \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/employee_db \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres \
  employee-management:latest
```

## 📁 Project Structure

```
employee-management/
├── Dockerfile                 # Multi-stage build configuration
├── docker-compose.yml         # Orchestration for app + database
├── .dockerignore             # Files to exclude from Docker build
├── src/main/resources/
│   ├── application.yml       # Default configuration
│   └── application-docker.yml # Docker-specific configuration
└── DOCKER.md                 # This file
```

## 🔧 Docker Compose Services

### Services Overview

```yaml
services:
  postgres:   # PostgreSQL database
  app:        # Spring Boot application
```

### Service Details

**PostgreSQL (postgres):**
- Image: `postgres:16-alpine`
- Port: `5432`
- Database: `employee_db`
- Volume: Persistent data storage
- Health check: Automatic readiness check

**Spring Boot App (app):**
- Built from Dockerfile
- Port: `8080`
- Depends on: PostgreSQL
- Health check: Actuator endpoint
- Auto-restart: On failure

## 🎯 Docker Commands Cheat Sheet

### Basic Operations

```bash
# Start services
docker compose up -d

# View running containers
docker compose ps

# View logs
docker compose logs
docker compose logs -f app        # Follow app logs
docker compose logs -f postgres   # Follow database logs

# Stop services
docker compose stop

# Start stopped services
docker compose start

# Restart services
docker compose restart

# Stop and remove containers
docker compose down

# Stop and remove everything (including volumes)
docker compose down -v
```

### Rebuild and Update

```bash
# Rebuild images
docker compose build

# Rebuild without cache
docker compose build --no-cache

# Rebuild and restart
docker compose up -d --build

# Pull latest base images
docker compose pull
```

### Database Operations

```bash
# Access PostgreSQL shell
docker compose exec postgres psql -U postgres -d employee_db

# Backup database
docker compose exec postgres pg_dump -U postgres employee_db > backup.sql

# Restore database
docker compose exec -T postgres psql -U postgres employee_db < backup.sql

# View database logs
docker compose logs postgres
```

### Application Operations

```bash
# View application logs
docker compose logs app

# Access application shell
docker compose exec app sh

# Check application health
curl http://localhost:8080/actuator/health

# Restart only the app
docker compose restart app
```

### Monitoring and Debugging

```bash
# Check container resource usage
docker stats

# Inspect container details
docker compose exec app env

# Check container health
docker compose ps

# Follow all logs
docker compose logs -f --tail=100
```

## 🐛 Troubleshooting

### Application Won't Start

**1. Check if PostgreSQL is ready:**
```bash
docker compose logs postgres
```

**2. Check application logs:**
```bash
docker compose logs app
```

**3. Verify database connection:**
```bash
docker compose exec app env | grep SPRING_DATASOURCE
```

### Port Already in Use

**Change ports in docker-compose.yml:**
```yaml
services:
  app:
    ports:
      - "8081:8080"  # Change 8080 to 8081
```

### Database Connection Failed

**1. Verify PostgreSQL is running:**
```bash
docker compose ps postgres
```

**2. Check network connectivity:**
```bash
docker compose exec app ping postgres
```

**3. Restart services in order:**
```bash
docker compose down
docker compose up -d postgres
# Wait 10 seconds
docker compose up -d app
```

### Clean Start (Nuclear Option)

```bash
# Stop everything
docker compose down -v

# Remove all images
docker compose rm -f
docker rmi employee-management:latest

# Remove volumes
docker volume prune -f

# Start fresh
docker compose up -d --build
```

## 🔒 Production Configuration

### Environment Variables

Create a `.env` file:
```env
# Database
POSTGRES_DB=employee_db
POSTGRES_USER=admin
POSTGRES_PASSWORD=strong_password_here

# Application
SPRING_PROFILES_ACTIVE=docker
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/employee_db
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=strong_password_here
```

Use in docker-compose.yml:
```yaml
services:
  app:
    env_file:
      - .env
```

### Security Best Practices

1. **Change default passwords**
2. **Use secrets management** (Docker secrets, Vault)
3. **Don't expose PostgreSQL port** in production
4. **Use read-only filesystem** where possible
5. **Implement resource limits**

### Resource Limits

Add to docker-compose.yml:
```yaml
services:
  app:
    deploy:
      resources:
        limits:
          cpus: '1.0'
          memory: 1G
        reservations:
          cpus: '0.5'
          memory: 512M
```

## 📊 Monitoring

### Health Checks

**Application Health:**
```bash
curl http://localhost:8080/actuator/health
```

**Docker Health Status:**
```bash
docker compose ps
```

### Logs

**Export logs to file:**
```bash
docker compose logs > logs.txt
```

**Watch logs in real-time:**
```bash
docker compose logs -f --tail=50
```

## 🎓 Docker vs Local Development

| Aspect | Local Development | Docker |
|--------|------------------|---------|
| Setup Time | 15-30 minutes | 2-3 minutes |
| Dependencies | Manual install | Automatic |
| Isolation | Shared environment | Isolated containers |
| Portability | Machine-specific | Works anywhere |
| Clean-up | Manual uninstall | `docker compose down -v` |

## 🌐 Deployment Platforms

Once Dockerized, you can deploy to:

- **AWS ECS/Fargate**
- **Google Cloud Run**
- **Azure Container Instances**
- **Kubernetes (EKS, GKE, AKS)**
- **DigitalOcean App Platform**
- **Heroku Container Registry**
- **Railway**
- **Render**

## 📚 Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Docker Guide](https://spring.io/guides/topicals/spring-boot-docker/)
- [PostgreSQL Docker Hub](https://hub.docker.com/_/postgres)

## 🎉 Success Verification

After running `docker compose up -d`, verify everything works:

```bash
# 1. Check all services are running
docker compose ps

# 2. Check application health
curl http://localhost:8080/actuator/health

# 3. Access Swagger UI
open http://localhost:8080/swagger-ui.html

# 4. Test API
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","role":"Developer"}'
```

If all commands succeed, you're ready to go! 🚀

## 💡 Tips

- Use `docker compose up` (without `-d`) to see logs in real-time during development
- Use `docker compose down -v` to completely reset your environment
- Keep your Docker images updated with `docker compose pull`
- Monitor resource usage with `docker stats`
- Use `.dockerignore` to keep image sizes small

---

For more help, check the main README.md or open an issue on GitHub.