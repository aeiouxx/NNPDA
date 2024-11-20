package com.josefy.nnpda.dto.measurement;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record DeviceMeasurementDto(
        @Schema(description = "List of measurements")
        @NotEmpty
        @Valid
        List<SensorMeasurementDto> measurements)
{
}
