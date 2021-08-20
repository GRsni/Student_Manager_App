package uca.esi.dni.types;

import org.jetbrains.annotations.NotNull;
import uca.esi.dni.handlers.Util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * The type Student.
 */
public class Student {
    /**
     * The constant VALID_LETTERS.
     */
    private static final String VALID_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345678";
    /**
     * The constant KEY_LENGTH.
     */
    private static final int KEY_LENGTH = 7;

    /**
     * The Id.
     */
    private String id;
    /**
     * The Hash key.
     */
    private String hashKey;
    /**
     * The Key.
     */
    private String key;
    /**
     * The Email.
     */
    private String email;

    /**
     * Instantiates a new Student.
     */
    public Student() {

    }

    /**
     * Instantiates a new Student.
     *
     * @param id    the id
     * @param email the email
     * @throws NullPointerException the null pointer exception
     */
    public Student(String id, String email) throws NullPointerException {
        this.id = id;
        this.email = email;
        this.key = generateRandomKey();
        this.hashKey = keyToHash(key);
    }

    /**
     * Instantiates a new Student.
     *
     * @param id      the id
     * @param email   the email
     * @param hashKey the hash key
     */
    public Student(String id, String email, String hashKey) {
        this.id = id;
        this.email = email;
        this.key = "";
        this.hashKey = hashKey;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets key.
     *
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets hash key.
     *
     * @return the hash key
     */
    public String getHashKey() {
        return hashKey;
    }

    /**
     * Sets hash key.
     *
     * @param hashKey the hash key
     */
    public void setHashKey(String hashKey) {
        this.hashKey = hashKey;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Key to hash string.
     *
     * @param key the key
     * @return the string
     * @throws NullPointerException the null pointer exception
     */
    private String keyToHash(@NotNull String key) throws NullPointerException {
        return Util.getSHA256HashedString(key);
    }

    /**
     * Generate random key string.
     *
     * @return the string
     */
    private String generateRandomKey() {
        StringBuilder plainKey = new StringBuilder();
        Random gen = new SecureRandom();

        for (int i = 0; i < KEY_LENGTH; i++) {
            int index = gen.nextInt(VALID_LETTERS.length());
            plainKey.append(VALID_LETTERS.charAt(index));
        }
        return plainKey.toString();
    }

    /**
     * Gets attribute from student.
     *
     * @param attribute the attribute
     * @return the attribute from student
     */
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

    /**
     * To string string.
     *
     * @return the string
     */
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