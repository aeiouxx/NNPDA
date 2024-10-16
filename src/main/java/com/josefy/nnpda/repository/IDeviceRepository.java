package com.josefy.nnpda.repository;

import com.josefy.nnpda.model.Device;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IDeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findAll();
    Optional<Device> findById(Long id);

    boolean existsBySerialNumber(String serialNumber);
    Optional<Device> findBySerialNumber(String serialNumber);
    List<Device> findBySerialNumberIn(List<String> serialNumbers);
    Set<Device> findByModelName(String modelName);
    Device save(Device device);
    void deleteBySerialNumber(String serialNumber);
    void deleteById(Long id);
}
