package com.josefy.nnpda.service;

import com.josefy.nnpda.dto.sensor.SensorWithDeviceResponseDto;
import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.model.UserDevice;

import java.util.List;

public interface IUserDeviceService {
    public List<Device> findAssignedDevicesByUser(String username);
    public List<SensorWithDeviceResponseDto> findSensorsByUserDevice(String username, String deviceSerialNumber);
    public Either<Status, Void> unassignDeviceFromUser(String serialNumber, String username);
    public Either<Status, UserDevice> assignDeviceToUser(String serialNumber, String username);
}
