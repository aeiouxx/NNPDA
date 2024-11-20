package com.josefy.nnpda.dto.sensor;

import com.josefy.nnpda.model.Sensor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SensorWithDeviceDto(
        @Schema(description = "Sensor serial number", example = "DEADBEEF1234")
        String serialNumber,

        @Schema(description = "Sensor name", example = "Temperature sensor")
        @NotNull
        String name,

        @Schema(description = "Device serial number", example = "DEADBEEF")
        @NotNull
        String deviceSerialNumber) {

        public static SensorWithDeviceDto fromEntity(Sensor sensor) {
                var device = sensor.getDevice();
                return new SensorWithDeviceDto(
                        sensor.getSerialNumber(),
                        sensor.getName(),
                        device == null ? null : device.getSerialNumber()
                );
        }

}
