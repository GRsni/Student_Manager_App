package uca.esi.dni.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.data.JSONObject;
import uca.esi.dni.handlers.json.JSONHandler;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The type Json handler test.
 */
class JSONHandlerTest {

    /**
     * Given an empty input filepath return an empty json object.
     */
    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject when no input file is given")
    void givenAnEmptyInputFilepathReturnAnEmptyJSONObject() {
        assertEquals(0, JSONHandler.loadJSONObject("").size(),
                "JSONObject should be empty");
    }

    /**
     * Given a null input filepath return an empty json object.
     */
    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject from a null file")
    void givenANullInputFilepathReturnAnEmptyJSONObject() {
        assertEquals(0, JSONHandler.loadJSONObject(null).size(),
                "JSONObject should be empty");
    }

    /**
     * Given a filepath with an incorrect file extension return an empty json object.
     */
    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject if the file extension is not .json")
    void givenAFilepathWithAnIncorrectFileExtensionReturnAnEmptyJSONObject() {
        String file = "data/files/jsonTestFile.txt";
        assertEquals(0, JSONHandler.loadJSONObject(file).size(),
                "JSONObject should be empty");
    }

    /**
     * Given an empty string return an empty json object.
     */
    @Test
    @DisplayName("parseJSONObject should return an empty JSONObject from an empty string")
    void givenAnEmptyStringReturnAnEmptyJSONObject() {
        assertEquals(0, JSONHandler.parseJSONObject("").size(),
                "Parsed JSONObject should be empty");
    }

    /**
     * Given a null input string return an empty json object.
     */
    @Test
    @DisplayName("parseJSONObject should return an empty JSONObject from a null input String")
    void givenANullInputStringReturnAnEmptyJSONObject() {
        assertEquals(0, JSONHandler.parseJSONObject(null).size(),
                "Parsed JSONObject should be empty");
    }

    /**
     * Given an empty map return an empty path string.
     */
    @Test
    @DisplayName("generateMultiPath should return an empty string if the input map is empty")
    void givenAnEmptyMapReturnAnEmptyPathString() {
        Map<String, JSONObject> testMap = new HashMap<>();
        String emptyJSONString = new JSONObject().toString();
        assertEquals(emptyJSONString, JSONHandler.generateMultiPathJSONString(testMap),
                "String should be empty if input map is empty");
    }

    /**
     * Given a null input map throw a null pointer exception.
     */
    @Test
    @DisplayName("generateMultiPath should throw a NullPointerException if the input map is null")
    void givenANullInputMapThrowANullPointerException() {
        assertThrows(NullPointerException.class, () -> JSONHandler.generateMultiPathJSONString(null),
                "Input map cannot be null in generateMultiPath");
    }

    /**
     * Given a map with null input json objects throw a null pointer exception.
     */
    @Test
    @DisplayName("generateMultiPath should throw and exception if the map contains null JSONObjects")
    void givenAMapWithNullInputJSONObjectsThrowANullPointerException() {
        Map<String, JSONObject> testMap = new HashMap<>();
        testMap.put("test", null);
        assertThrows(NullPointerException.class, () -> JSONHandler.generateMultiPathJSONString(testMap),
                "Input map cannot contain null JSONObjects");
    }
}
