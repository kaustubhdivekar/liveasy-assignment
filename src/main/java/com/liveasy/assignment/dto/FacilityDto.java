package com.liveasy.assignment.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.sql.Timestamp;

@Data
public class FacilityDto {
    @NotBlank(message = "Loading point cannot be blank")
    private String loadingPoint;

    @NotBlank(message = "Unloading point cannot be blank")
    private String unloadingPoint;

    @NotNull(message = "Loading date cannot be null")
    @FutureOrPresent(message = "Loading date must be in the present or future")
    private Timestamp loadingDate;

    @NotNull(message = "Unloading date cannot be null")
    @FutureOrPresent(message = "Unloading date must be in the present or future")
    private Timestamp unloadingDate;
    // Add validation: unloadingDate should be after loadingDate (custom validator needed)
}