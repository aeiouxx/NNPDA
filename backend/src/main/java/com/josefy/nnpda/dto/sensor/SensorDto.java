package com.josefy.nnpda.dto.sensor;

import com.josefy.nnpda.model.Sensor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SensorDto(
        @Schema(description = "Sensor name", example = "Temperature")
        @NotNull(message = "Name is required.")
        String name,
        @Schema(description = "Serial number of sensor", example = "DEADBEEF1234")
        String serialNumber
) {

    public static SensorDto fromEntity(Sensor sensor) {
        return new SensorDto(
                sensor.getName(),
                sensor.getSerialNumber()
        );
    }

    public static Sensor toEntity(SensorDto sensorDto) {
        return new Sensor(
                sensorDto.name(),
                sensorDto.serialNumber()
        );
    }
}
