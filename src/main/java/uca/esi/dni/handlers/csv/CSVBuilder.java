package uca.esi.dni.handlers.csv;

import processing.data.JSONObject;
import processing.data.Table;
import processing.data.TableRow;
import uca.esi.dni.handlers.json.JSONHandler;
import uca.esi.dni.types.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The type Csv builder.
 */
public class CSVBuilder implements CSVBuilderI {
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * The constant PANDEO_STRING.
     */
    private static final String PANDEO_STRING = "pandeo";
    /**
     * The constant TORSION_STRING.
     */
    private static final String TORSION_STRING = "torsion";
    /**
     * The constant TRACCION_STRING.
     */
    private static final String TRACCION_STRING = "traccion";
    /**
     * The constant FLEXION_STRING.
     */
    private static final String FLEXION_STRING = "flexion";

    /**
     * The constant GENERAL_LAB_KEYS.
     */
    private static final List<Pair> GENERAL_LAB_KEYS = new ArrayList<>();
    /**
     * The constant PANDEO_LAB_KEYS.
     */
    private static final List<Pair> PANDEO_LAB_KEYS = new ArrayList<>();
    /**
     * The constant TORSION_LAB_KEYS.
     */
    private static final List<Pair> TORSION_LAB_KEYS = new ArrayList<>();
    /**
     * The constant TRACCION_LAB_KEYS.
     */
    private static final List<Pair> TRACCION_LAB_KEYS = new ArrayList<>();
    /**
     * The constant FLEXION_LAB_KEYS.
     */
    private static final List<Pair> FLEXION_LAB_KEYS = new ArrayList<>();

    static {
        loadGeneralLabKeyList();
        loadPandeoLabKeyList();
        loadTorsionLabKeyList();
    }

    /**
     * Load general lab key list.
     */
    private static void loadGeneralLabKeyList() {
        GENERAL_LAB_KEYS.add(new Pair("userID", "Identificador"));
        GENERAL_LAB_KEYS.add(new Pair("labType", "Tipo de práctica"));
        GENERAL_LAB_KEYS.add(new Pair("date", "Fecha"));
        GENERAL_LAB_KEYS.add(new Pair("manual", "Manual usado"));
        GENERAL_LAB_KEYS.add(new Pair("inLab", "Realizado en laboratorio"));
        GENERAL_LAB_KEYS.add(new Pair("valTeo", "Valor teórico a calcular"));
        GENERAL_LAB_KEYS.add(new Pair("valExp", "Valor experimental calculado"));
        GENERAL_LAB_KEYS.add(new Pair("errVal", "Errores calculando valor experimental"));
        GENERAL_LAB_KEYS.add(new Pair("data", "Datos"));
        GENERAL_LAB_KEYS.add(new Pair("q1", "Pregunta 1 correcta"));
        GENERAL_LAB_KEYS.add(new Pair("q2", "Pregunta 2 correcta"));
        GENERAL_LAB_KEYS.add(new Pair("q3", "Pregunta 3 correcta"));
        GENERAL_LAB_KEYS.add(new Pair("q4", "Pregunta 4 correcta"));
    }

    /**
     * Load pandeo lab key list.
     */
    private static void loadPandeoLabKeyList() {
        PANDEO_LAB_KEYS.add(new Pair("bar", "Barra asignada"));
        PANDEO_LAB_KEYS.add(new Pair("errBar", "Errores eligiendo barra"));
        PANDEO_LAB_KEYS.add(new Pair("fixtures", "Apoyos asignados"));
        PANDEO_LAB_KEYS.add(new Pair("errFixtures", "Errores eligiendo apoyos"));
    }

    /**
     * Load torsion lab key list.
     */
    private static void loadTorsionLabKeyList() {
        TORSION_LAB_KEYS.add(new Pair("weights", "Carga asignada"));
        TORSION_LAB_KEYS.add(new Pair("errWeights", "Errores eligiendo pesas"));
        TORSION_LAB_KEYS.add(new Pair("ampliTeo", "Valor teórico del amplificador"));
        TORSION_LAB_KEYS.add(new Pair("ampliExp", "Valor experimental del amplificador"));
    }

    /**
     * Gets lab keys list.
     *
     * @param labType the lab type
     * @return the lab keys list
     */
    private static List<Pair> getLabKeysList(String labType) {
        switch (labType) {
            case TORSION_STRING:
                return TORSION_LAB_KEYS;
            case PANDEO_STRING:
                return PANDEO_LAB_KEYS;
            case TRACCION_STRING:
                return TRACCION_LAB_KEYS;
            case FLEXION_STRING:
                return FLEXION_LAB_KEYS;
            default:
                return GENERAL_LAB_KEYS;
        }
    }

    /**
     * Instantiates a new Csv builder.
     */
    private CSVBuilder() {
    }

    /**
     * Create students data tables map.
     *
     * @param allStudentsList the all students list
     * @return the map
     * @throws NullPointerException the null pointer exception
     */
    public static Map<String, Map<String, Table>> createStudentsDataTables(JSONObject allStudentsList) throws NullPointerException {
        Map<String, Map<String, Table>> studentTableMap = new HashMap<>();
        List<String> ids = JSONHandler.getJSONObjectKeys(allStudentsList);
        for (String id : ids) {
            Map<String, Table> tableMap = new HashMap<>();
            JSONObject studentData = allStudentsList.getJSONObject(id);

            JSONObject labs = studentData.getJSONObject("practicas");
            if (labs != null) {
                List<String> labTypes = JSONHandler.getJSONObjectKeys(labs);
                for (String labType : labTypes) {
                    JSONObject labRuns = labs.getJSONObject(labType);
                    if (labRuns != null) {
                        Table dataTable = parseJSONDataIntoTable(labType, labRuns);
                        tableMap.put(labType, dataTable);
                    }
                }
                studentTableMap.put(id, tableMap);
            }
        }
        return studentTableMap;
    }

    /**
     * Parse json data into table table.
     *
     * @param labType    the lab type
     * @param jsonObject the json object
     * @return the table
     */
    private static Table parseJSONDataIntoTable(String labType, JSONObject jsonObject) {
        Table table = new Table();
        List<String> labRuns = JSONHandler.getJSONObjectKeys(jsonObject);
        List<Pair> keyList = getLabKeysList(labType);
        addColumnsToTable(table, keyList);

        for (String lab : labRuns) {
            JSONObject entryJSONObject = jsonObject.getJSONObject(lab);
            TableRow row = table.addRow();
            populateRowFromJSONObject(entryJSONObject, row);
        }
        changeTableColumnTitles(table, keyList);
        return table;
    }

    /**
     * Add columns to table.
     *
     * @param table   the table
     * @param keyList the key list
     */
    private static void addColumnsToTable(Table table, List<Pair> keyList) {
        for (Pair keyValuePair : GENERAL_LAB_KEYS) {
            table.addColumn(keyValuePair.getKey());
        }
        for (Pair keyValuePair : keyList) {
            table.addColumn(keyValuePair.getKey());
        }
    }

    /**
     * Populate row from json object.
     *
     * @param jsonObject the json object
     * @param row        the row
     */
    private static void populateRowFromJSONObject(JSONObject jsonObject, TableRow row) {
        for (String k : JSONHandler.getJSONObjectKeys(jsonObject)) {
            try {
                Object v = jsonObject.get(k);
                if (v instanceof Integer || v instanceof Long) {
                    long intToUse = ((Number) v).longValue();
                    row.setLong(k, intToUse);
                } else if (v instanceof Boolean) {
                    boolean boolToUse = (Boolean) v;
                    row.setString(k, Boolean.toString(boolToUse));
                } else if (v instanceof Float || v instanceof Double) {
                    double floatToUse = ((Number) v).doubleValue();
                    row.setDouble(k, floatToUse);
                } else if (JSONObject.NULL.equals(v)) {
                    row.setString(k, "null");
                } else {
                    String stringToUse = jsonObject.getString(k);
                    row.setString(k, stringToUse);
                }
            } catch (Exception e2) {
                LOGGER.warning("[Exception while reading JSONObject]: " + e2.getMessage());
            }
        }
    }

    /**
     * Change table column titles.
     *
     * @param table   the table
     * @param keyList the key list
     */
    private static void changeTableColumnTitles(Table table, List<Pair> keyList) {
        for (int i = 0; i < GENERAL_LAB_KEYS.size(); i++) {
            table.setColumnTitle(i, GENERAL_LAB_KEYS.get(i).getValue());
        }
        int lastGeneralColumn = GENERAL_LAB_KEYS.size();
        for (int i = 0; i < keyList.size(); i++) {
            table.setColumnTitle(i + lastGeneralColumn, keyList.get(i).getValue());
        }

    }
}
