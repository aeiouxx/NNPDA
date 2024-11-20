package com.josefy.nnpda.dto.sensor;

import com.josefy.nnpda.dto.device.DeviceDto;
import com.josefy.nnpda.model.Sensor;

public record SensorWithDeviceResponseDto(
        String serialNumber,
        String name,
        String deviceSerial,
        String deviceName)
{
    public static SensorWithDeviceResponseDto fromEntity(Sensor sensor) {
        var device = sensor.getDevice();
        var deviceDto = device == null ? null : DeviceDto.fromEntity(device);
        return new SensorWithDeviceResponseDto(
                sensor.getSerialNumber(),
                sensor.getName(),
                deviceDto == null ? null : deviceDto.serialNumber(),
                deviceDto == null ? null : deviceDto.modelName()
        );
    }
}
