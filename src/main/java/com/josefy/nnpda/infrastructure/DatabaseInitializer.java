package com.josefy.nnpda.infrastructure;

import com.josefy.nnpda.infrastructure.repository.IUserRepository;
import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.model.Sensor;
import com.josefy.nnpda.model.User;
import com.josefy.nnpda.model.UserDevice;
import com.josefy.nnpda.repository.IDeviceRepository;
import com.josefy.nnpda.repository.ISensorRepository;
import com.josefy.nnpda.repository.IUserDeviceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {
    private final IUserRepository userRepository;
    private final IDeviceRepository deviceRepository;
    private final ISensorRepository sensorRepository;
    private final IUserDeviceRepository userDeviceRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional
    public void run(String... args) throws Exception {
        userRepository.saveAll(InitUsers());
        var device = new Device( "000000000001", "Test_Device");
        var device2 = new Device( "000000000002", "Test_Device");
        var device3 = new Device( "000000000003", "Test_Device");
        deviceRepository.saveAll(List.of(device, device2, device3));

        var sensor = new Sensor("Test_Sensor", "000000000001");
        var sensor2 = new Sensor("Test_Sensor", "000000000002");
        var sensor3 = new Sensor("Test_Sensor", "000000000003");

        var sensor4 = new Sensor("Test_Sensor", "000000000004");

        sensor.setDevice(device);
        sensor2.setDevice(device);
        sensor3.setDevice(device);
        sensorRepository.saveAll(List.of(sensor, sensor2, sensor3, sensor4));
        device.getSensors().addAll(List.of(
                sensor, sensor2, sensor3
        ));
        deviceRepository.save(device);

        UserDevice userDevice = new UserDevice( );
        userDevice.setUser(userRepository.findByUsername("test_uzivatel_1").get());
        userDevice.setDevice(device);
        userDeviceRepository.save(userDevice);
    }

    private List<User> InitUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            users.add(createUser("test_uzivatel_" + i, "test_email_" + i + "@domain.io", "SuperTajneHeslo123!!"));
        }
        return users;
    }



    private User createUser(String username, String email, String password) {
        return new User(
                username,
                email,
                passwordEncoder.encode(password)
        );
    }

}
