package uca.esi.dni.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.data.JSONObject;
import uca.esi.dni.handlers.JSON.JSONHandler;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JSONHandlerTest {

    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject when no input file is given")
    void givenAnEmptyInputFilepathReturnAnEmptyJSONObject() {
        assertEquals(0, JSONHandler.loadJSONObject("").size(),
                "JSONObject should be empty");
    }

    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject from a null file")
    void givenANullInputFilepathReturnAnEmptyJSONObject() {
        assertEquals(0, JSONHandler.loadJSONObject(null).size(),
                "JSONObject should be empty");
    }

    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject if the file extension is not .json")
    void givenAFilepathWithAnIncorrectFileExtensionReturnAnEmptyJSONObject() {
        String file = "data/files/jsonTestFile.txt";
        assertEquals(0, JSONHandler.loadJSONObject(file).size(),
                "JSONObject should be empty");
    }

    @Test
    @DisplayName("parseJSONObject should return an empty JSONObject from an empty string")
    void givenAnEmptyStringReturnAnEmptyJSONObject() {
        assertEquals(0, JSONHandler.parseJSONObject("").size(),
                "Parsed JSONObject should be empty");
    }

    @Test
    @DisplayName("parseJSONObject should return an empty JSONObject from a null input String")
    void givenANullInputStringReturnAnEmptyJSONObject() {
        assertEquals(0, JSONHandler.parseJSONObject(null).size(),
                "Parsed JSONObject should be empty");
    }

    @Test
    @DisplayName("generateMultiPath should return an empty string if the input map is empty")
    void givenAnEmptyMapReturnAnEmptyPathString() {
        Map<String, JSONObject> testMap = new HashMap<>();
        String emptyJSONString = new JSONObject().toString();
        assertEquals(emptyJSONString, JSONHandler.generateMultiPathJSONString(testMap),
                "String should be empty if input map is empty");
    }

    @Test
    @DisplayName("generateMultiPath should throw a NullPointerException if the input map is null")
    void givenANullInputMapThrowANullPointerException() {
        assertThrows(NullPointerException.class, () -> JSONHandler.generateMultiPathJSONString(null),
                "Input map cannot be null in generateMultiPath");
    }

    @Test
    @DisplayName("generateMultiPath should throw and exception if the map contains null JSONObjects")
    void givenAMapWithNullInputJSONObjectsThrowANullPointerException() {
        Map<String, JSONObject> testMap = new HashMap<>();
        testMap.put("test", null);
        assertThrows(NullPointerException.class, () -> JSONHandler.generateMultiPathJSONString(testMap),
                "Input map cannot contain null JSONObjects");
    }
}
