package com.josefy.nnpda.service.impl;

import com.josefy.nnpda.dto.measurement.DeviceMeasurementDto;
import com.josefy.nnpda.dto.measurement.SensorMeasurementDto;
import com.josefy.nnpda.infrastructure.exceptions.BadRequestException;
import com.josefy.nnpda.infrastructure.exceptions.NotFoundException;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.model.Measurement;
import com.josefy.nnpda.repository.IMeasurementRepository;
import com.josefy.nnpda.repository.ISensorRepository;
import com.josefy.nnpda.service.IMeasurementService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MeasurementService implements IMeasurementService {
    private final ISensorRepository sensorRepository;
    private final IMeasurementRepository measurementRepository;

    @Override
    @Transactional
    public Status ingestMeasurements(Device assumedSender,
                                     DeviceMeasurementDto deviceMeasurementDto) {
        List<Measurement> measurements = new ArrayList<>();
        for (SensorMeasurementDto dto : deviceMeasurementDto.measurements()) {
            var sensor = sensorRepository.findBySerialNumber(dto.serialNumber())
                    .orElseThrow(() -> new NotFoundException("Sensor", "serial number", dto.serialNumber()));
            if (!sensor.getDevice().equals(assumedSender)) {
                throw new BadRequestException("Measurements",
                        "Sensor with serial number '%s' does not belong to given device."
                        .formatted(dto.serialNumber(), assumedSender.getSerialNumber()));
            }
            Measurement measurement = new Measurement();
            measurement.setSensor(sensor);
            measurement.setValue(dto.value());
            measurement.setTimestamp(dto.timestamp());
            measurements.add(measurement);
        }
        measurementRepository.saveAll(measurements);
        return Status.SUCCESS;
    }
}
