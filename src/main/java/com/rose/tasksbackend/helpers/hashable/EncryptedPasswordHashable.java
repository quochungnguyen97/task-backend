package com.rose.tasksbackend.helpers.hashable;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class EncryptedPasswordHashable implements PasswordHashable {
    private static final String ALGO = "PBKDF2WithHmacSHA1";
    private static final byte[] SALT = {
            1, 2, 5, 4, 5,
            2, 3, 1, 6, 1,
            2, 4, 0, 1, 7,
            9
    };
    private static final int ITERATION_COUNT = 1000;
    private static final int KEY_LENGTH = 128;

    private SecretKeyFactory mFactory;

    @Override
    public String getHash(String password) {
        SecretKeyFactory factory = getFactory();

        if (factory != null) {
            try {
                KeySpec spec = new PBEKeySpec(password.toCharArray(), SALT, ITERATION_COUNT, KEY_LENGTH);
                return Base64.getEncoder().encodeToString(factory.generateSecret(spec).getEncoded());
            } catch (InvalidKeySpecException e) {
                e.printStackTrace();
            }
        }
        return null;    }

    @Override
    public boolean verifyHash(String password, String hashedPassword) {
        String hash = getHash(password);
        if (hash == null) {
            System.out.println("Hash failed");
            return false;
        }
        return hash.equals(hashedPassword);
    }

    private SecretKeyFactory getFactory() {
        if (mFactory == null) {
            try {
                mFactory = SecretKeyFactory.getInstance(ALGO);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return mFactory;
    }
}
