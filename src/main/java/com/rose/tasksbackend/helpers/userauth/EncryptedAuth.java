package com.rose.tasksbackend.helpers.userauth;

import com.rose.tasksbackend.data.User;
import com.rose.tasksbackend.helpers.hashable.PasswordHashable;
import com.rose.tasksbackend.services.UserService;

import javax.persistence.EntityNotFoundException;

public class EncryptedAuth implements UserAuth {
    private final TokenGenerator mTokenGenerator = new TokenGenerator();

    private final UserService mUserService;
    private final PasswordHashable mPasswordHashable;

    EncryptedAuth(UserService userService, PasswordHashable passwordHashable) {
        mUserService = userService;
        mPasswordHashable = passwordHashable;
    }

    @Override
    public User authorizeToken(String token) {
        User user = mTokenGenerator.generateUser(token);
        if (user == null) {
            return null;
        }

        try {
            User expectedUser = mUserService.getReferenceById(user.getUsername());
            if (mPasswordHashable.verifyHash(user.getPassword(), expectedUser.getPassword())) {
                return expectedUser;
            } else {
                System.err.println("EncryptedAuthorizeToken error: " + user.getPassword() +
                        ", can not verify password");
            }
        } catch (EntityNotFoundException e) {
            System.err.println("EncryptedAuthorizeToken error: " + user.getPassword() +
                    ", " + e.getLocalizedMessage());
        }
        return null;
    }

    @Override
    public String generateToken(User user, long expiredTime) {
        return mTokenGenerator.generateToken(user, 10 * 365);
    }
}
