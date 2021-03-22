package uca.esi.dni.data;

import uca.esi.dni.file.UtilParser;

import java.security.SecureRandom;
import java.util.Random;

public class Student {
    private static final String VALID_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345678";
    private static final int KEY_LENGTH = 7;

    private String ID;
    private String hashKey;
    private String key;
    private String email;

    public Student() {

    }

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

    private String keyToHash(String key) {
        return UtilParser.getSHA256HashedString(key);
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

    public String getAttributeFromStudent(String attribute) {
        switch (attribute) {
            case "ID":
                return ID;
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
                "ID='" + ID + '\'' +
                ", plainKey='" + key + '\'' +
                ", hashKey='" + hashKey + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}