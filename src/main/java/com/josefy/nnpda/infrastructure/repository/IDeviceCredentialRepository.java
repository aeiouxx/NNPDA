package com.josefy.nnpda.infrastructure.repository;

import com.josefy.nnpda.model.DeviceCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IDeviceCredentialRepository extends JpaRepository<DeviceCredential, Long> {
    // Because I wanna lazy fetch the credentials when I fetch device but one to one relation
    // doesn't allow me to do that while eager fetching on the credentials side
    @Query("SELECT dc FROM DeviceCredential dc JOIN FETCH dc.device WHERE dc.derivedId = :derived_id")
    Optional<DeviceCredential> findDeviceWithCredentials(@Param("derived_id") String derivedId);
}
