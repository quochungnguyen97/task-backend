package com.rose.tasksbackend.helpers.hashable;

public interface PasswordHashable {
    String getHash(String password);
    boolean verifyHash(String password, String hashedPassword);
}
