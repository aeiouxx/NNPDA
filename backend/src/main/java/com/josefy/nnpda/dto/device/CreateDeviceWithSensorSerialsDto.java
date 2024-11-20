package com.josefy.nnpda.dto.device;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateDeviceWithSensorSerialsDto(
        @Schema(description = "Device serial number", example = "DEADBEEF1234")
        String serialNumber,

        @Schema(description = "API key hash", example = "oasdfgh9iwerht4391082")
        @NotNull(message = "API key hash is required.")
        String apiKeyHash,

        @Schema(description = "Device model name", example = "ESP32")
        @NotNull(message = "Model name is required.")
        String modelName,

        @Schema(description = "Device sensors, if any.", example = "[\"DEADBEEF1234\", \"DEADBEEF5678\"]")
        List<String> sensors
)
{
}
