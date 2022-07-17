package com.rose.tasksbackend.helpers.userauth;

import com.rose.tasksbackend.common.StringUtils;
import com.rose.tasksbackend.data.User;
import com.rose.tasksbackend.helpers.hashable.PasswordHashable;
import com.rose.tasksbackend.helpers.hashable.PasswordHashableFactory;
import com.rose.tasksbackend.services.UserService;

import javax.persistence.EntityNotFoundException;

public class UPAuth implements UserAuth {
    private final UserService mUserService;
    private final PasswordHashable mPasswordHashable;

    UPAuth(UserService userService, PasswordHashable passwordHashable) {
        mUserService = userService;
        mPasswordHashable = passwordHashable;
    }

    UPAuth(UserService userService) {
        mUserService = userService;
        mPasswordHashable = PasswordHashableFactory.newPasswordHashable(PasswordHashableFactory.FAKE);
    }

    @Override
    // pattern: username password
    public User authorizeToken(String token) {
        String[] elements = token.split("\\s+");
        if (elements.length != 2) {
            return null;
        }
        try {
            User user = mUserService.getReferenceById(elements[0]);
            if (mPasswordHashable.verifyHash(elements[1], user.getPassword())) {
                return user;
            } else {
                System.err.println("UPAuthorizeToken error: " + elements[0] +
                        ", can not verify password");
            }
        } catch (EntityNotFoundException e) {
            System.err.println("UPAuthorizeToken error: " + elements[0] +
                    ", " + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public String generateToken(User user, long expiredTime) {
        return user.getUsername() + " " + user.getPassword();
    }
}
