package com.josefy.nnpda.repository;

import com.josefy.nnpda.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISensorRepository extends JpaRepository<Sensor, Long> {
    public List<Sensor> findBySerialNumberIn(List<String> serialNumbers);
}
