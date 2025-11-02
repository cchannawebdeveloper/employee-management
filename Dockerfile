# Multi-stage build for optimized image size

# Stage 1: Build stage
FROM gradle:8.5-jdk21-alpine AS builder

WORKDIR /app

# Copy gradle files first for better caching
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Download dependencies (cached if build.gradle hasn't changed)
RUN gradle dependencies --no-daemon

# Copy source code
COPY src ./src

# Build the application
RUN gradle clean build -x test --no-daemon

# Stage 2: Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create a non-root user for security
RUN addgroup -S spring && adduser -S spring -G spring

# Copy the built jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Change ownership to non-root user
RUN chown spring:spring app.jar

# Switch to non-root user
USER spring:spring

# Expose application port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Run the application
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]