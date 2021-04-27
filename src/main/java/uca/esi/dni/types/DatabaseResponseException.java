package uca.esi.dni.types;

public class DatabaseResponseException extends Exception {
    public DatabaseResponseException(String why) {
        super(why);
    }
}
