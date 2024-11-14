package com.josefy.nnpda.service.impl;

import com.josefy.nnpda.dto.device.DeviceDto;
import com.josefy.nnpda.dto.device.DeviceWithSensorSerialsDto;
import com.josefy.nnpda.dto.device.DeviceWithSensorsDto;
import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.model.Sensor;
import com.josefy.nnpda.repository.IDeviceRepository;
import com.josefy.nnpda.repository.IDeviceRepositoryEager;
import com.josefy.nnpda.repository.ISensorRepository;
import com.josefy.nnpda.service.IDeviceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeviceService implements IDeviceService {
    private final IDeviceRepository deviceRepository;
    private final IDeviceRepositoryEager deviceRepositoryEager;
    private final ISensorRepository sensorRepository;

    @Override
    public Iterable<Device> findAll(boolean withSensors) {
        if (withSensors) {
            return deviceRepositoryEager.findAll();
        }
        else {
            return deviceRepository.findAll();
        }
    }
    @Override
    public Either<Status, Device> findById(long id, boolean withSensors) {
        return deviceRepository.findById(id)
                .map(Either::<Status, Device>right)
                .orElseGet(() -> Either.left(new Status("Device not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public Either<Status, Device> findBySerialNumber(String serialNumber, boolean withSensors) {
        return deviceRepository.findBySerialNumber(serialNumber)
                .map(Either::<Status, Device>right)
                .orElseGet(() -> Either.left(new Status("Device not found", HttpStatus.NOT_FOUND)));
    }

    @Override
    public Iterable<Device> findByModelName(String modelName, boolean withSensors) {
        return deviceRepository.findByModelName(modelName);
    }

    @Override
    @Transactional
    public Either<Status, Device> create(DeviceWithSensorSerialsDto deviceRequest) {
        var serialNumber = deviceRequest.serialNumber();
        if (deviceRepository.existsBySerialNumber(serialNumber)) {
            return Either.left(new Status("Device with serial number '%s' already exists".formatted(serialNumber), HttpStatus.CONFLICT));
        }
        if (deviceRequest.sensors() != null) {
            var sensors = sensorRepository.findBySerialNumberIn(deviceRequest.sensors());
            var missingSerialNumbers = getMissingSerialNumbers(deviceRequest.sensors(), sensors);
            if (missingSerialNumbers != null) {
                return Either.left(new Status("Sensors with serial numbers '%s' do not exist.".formatted(missingSerialNumbers),
                        HttpStatus.BAD_REQUEST));
            }
            var device = new Device(serialNumber, deviceRequest.modelName());
            sensors.forEach(sensor -> sensor.setDevice(device));
            device.setSensors(sensors);
            return Either.right(deviceRepository.save(device));
        }
        else {
            return Either.right(deviceRepository.save(new Device(serialNumber, deviceRequest.modelName())));
        }
    }

    private String getMissingSerialNumbers(List<String> requested, List<Sensor> found) {
        return requested.stream()
                .filter(serial -> found.stream().noneMatch(sensor -> sensor.getSerialNumber().equals(serial)))
                .reduce((s1, s2) -> s1 + ", " + s2)
                .orElse(null);
    }

    @Override
    @Transactional
    public Either<Status, Device> update(String oldSerialNumber, DeviceWithSensorSerialsDto request) {
        var device = deviceRepository.findBySerialNumber(oldSerialNumber)
                .orElse(null);
        if (device == null) {
            return Either.left(new Status("Device with serial number '%s' does not exist.".formatted(oldSerialNumber),
                    HttpStatus.NOT_FOUND));
        }
        if (!oldSerialNumber.equals(request.serialNumber()) && deviceRepository.existsBySerialNumber(request.serialNumber())) {
            return Either.left(new Status("Device with serial number '%s' already exists.".formatted(request.serialNumber()),
                    HttpStatus.CONFLICT));
        }
        if (request.sensors() != null) {
            var sensors = sensorRepository.findBySerialNumberIn(request.sensors());
            var missingSerialNumbers = getMissingSerialNumbers(request.sensors(), sensors);
            if (missingSerialNumbers != null) {
                return Either.left(new Status("Sensors with serial numbers '%s' do not exist.".formatted(missingSerialNumbers),
                        HttpStatus.BAD_REQUEST));
            }
            // todo: Do we unset the old sensors?
            // todo: Do we remove the old sensors completely?
            device.setSerialNumber(request.serialNumber());
            device.setModelName(request.modelName());
            device.setSensors(sensors);
            sensors.forEach(sensor -> sensor.setDevice(device));
        }
        else {
            device.setSerialNumber(request.serialNumber());
            device.setModelName(request.modelName());
        }
        return Either.right(deviceRepository.save(device));
    }

    @Override
    @Transactional
    public Either<Status, Void> delete(long id) {
        deviceRepository.deleteById(id);
        return Either.right(null);
    }

    @Override
    @Transactional
    public Either<Status, Void> delete(String serialNumber) {
        deviceRepository.deleteBySerialNumber(serialNumber);
        return Either.right(null);
    }
}
