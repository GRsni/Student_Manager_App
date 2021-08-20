package uca.esi.dni.types;

/**
 * The type Pair.
 */
public class Pair {
    /**
     * The Key.
     */
    private String key;
    /**
     * The Value.
     */
    private String value;

    /**
     * Instantiates a new Pair.
     *
     * @param key   the key
     * @param value the value
     */
    public Pair(String key, String value) {
        this.key = key;
        this.value = value;
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
     * Sets key.
     *
     * @param key the key
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets value.
     *
     * @param value the value
     */
    public void setValue(String value) {
        this.value = value;
    }
}
