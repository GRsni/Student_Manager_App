package uca.esi.dni.handlers.DB;

import uca.esi.dni.types.DatabaseResponseException;

import java.io.IOException;

public interface DatabaseHandlerI {

    String getData(String url) throws IOException, NullPointerException, DatabaseResponseException;

    String putData(String url, String jsonString) throws IOException, NullPointerException, DatabaseResponseException;

    String updateData(String url, String jsonString) throws IOException, NullPointerException, DatabaseResponseException;

    String deleteData(String url) throws IOException, NullPointerException, DatabaseResponseException;

}
