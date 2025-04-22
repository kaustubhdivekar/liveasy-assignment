package com.liveasy.assignment.dto;

import com.liveasy.assignment.entity.LoadStatus; // Import enum
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.sql.Timestamp;
import java.util.UUID;

@Data
public class LoadDto {
    private UUID id; // Not required in request body for POST, present in response

    @NotBlank(message = "Shipper ID cannot be blank")
    private String shipperId;

    @NotNull(message = "Facility details cannot be null")
    @Valid // Enable validation for nested FacilityDto
    private FacilityDto facility;

    @NotBlank(message = "Product type cannot be blank")
    private String productType;

    @NotBlank(message = "Truck type cannot be blank")
    private String truckType;

    @Min(value = 1, message = "Number of trucks must be at least 1")
    private int noOfTrucks;

    @Positive(message = "Weight must be positive")
    private double weight;

    private String comment; // Optional

    private Timestamp datePosted; // Not in request, present in response

    private LoadStatus status; // Not in request (defaults to POSTED), present in response
}