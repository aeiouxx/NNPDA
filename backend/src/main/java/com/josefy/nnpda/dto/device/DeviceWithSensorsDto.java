package com.josefy.nnpda.dto.device;

import com.josefy.nnpda.dto.sensor.SensorDto;
import com.josefy.nnpda.model.Device;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record DeviceWithSensorsDto(
        @Schema(description = "Device serial number", example = "DEADBEEF1234")
        String serialNumber,
        @Schema(description = "Device model name", example = "ESP32")
        @NotNull(message = "Model name is required.")
        String modelName,
        @Schema(description = "Device sensors, if any.")
        List<SensorDto> sensors
)
{
    public static DeviceWithSensorsDto fromEntity(Device device) {
        var sensors = device.getSensors().stream().map(SensorDto::fromEntity).toList();
        return new DeviceWithSensorsDto(
                device.getSerialNumber(),
                device.getModelName(),
                sensors
        );
    }
}
