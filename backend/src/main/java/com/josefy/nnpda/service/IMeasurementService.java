package com.josefy.nnpda.service;

import com.josefy.nnpda.dto.measurement.DeviceMeasurementDto;
import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.Device;

public interface IMeasurementService {

    public Status ingestMeasurements(Device device, DeviceMeasurementDto deviceMeasurementDto);
}
