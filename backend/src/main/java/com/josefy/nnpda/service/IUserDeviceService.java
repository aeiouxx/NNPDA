package com.josefy.nnpda.service;

import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.User;
import com.josefy.nnpda.model.UserDevice;

import java.util.List;

public interface IUserDeviceService {
    public List<UserDevice> findAllForUser(User user);
    public Either<Status, Void> unassignDeviceFromUser(String serialNumber, User user);
    public Either<Status, UserDevice> assignDeviceToUser(String serialNumber, User user);
}
