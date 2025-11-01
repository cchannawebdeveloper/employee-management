package com.example.employee.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error response object")
public class ErrorResponse {

    @Schema(description = "Timestamp when the error occurred", example = "2025-11-01T10:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "HTTP status code", example = "404")
    private int status;

    @Schema(description = "Error type", example = "Not Found")
    private String error;

    @Schema(description = "Error message", example = "Employee not found with id: 1")
    private String message;

    @Schema(description = "API path where error occurred", example = "/api/employees/1")
    private String path;

    @Schema(description = "Validation errors (only for 400 Bad Request)", example = "{\"email\": \"Email must be valid\"}")
    private Map<String, String> validationErrors;

    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
}