package com.josefy.nnpda.repository;

import com.josefy.nnpda.model.Measurement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMeasurementRepository extends JpaRepository<Measurement, Long> {
}
