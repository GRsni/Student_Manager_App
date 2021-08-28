package uca.esi.dni.handlers.db;

import uca.esi.dni.types.DatabaseResponseException;

import java.io.IOException;

/**
 * The interface Database handler i.
 */
public interface DatabaseHandlerI {

    /**
     * Gets data.
     *
     * @param url the url
     * @return the data
     * @throws IOException               the io exception
     * @throws NullPointerException      the null pointer exception
     * @throws DatabaseResponseException the database response exception
     */
    String getData(String url) throws IOException, NullPointerException, DatabaseResponseException;

    /**
     * Put data string.
     *
     * @param url        the url
     * @param jsonString the json string
     * @return the string
     * @throws IOException               the io exception
     * @throws NullPointerException      the null pointer exception
     * @throws DatabaseResponseException the database response exception
     */
    String putData(String url, String jsonString) throws IOException, NullPointerException, DatabaseResponseException;

    /**
     * Update data string.
     *
     * @param url        the url
     * @param jsonString the json string
     * @return the string
     * @throws IOException               the io exception
     * @throws NullPointerException      the null pointer exception
     * @throws DatabaseResponseException the database response exception
     */
    String updateData(String url, String jsonString) throws IOException, NullPointerException, DatabaseResponseException;

    /**
     * Delete data string.
     *
     * @param url the url
     * @return the string
     * @throws IOException               the io exception
     * @throws NullPointerException      the null pointer exception
     * @throws DatabaseResponseException the database response exception
     */
    String deleteData(String url) throws IOException, NullPointerException, DatabaseResponseException;

}
