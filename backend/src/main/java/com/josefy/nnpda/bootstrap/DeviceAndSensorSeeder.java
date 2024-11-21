package com.josefy.nnpda.bootstrap;


import com.josefy.nnpda.infrastructure.repository.IUserRepository;
import com.josefy.nnpda.infrastructure.utils.IHashProvider;
import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.model.DeviceCredential;
import com.josefy.nnpda.model.Sensor;
import com.josefy.nnpda.model.UserDevice;
import com.josefy.nnpda.repository.IDeviceRepository;
import com.josefy.nnpda.repository.ISensorRepository;
import com.josefy.nnpda.repository.IUserDeviceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Order(3)
@Profile("!test")
public class DeviceAndSensorSeeder implements CommandLineRunner {
    private final IDeviceRepository deviceRepository;
    private final ISensorRepository sensorRepository;
    private final IUserRepository userRepository;
    private final IUserDeviceRepository userDeviceRepository;
    private final IHashProvider hashProvider;
    private static final int SERIAL_NUMBER_LENGTH = 12;
    private static final String HEX_CHARS = "0123456789ABCDEF";

    public static String generateSerialNumber() {
        StringBuilder serialNumber = new StringBuilder(SERIAL_NUMBER_LENGTH);
        Random random = new Random();

        for (int i = 0; i < SERIAL_NUMBER_LENGTH; i++) {
            int index = random.nextInt(HEX_CHARS.length());
            serialNumber.append(HEX_CHARS.charAt(index));
        }

        return serialNumber.toString();
    }

    @Override
    public void run(String... args) throws Exception {
        final String prefix = "ZARIZENI_";
        Device[] devices = {
                new Device("000000000001", "ZARIZENI_JEDNA"),
                new Device("000000000002", "ZARIZENI_DVA"),
                new Device("000000000003", "ZARIZENI_TRI"),
                new Device("000000000004", "ZARIZENI_CTYRI"),
                new Device("000000000005", "ZARIZENI_PET"),
                new Device("000000000006", "ZARIZENI_SEST"),
                new Device("000000000007", "ZARIZENI_SEDM"),
                new Device("000000000008", "ZARIZENI_OSM"),
                new Device("000000000009", "ZARIZENI_DEVET"),
                new Device("000000000010", "ZARIZENI_DESET")
        };

        for (Device device : devices) {
            var keyHash = hashProvider.hash(device.getSerialNumber());
            var credentials = generateCredentials(device, keyHash);
            device.setDeviceCredential(credentials);
            deviceRepository.save(device);
        }

        Random random = new Random();
        var foundDevices = deviceRepository.findAll();
        var admin = userRepository.findByUsername("admin").get();
        for (Device device : foundDevices) {
            List<UserDevice> userDevices = new ArrayList<>();
            if (!userDeviceRepository.existsByUserAndSerialNumber(admin, device.getSerialNumber())) {
                var userDevice = new UserDevice();
                userDevice.setUser(admin);
                userDevice.setDevice(device);
                userDevices.add(userDevice);
            }
            int sensorCount = random.nextInt(5) + 1;
            List<Sensor> sensors = new ArrayList<>();

            for (int i = 0; i < sensorCount; i++) {
                var sensor = new Sensor();
                sensor.setDevice(device);
                sensor.setSerialNumber(generateSerialNumber());
                var name = device.getModelName().substring(prefix.length()) + "_SENZOR_" + (i + 1);
                sensor.setName(name);
                sensors.add(sensor);
            }
            sensorRepository.saveAll(sensors);
            userDeviceRepository.saveAll(userDevices);
        }
    }

    private DeviceCredential generateCredentials(Device toSave, String keyHash) {
        var credentials = new DeviceCredential();
        credentials.setDevice(toSave);
        credentials.setApiKey(keyHash);
        credentials.setDerivedId(hashProvider.hmac(toSave.getSerialNumber(), keyHash));
        credentials.setCreatedAt(Instant.now());
        return credentials;
    }
}
