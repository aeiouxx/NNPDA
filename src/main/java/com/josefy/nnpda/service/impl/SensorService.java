package com.josefy.nnpda.service.impl;

import com.josefy.nnpda.dto.sensor.SensorWithDeviceDto;
import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.model.Sensor;
import com.josefy.nnpda.repository.IDeviceRepository;
import com.josefy.nnpda.repository.ISensorRepository;
import com.josefy.nnpda.repository.ISensorRepositoryEager;
import com.josefy.nnpda.service.ISensorService;
import jakarta.transaction.Transactional;
import jdk.jfr.Timespan;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SensorService implements ISensorService {
    private final ISensorRepository sensorRepository;
    private final ISensorRepositoryEager sensorRepositoryEager;
    private final IDeviceRepository deviceRepository;

    @Override
    public Iterable<Sensor> findAll() {
        return sensorRepository.findAll();
    }

    @Override
    public Either<Status, Sensor> findById(long id) {
        return sensorRepository.findById(id)
                .map(Either::<Status, Sensor>right)
                .orElseGet(() -> Either.left(new Status("Sensor not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public Either<Status, Sensor> findBySerialNumber(String serialNumber) {
        return sensorRepository.findBySerialNumber(serialNumber)
                .map(Either::<Status, Sensor>right)
                .orElseGet(() -> Either.left(new Status("Sensor not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public Iterable<Sensor> findByName(String name) {
        return sensorRepository.findByName(name);
    }

    @Override
    @Transactional
    public Either<Status, Sensor> create(SensorWithDeviceDto request) {
        var serialNumber = request.serialNumber();
        if (sensorRepository.existsBySerialNumber(serialNumber)) {
            return Either.left(new Status("Sensor with serial number '%s' already exists".formatted(serialNumber), HttpStatus.CONFLICT));
        }
        var sensor = new Sensor(request.name(), request.serialNumber());
        Device device = null;
        if (request.deviceSerialNumber() != null) {
            device = deviceRepository.findBySerialNumber(request.deviceSerialNumber()).orElse(null);
            if (device == null) {
                return Either.left(new Status("Device with serial number '%s' not found".formatted(request.deviceSerialNumber()), HttpStatus.NOT_FOUND));
            }
            sensor.setDevice(device);
            device.getSensors().add(sensor);
        }
        var savedSensor = sensorRepository.save(sensor);
        if (device != null) {
            deviceRepository.save(device);
        }
        return Either.right(savedSensor);
    }

    @Override
    @Transactional
    public Either<Status, Sensor> update(String oldSerialNumber, SensorWithDeviceDto request) {
        var sensor = sensorRepository.findBySerialNumber(oldSerialNumber).orElse(null);
        if (sensor == null) {
            return Either.left(new Status("Sensor with serial number '%s' not found".formatted(oldSerialNumber), HttpStatus.NOT_FOUND));
        }
        sensor.setName(request.name());
        if (!oldSerialNumber.equals(request.serialNumber())) {
            if (sensorRepository.existsBySerialNumber(request.serialNumber())) {
                return Either.left(new Status("Sensor with serial number '%s' already exists".formatted(request.serialNumber()), HttpStatus.CONFLICT));
            }
            sensor.setSerialNumber(request.serialNumber());
        }
        if (request.deviceSerialNumber() != null) {
            var device = deviceRepository.findBySerialNumber(request.deviceSerialNumber()).orElse(null);
            if (device == null) {
                return Either.left(new Status("Device with serial number '%s' not found".formatted(request.deviceSerialNumber()), HttpStatus.NOT_FOUND));
            }
            var oldDevice = sensor.getDevice();
            if (oldDevice != null) {
                oldDevice.getSensors().remove(sensor);
                deviceRepository.save(oldDevice);
            }
            sensor.setDevice(device);
            device.getSensors().add(sensor);
            deviceRepository.save(device);
        }
        return Either.right(sensorRepository.save(sensor));
    }

    @Override
    @Transactional
    public Either<Status, Void> delete(long id) {
        sensorRepository.deleteById(id);
        return Either.right(null);
    }

    @Override
    @Transactional
    public Either<Status, Void> delete(String serialNumber) {
        sensorRepository.deleteBySerialNumber(serialNumber);
        return Either.right(null);
    }

}
