package uca.esi.dni.types;

import org.jetbrains.annotations.NotNull;
import uca.esi.dni.handlers.Util;

import java.security.SecureRandom;
import java.util.Random;

public class Student {
    private static final String VALID_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345678";
    private static final int KEY_LENGTH = 7;

    private String id;
    private String hashKey;
    private String key;
    private String email;

    public Student() {

    }

    public Student(String id, String email) throws NullPointerException {
        this.id = id;
        this.email = email;
        this.key = generateRandomKey();
        this.hashKey = keyToHash(key);
    }

    public Student(String id, String email, String hashKey) {
        this.id = id;
        this.email = email;
        this.key = "";
        this.hashKey = hashKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public String getHashKey() {
        return hashKey;
    }

    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String keyToHash(@NotNull String key) throws NullPointerException {
        return Util.getSHA256HashedString(key);
    }

    private String generateRandomKey() {
        StringBuilder plainKey = new StringBuilder();
        Random gen = new SecureRandom();

        for (int i = 0; i < KEY_LENGTH; i++) {
            int index = gen.nextInt(VALID_LETTERS.length());
            plainKey.append(VALID_LETTERS.charAt(index));
        }
        return plainKey.toString();
    }

    public String getAttributeFromStudent(String attribute) {
        switch (attribute) {
            case "ID":
                return id;
            case "email":
                return email;
            case "hashKey":
                return hashKey;
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "ID='" + id + '\'' +
                ", plainKey='" + key + '\'' +
                ", hashKey='" + hashKey + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}