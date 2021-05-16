package uca.esi.dni.handlers.CSV;

import processing.data.JSONObject;
import processing.data.Table;

import java.util.HashMap;
import java.util.Map;

public interface CSVBuilderI {

    static Map<String, Map<String, Table>> createStudentsDataTables(JSONObject allStudentsList) throws NullPointerException {
        return new HashMap<>();
    }
}
