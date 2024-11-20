package com.josefy.nnpda.service;


import com.josefy.nnpda.dto.device.CreateDeviceWithSensorSerialsDto;
import com.josefy.nnpda.dto.device.DeviceDto;
import com.josefy.nnpda.dto.device.DeviceWithSensorSerialsDto;
import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.Device;

public interface IDeviceService {
    Iterable<Device> findAll(boolean withSensors);
    Either<Status, Device> findById(long id, boolean withSensors);
    Either<Status, Device> findBySerialNumber(String serialNumber, boolean withSensors);
    Iterable<Device> findByModelName(String modelName, boolean withSensors);

    Either<Status, Device> create(CreateDeviceWithSensorSerialsDto device);
    Either<Status, Device> update(String oldSerialNumber, CreateDeviceWithSensorSerialsDto device);
    Either<Status, Void> delete(long id);
    Either<Status, Void> delete(String serialNumber);
}
