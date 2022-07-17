package com.rose.tasksbackend.helpers.hashable;

public class PasswordHashableFactory {
    private PasswordHashableFactory() {
    }

    public static final int FAKE = 0;
    public static final int REAL = 1;

    public static PasswordHashable newPasswordHashable(int type) {
        return new FakePasswordHashable();
    }
}
