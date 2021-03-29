package uca.esi.dni.data;

import org.jetbrains.annotations.NotNull;
import uca.esi.dni.file.Util;

import java.security.SecureRandom;
import java.util.Random;
import java.util.logging.Logger;

public class Student {
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private static final String VALID_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345678";
    private static final int KEY_LENGTH = 7;


    private String ID;
    private String hashKey;
    private String key;
    private String email;

    public Student() {

    }

    public Student(String ID, String email) throws NullPointerException {
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

    private String keyToHash(@NotNull String key) throws NullPointerException {
        try {
            return Util.getSHA256HashedString(key);
        } catch (NullPointerException e) {
            System.err.println("[Error while generating hashedKey]: " + e.getMessage());
            LOGGER.severe("[Error while generating hashedKey]: " + e.getMessage());
            throw e;
        }
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