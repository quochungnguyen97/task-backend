package com.rose.tasksbackend.helpers.userauth;

import com.rose.tasksbackend.data.User;

public interface UserAuth {
    User authorizeToken(String token);
    String generateToken(User user, long expiredTime);
}
