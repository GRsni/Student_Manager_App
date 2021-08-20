package uca.esi.dni.handlers.CSV;

import processing.data.JSONObject;
import processing.data.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * The interface Csv builder i.
 */
public interface CSVBuilderI {

    /**
     * Create students data tables map.
     *
     * @param allStudentsList the all students list
     * @return the map
     * @throws NullPointerException the null pointer exception
     */
    static Map<String, Map<String, Table>> createStudentsDataTables(JSONObject allStudentsList) throws NullPointerException {
        return new HashMap<>();
    }
}
