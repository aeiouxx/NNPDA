package com.josefy.nnpda.bootstrap;

import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.model.Measurement;
import com.josefy.nnpda.repository.IDeviceRepository;
import com.josefy.nnpda.repository.IMeasurementRepository;
import com.josefy.nnpda.service.IMeasurementService;
import com.josefy.nnpda.service.impl.MeasurementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
@Order(4)
@Profile("!test")
public class MeasurementGenerator  {
    private final IDeviceRepository deviceRepository;
    private final IMeasurementRepository measurementRepository;

    @Scheduled(fixedRate = 5000)
    @Transactional
    public void generateMeasurements() {
        for (var device : deviceRepository.findAll()) {
            generateMeasurementsForDevice(device);
        }
    }

    private void generateMeasurementsForDevice(Device device) {
        var measurements = new ArrayList<Measurement>();
        for (var sensor : device.getSensors()) {
            var measurement = new Measurement();
            OffsetDateTime time = OffsetDateTime.now();
            measurement.setTimestamp(time);
            measurement.setValue((double) (Math.random() * 100));
            measurement.setSensor(sensor);
            measurements.add(measurement);
        }
        measurementRepository.saveAll(measurements);
    }
}
