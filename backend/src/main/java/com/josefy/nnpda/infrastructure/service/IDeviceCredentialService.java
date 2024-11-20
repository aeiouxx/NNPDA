package com.josefy.nnpda.infrastructure.service;

import com.josefy.nnpda.model.DeviceCredential;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IDeviceCredentialService {
    Optional<DeviceCredential> findDeviceWithCredentials(String derivedId);
}
