package com.josefy.nnpda.service.impl;

import com.josefy.nnpda.dto.sensor.SensorWithDeviceResponseDto;
import com.josefy.nnpda.infrastructure.exceptions.NotFoundException;
import com.josefy.nnpda.infrastructure.repository.IUserRepository;
import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.model.Sensor;
import com.josefy.nnpda.model.User;
import com.josefy.nnpda.model.UserDevice;
import com.josefy.nnpda.repository.IDeviceRepository;
import com.josefy.nnpda.repository.IDeviceRepositoryEager;
import com.josefy.nnpda.repository.IUserDeviceRepository;
import com.josefy.nnpda.service.IUserDeviceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDeviceService implements IUserDeviceService {
    private final IDeviceRepository deviceRepository;
    private final IDeviceRepositoryEager deviceRepositoryEager;
    private final IUserDeviceRepository userDeviceRepository;
    private final IUserRepository userRepository;


    @Override
    public List<Device> findAssignedDevicesByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User", "username", username));
        var devices = userDeviceRepository.findByUsername(user.getUsername());
        return devices;
    }

    @Override
    public List<SensorWithDeviceResponseDto> findSensorsByUserDevice(String username, String deviceSerialNumber) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User", "username", username));
        if (!userDeviceRepository.existsByUserAndSerialNumber(user, deviceSerialNumber)) {
            throw new NotFoundException("Device", "serialNumber", deviceSerialNumber);
        }
        Device device = deviceRepository.findBySerialNumber(deviceSerialNumber)
                .orElseThrow(() -> new NotFoundException("Device", "serialNumber", deviceSerialNumber));
        return device
                .getSensors()
                .stream()
                .map(SensorWithDeviceResponseDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional
    public Either<Status, Void> unassignDeviceFromUser(String serialNumber, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User", "username", username));
        Device device = deviceRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new NotFoundException("Device", "serialNumber", serialNumber));
        if (userDeviceRepository.existsByUserAndSerialNumber(user, serialNumber)) {
            userDeviceRepository.unassignDeviceFromUser(user, serialNumber);
            return Either.right(null);
        } else {
            return Either.left(new Status("Device with serial number " + serialNumber + " is not assigned to the user.",
                    HttpStatus.NOT_FOUND));
        }
    }

    @Override
    @Transactional
    public Either<Status, UserDevice> assignDeviceToUser(String serialNumber, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User", "username", username));
        Device device = deviceRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new NotFoundException("Device", "serialNumber", serialNumber));
        if (!userDeviceRepository.existsByUserAndSerialNumber(user, serialNumber)) {
            UserDevice userDevice = new UserDevice();
            userDevice.setUser(user);
            userDevice.setDevice(device);
            userDeviceRepository.save(userDevice);
            return Either.right(userDevice);
        } else {
            return Either.left(new Status("Device with serial number " + serialNumber + " is already assigned to the user.",
                    HttpStatus.CONFLICT));
        }
    }
}
