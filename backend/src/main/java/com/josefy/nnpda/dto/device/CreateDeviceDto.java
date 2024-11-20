package com.josefy.nnpda.dto.device;

import com.josefy.nnpda.model.Device;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record CreateDeviceDto(
        @Schema(description = "Device serial number", example = "DEADBEEF1234")
        String serialNumber,

        @Schema(description = "API key hash", example = "oasdfgh9iwerht4391082")
        @NotNull(message = "API key hash is required.")
        String apiKeyHash,

        @Schema(description = "Device model name", example = "ESP32")
        @NotNull(message = "Model name is required.")
        String modelName
)
{
    public static Device toEntity(DeviceDto deviceDto) {
        return new Device(deviceDto.serialNumber(), deviceDto.modelName());
    }
    public static DeviceDto fromEntity(Device device) {
        return new DeviceDto(device.getSerialNumber(), device.getModelName());
    }
}
