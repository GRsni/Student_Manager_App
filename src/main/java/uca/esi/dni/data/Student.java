package uca.esi.dni.data;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.security.SecureRandom;
import java.util.Random;

public class Student {
    private final String VALID_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345678";
    private final int KEY_LENGTH = 7;

    private String ID;
    private String hashKey;
    private String key;
    private String email;

    public Student(String ID, String email) {
        this.ID = ID;
        this.email = email;
        this.key = generateRandomKey();
        this.hashKey = keyToHash(key);
    }

    public Student(String ID, String email, String hashKey) {
        this.ID = ID;
        this.email = email;
        this.hashKey = hashKey;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    private String keyToHash(String key) {
        return Hashing.sha256().hashString(key, Charsets.UTF_8).toString();
    }

    private String generateRandomKey() {
        StringBuilder key = new StringBuilder();
        Random gen = new SecureRandom();

        for (int i = 0; i < KEY_LENGTH; i++) {
            int index = gen.nextInt(VALID_LETTERS.length());
            key.append(VALID_LETTERS.charAt(index));
        }
        return key.toString();
    }
}
