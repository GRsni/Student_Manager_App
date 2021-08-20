package uca.esi.dni.types;

/**
 * The type Database response exception.
 */
public class DatabaseResponseException extends Exception {
    /**
     * Instantiates a new Database response exception.
     *
     * @param why the why
     */
    public DatabaseResponseException(String why) {
        super(why);
    }
}
