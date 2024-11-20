package com.josefy.nnpda.bootstrap;


import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.repository.IDeviceRepository;
import com.josefy.nnpda.service.IDeviceService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.time.futureorpresent.FutureOrPresentValidatorForReadableInstant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Order(3)
@Profile("!test")
public class DeviceSeeder implements CommandLineRunner {
    private final IDeviceRepository deviceRepository;

    @Override
    public void run(String... args) throws Exception {
        Device[] devices = {
                new Device("123456789", "JEDNA_DEVAJS"),
                new Device("023456789", "NULA_DEVAJS"),
                new Device("012391023", "NEJAKEJ_DALSI")
        };

        for (Device device : devices) {
            if (!deviceRepository.existsBySerialNumber(device.getSerialNumber())) {
                deviceRepository.save(device);
            }
        }
    }
}
