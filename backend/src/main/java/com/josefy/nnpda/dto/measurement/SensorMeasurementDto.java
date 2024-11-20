package com.josefy.nnpda.dto.measurement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record SensorMeasurementDto(
        @Schema(description = "Sensor serial number", example = "DEADBEEF1234")
        String serialNumber,

        @Schema(description = "Measurement value", example = "25.5")
        @NotNull(message = "Value is required.")
        double value,

        @Schema(description = "Timestamp of the measurement in ISO 8601 format", example = "2023-10-13T15:23:01Z")
        @NotNull(message = "Timestamp must not be null")
        OffsetDateTime timestamp
) {
}
