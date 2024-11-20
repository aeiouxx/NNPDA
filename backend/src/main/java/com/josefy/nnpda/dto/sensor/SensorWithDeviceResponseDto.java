package com.josefy.nnpda.dto.sensor;

import com.josefy.nnpda.dto.device.DeviceDto;
import com.josefy.nnpda.model.Sensor;

public record SensorWithDeviceResponseDto(
        String serialNumber,
        String name,
        DeviceDto deviceDto) {

    public static SensorWithDeviceResponseDto fromEntity(Sensor sensor) {
        var device = sensor.getDevice();
        return new SensorWithDeviceResponseDto(
                sensor.getSerialNumber(),
                sensor.getName(),
                DeviceDto.fromEntity(device)
        );
    }

}
