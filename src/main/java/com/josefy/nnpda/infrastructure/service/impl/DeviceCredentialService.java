package com.josefy.nnpda.infrastructure.service.impl;

import com.josefy.nnpda.infrastructure.repository.IDeviceCredentialRepository;
import com.josefy.nnpda.infrastructure.service.IDeviceCredentialService;
import com.josefy.nnpda.infrastructure.utils.IHashProvider;
import com.josefy.nnpda.model.DeviceCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceCredentialService implements IDeviceCredentialService {
    private final IDeviceCredentialRepository deviceCredentialRepository;
    private final IHashProvider hashProvider;

    @Override
    public Optional<DeviceCredential> findDeviceWithCredentials(String derivedId) {
        return deviceCredentialRepository.findDeviceWithCredentials(derivedId);
    }
}
