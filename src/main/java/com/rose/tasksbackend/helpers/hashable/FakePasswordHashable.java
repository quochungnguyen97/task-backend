package com.rose.tasksbackend.helpers.hashable;

import java.util.Objects;

public class FakePasswordHashable implements PasswordHashable {
    FakePasswordHashable() {

    }
    @Override
    public String getHash(String password) {
        return password;
    }

    @Override
    public boolean verifyHash(String password, String hashedPassword) {
        return Objects.equals(password, hashedPassword);
    }
}
