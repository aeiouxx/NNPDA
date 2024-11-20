package com.josefy.nnpda.repository;

import com.josefy.nnpda.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface ISensorRepository extends JpaRepository<Sensor, Long> {
    public List<Sensor> findBySerialNumberIn(List<String> serialNumbers);
    public Optional<Sensor> findBySerialNumber(String serialNumber);
    public List<Sensor> findByName(String name);

    public void deleteBySerialNumber(String serialNumber);
    public void deleteById(Long id);

    public boolean existsBySerialNumber(String serialNumber);

    public List<Sensor> findByDeviceSerialNumber(String deviceSerialNumber);
}
