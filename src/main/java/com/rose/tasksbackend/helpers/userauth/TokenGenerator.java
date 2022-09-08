package com.rose.tasksbackend.helpers.userauth;

import com.rose.tasksbackend.data.User;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Calendar;

public class TokenGenerator {
    private static final byte[] PRIVATE_KEY = "Bar12345Bar12345".getBytes();
    private static final String TRANSFORMATION = "AES";

    private final Key mAesKey = new SecretKeySpec(PRIVATE_KEY, TRANSFORMATION);

    private Cipher mCipher;

    String generateToken(User user, int effectDayCount) {
        return generateToken(user.getUsername(), user.getPassword(), effectDayCount);
    }

    String generateToken(String username, String password, int effectDayCount) {
        Cipher cipher = getCipher();
        if (cipher == null) {
            return null;
        }

        Calendar currentTime = Calendar.getInstance();
        currentTime.add(Calendar.DAY_OF_MONTH, effectDayCount);

        String text = username + " " + password + " " + currentTime.getTimeInMillis();

        try {
            cipher.init(Cipher.ENCRYPT_MODE, mAesKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    User generateUser(String token) {
        Cipher cipher = getCipher();
        if (cipher == null) {
            return null;
        }

        try {
            cipher.init(Cipher.DECRYPT_MODE, mAesKey);
            byte[] decoded = Base64.getDecoder().decode(token);
            String decrypted = new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
            String[] elements = decrypted.split("\\s+");
            if (elements.length == 3) {
                String username = elements[0];
                String password = elements[1];
                long expiredTime = Long.parseLong(elements[2]);
                if (expiredTime > System.currentTimeMillis()) {
                    return new User(username, password, "");
                }
            }
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Cipher getCipher() {
        if (mCipher == null) {
            try {
                mCipher = Cipher.getInstance(TRANSFORMATION);
            } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                e.printStackTrace();
            }
        }
        return mCipher;
    }
}
