package com.josefy.nnpda.repository;

import com.josefy.nnpda.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IDeviceRepositoryEager extends JpaRepository<Device, Long> {
    List<Device> findAll();
    Optional<Device> findById(Long id);
    Optional<Device> findBySerialNumber(String serialNumber);
    Set<Device> findByModelName(String modelName);
}
