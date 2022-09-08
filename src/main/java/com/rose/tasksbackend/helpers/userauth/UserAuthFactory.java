package com.rose.tasksbackend.helpers.userauth;

import com.rose.tasksbackend.helpers.hashable.PasswordHashable;
import com.rose.tasksbackend.services.UserService;

public class UserAuthFactory {
    private UserAuthFactory() {
    }

    public static UserAuth newUserAuth(UserService userService, PasswordHashable passwordHashable) {
        return new EncryptedAuth(userService, passwordHashable);
    }
}
