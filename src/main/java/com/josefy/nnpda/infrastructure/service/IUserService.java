package com.josefy.nnpda.infrastructure.service;

import com.josefy.nnpda.infrastructure.utils.Either;
import com.josefy.nnpda.infrastructure.utils.Status;
import com.josefy.nnpda.model.User;

public interface IUserService {
    public Either<Status, User> getById(Long id);
    public Either<Status, User> getByUsername(String username);
    public Either<Status, User> getByEmail(String email);
    public Either<Status, Void> delete(User user);
    public Either<Status, User> save(User user);


    public Either<Status, Void> requestPasswordReset(String username);
    public Either<Status, Void> resetPassword(String token, String password);
    public Either<Status, Void> changePassword(String username,
                                               String oldPassword,
                                               String newPassword);
}
