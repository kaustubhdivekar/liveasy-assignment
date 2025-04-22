package com.liveasy.assignment.dto;

import com.liveasy.assignment.entity.BookingStatus; // Import enum
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import java.sql.Timestamp;
import java.util.UUID;

@Data
public class BookingDto {
    private UUID id; // Not required in request body for POST, present in response

    @NotNull(message = "Load ID cannot be null")
    private UUID loadId; // Use this in the request DTO

    @NotBlank(message = "Transporter ID cannot be blank")
    private String transporterId;

    @Positive(message = "Proposed rate must be positive")
    private double proposedRate;

    private String comment; // Optional

    private BookingStatus status; // Not in POST request (defaults to PENDING), present in response/PUT

    private Timestamp requestedAt; // Not in request, present in response
}