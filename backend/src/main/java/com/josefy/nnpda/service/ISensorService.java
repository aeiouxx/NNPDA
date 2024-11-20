package com.josefy.nnpda.service;

import com.josefy.nnpda.dto.sensor.SensorWithDeviceDto;
import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.Sensor;

public interface ISensorService {
    Iterable<Sensor> findAll(boolean omitDevices);
    Either<Status, Sensor> findById(long id);
    Either<Status, Sensor> findBySerialNumber(String serialNumber);
    Iterable<Sensor> findByName(String name);

    Either<Status, Sensor> create(SensorWithDeviceDto request);
    Either<Status, Sensor> update(String oldSerialNumber, SensorWithDeviceDto request);
    Either<Status, Void> delete(long id);
    Either<Status, Void> delete(String serialNumber);




    Iterable<Sensor> findByDevice(String deviceSerialNumber);
}
