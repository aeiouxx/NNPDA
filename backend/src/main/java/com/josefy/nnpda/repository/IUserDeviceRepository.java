package com.josefy.nnpda.repository;

import com.josefy.nnpda.model.Device;
import com.josefy.nnpda.model.User;
import com.josefy.nnpda.model.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IUserDeviceRepository extends JpaRepository<UserDevice, Long> {

    @Query("SELECT ud.device FROM UserDevice ud WHERE ud.user.username = :username")
    List<Device> findByUsername(@Param("username") String username);

    @Modifying
    @Query("DELETE FROM UserDevice ud WHERE ud.user = :user AND ud.device.serialNumber = :serialNumber")
    void unassignDeviceFromUser(@Param("user") User user, @Param("serialNumber") String serialNumber);

    @Query("SELECT COUNT(ud) > 0 FROM UserDevice ud WHERE ud.user = :user AND ud.device.serialNumber = :serialNumber")
    boolean existsByUserAndSerialNumber(@Param("user") User user, @Param("serialNumber") String serialNumber);
}
