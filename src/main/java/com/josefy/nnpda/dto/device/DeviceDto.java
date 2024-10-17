package com.josefy.nnpda.dto.device;

import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.validation.SerialNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public record DeviceDto(
        @Schema(description = "Device serial number", example = "DEADBEEF1234")
        @SerialNumber
        String serialNumber,
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
