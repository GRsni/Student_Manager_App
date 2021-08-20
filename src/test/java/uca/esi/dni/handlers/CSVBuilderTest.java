package uca.esi.dni.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.data.JSONObject;
import uca.esi.dni.handlers.CSV.CSVBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The type Csv builder test.
 */
class CSVBuilderTest {

    /**
     * Given an empty json object return an empty studen table map.
     */
    @Test
    @DisplayName("createStudentDataTables should return an empty table map if the student list is empty")
    void givenAnEmptyJSONObjectReturnAnEmptyStudenTableMap() {
        JSONObject test = new PApplet().parseJSONObject("{}");
        assertEquals(0, CSVBuilder.createStudentsDataTables(test).size(),
                "Map should be empty");
    }

    /**
     * Given a null json object throw a null pointer exception.
     */
    @Test
    @DisplayName("createStudentDataTables should throw a NPE if the student list is null ")
    void givenANullJSONObjectThrowANullPointerException() {
        assertThrows(NullPointerException.class,
                () -> CSVBuilder.createStudentsDataTables(null),
                "Input JSONObject cant be null");
    }

    /**
     * Given a student list without lab runs return a map containing empty maps.
     */
    @Test
    @DisplayName("createStudentDataTables inner table map should be empty students have no lab runs")
    void givenAStudentListWithoutLabRunsReturnAMapContainingEmptyMaps() {
        JSONObject test = new PApplet().parseJSONObject("{\"test\":{\"practicas\":{}}}");
        assertEquals(0, CSVBuilder.createStudentsDataTables(test).get("test").size(),
                "Inner map should be empty");
    }
}
