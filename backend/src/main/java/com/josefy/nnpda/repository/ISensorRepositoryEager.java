package com.josefy.nnpda.repository;

import com.josefy.nnpda.model.Sensor;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ISensorRepositoryEager extends JpaRepository<Sensor, Long> {
    @EntityGraph(value = "sensor-with-device", type = EntityGraph.EntityGraphType.LOAD)
    List<Sensor> findAll();
}
