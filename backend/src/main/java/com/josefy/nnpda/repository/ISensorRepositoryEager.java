package com.josefy.nnpda.repository;

import com.josefy.nnpda.model.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISensorRepositoryEager extends JpaRepository<Sensor, Long> {
}
