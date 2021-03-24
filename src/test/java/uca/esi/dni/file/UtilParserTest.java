package uca.esi.dni.file;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.data.JSONObject;
import uca.esi.dni.data.Student;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class UtilParserTest {

    private static PApplet mockedPApplet;

    @BeforeAll
    public static void initMockPApplet() {
        mockedPApplet = mock(PApplet.class);
    }

    @Test
    @DisplayName("ExtractID should return an ID from a String with a valid ID")
    public void givenAStringWhenIDIsContainedThenReturnAValidID() {
        String test = "u99999999";
        assertEquals("u99999999", UtilParser.extractId(test),
                "ID returned should be u99999999");
    }

    @Test
    @DisplayName("ExtractID should return the empty string from a String with no valid IDs")
    public void givenAStringWhenNoIDIsContainedThenReturnEmptyString() {
        String test = "aaaaaaaa";
        assertEquals("", UtilParser.extractId(test),
                "String returned should be empty");
    }

    @Test
    @DisplayName("ExtractID should return the empty string from a String with partial IDs")
    public void givenAStringWhenPartialIDsAreContainedThenReturnEmptyString() {
        String test = "123343242u343";
        assertEquals("", UtilParser.extractId(test),
                "String returned should be empty");
    }

    @Test
    @DisplayName("ExtractID should return the first valid ID from a String with multiple concatenated IDs")
    public void givenAStringWhenValidAndInvalidIDsAreContainedThenReturnTheFirstValidID() {
        String test = "u12345678u123";
        assertEquals("u12345678", UtilParser.extractId(test),
                "ID returned should be u12345678");
    }

    @Test
    @DisplayName("ExtractID should return the first valid ID from a string with multiple concatenated valid IDs")
    public void givenAStringWhenValidIDsAreContainedThenReturnTheFirstValidID() {
        String test = "u11111111u22222222";
        assertEquals("u11111111", UtilParser.extractId(test),
                "ID returned should be u11111111");
    }

    @Test
    @DisplayName("checkFileExtension should return true when the extension matches the file extension")
    public void givenAFileAndMatchingExtensionReturnTrue() {
        String file = "test.txt";
        assertTrue(UtilParser.checkFileExtension(file, "txt"),
                "Should return true");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the file provided has no extension")
    public void givenAFileWithNoExtensionReturnFalse() {
        String file = "file";
        assertFalse(UtilParser.checkFileExtension(file, "txt"),
                "Should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when no file is provided")
    public void givenAnEmptyFileReturnFalse() {
        assertFalse(UtilParser.checkFileExtension("", "txt"),
                "Test should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the extension doesn't match the file extension")
    public void givenAnIncorrectExtensionReturnFalse() {
        String file = "test.txt";
        assertFalse(UtilParser.checkFileExtension(file, "zip"),
                "Test should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the extension provided is empty")
    public void givenAnEmptyExtensionReturnFalse() {
        String file = "file.txt";
        assertFalse(UtilParser.checkFileExtension(file, ""),
                "Test should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the extension provided is null")
    public void givenANullExtensionReturnFalse() {
        String file = "file.txt";
        assertFalse(UtilParser.checkFileExtension(file, null),
                "Test should return false");
    }

    @Test
    @DisplayName("getSHA256 should return a 256 bit hashed from source")
    public void givenAPlainTextStringReturnAHashedString() {
        String plain = "test";
        assertEquals(64, UtilParser.getSHA256HashedString(plain).length(),
                "Hashed text length should be 256 characters");
    }

    @Test
    @DisplayName("getSHA256 should return the same key for the same input")
    public void givenAPlainStringWhenRepeatedTestsReturnTheSameHash() {
        String plain = "test";
        assertEquals(UtilParser.getSHA256HashedString(plain),
                UtilParser.getSHA256HashedString(plain),
                "Output should be the same for the same imputs");
    }

    @Test
    @DisplayName("getSha256 should return a key when receiving an empty input")
    public void givenAnEmptyStringReturnAHashedKey() {
        String emptyHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        assertEquals(emptyHash, UtilParser.getSHA256HashedString(""),
                "Empty plain string returns a 256b hash");
    }

    @Test
    @DisplayName("getSha256 should throw when receiving a null input")
    public void givenANullStringThrow() {
        assertThrows(NullPointerException.class,
                () -> UtilParser.getSHA256HashedString(null),
                "getSHA256 does not allow for null input");
    }

    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject when no input file is given")
    public void givenAnEmptyInputFilepathReturnAnEmptyJSONObject() {
        assertEquals(0, UtilParser.loadJSONObject("").size(),
                "JSONObject should be empty");
    }

    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject from a null file")
    public void givenANullInputFilepathReturnAnEmptyJSONObject() {
        assertEquals(0, UtilParser.loadJSONObject(null).size(),
                "JSONObject should be empty");
    }

    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject if the file extension is not .json")
    public void givenAFilepathWithAnIncorrectFileExtensionReturnAnEmptyJSONObject() {
        String file = "data/files/jsonTestFile.txt";
        assertEquals(0, UtilParser.loadJSONObject(file).size(),
                "JSONObject should be empty");
    }

    @Test
    @DisplayName("parseJSONObject should return an empty JSONObject from an empty string")
    public void givenAnEmptyStringReturnAnEmptyJSONObject() {
        assertEquals(0, UtilParser.parseJSONObject("").size(),
                "Parsed JSONObject should be empty");
    }

    @Test
    @DisplayName("parseJSONObject should return an empty JSONObject from a null input String")
    public void givenANullInputStringReturnAnEmptyJSONObject() {
        assertEquals(0, UtilParser.parseJSONObject(null).size(),
                "Parsed JSONObject should be empty");
    }

    @Test
    @DisplayName("createStudentDataTables should return an empty map if JSONObject is empty")
    public void givenAnEmptyJSONObjectReturnAnEmptyMap() {
        JSONObject testObject = JSONObject.parse("{}");
        assertEquals(0, UtilParser.createStudentsDataTables(testObject).size(),
                "Map should be empty");
    }

    @Test
    @DisplayName("createStudentDataTables should throw a NPE if the JSONObject is null")
    public void givenANullJSONObjectReturnAnEmptyMap() {
        assertThrows(NullPointerException.class, () -> UtilParser.createStudentsDataTables(null),
                "JSONObject cannot be null in createStudentDataTables");
    }

    @Test
    @DisplayName("createStudentDataTables should return an empty map if the JSONObject doesnt contain practicas innerJSONObjects")
    public void givenAJSONObjectWhenTheContentsDontMatchReturnAnEmptyMap() {
        JSONObject testObject = JSONObject.parse("{ \"u11111111\":{}}");
        System.out.println(Arrays.toString(testObject.keys().toArray()));
        assertEquals(0, UtilParser.createStudentsDataTables(testObject).size(),
                "Map should be empty if contents dont match");
    }

    @Test
    @DisplayName("generateMultiPath should return an empty string if the input map is empty")
    public void givenAnEmptyMapReturnAnEmptyPathString() {
        Map<String, JSONObject> testMap = new HashMap<>();
        String emptyJSONString = new JSONObject().toString();
        assertEquals(emptyJSONString, UtilParser.generateMultiPathJSONString(testMap),
                "String should be empty if input map is empty");
    }

    @Test
    @DisplayName("generateMultiPath should throw a NullPointerException if the input map is null")
    public void givenANullInputMapThrowANullPointerException() {
        assertThrows(NullPointerException.class, () -> UtilParser.generateMultiPathJSONString(null),
                "Input map cannot be null in generateMultiPath");
    }

    @Test
    @DisplayName("generateMultiPath should throw and exception if the map contains null JSONObjects")
    public void givenAMapWithNullInputJSONObjectsThrowANullPointerException() {
        Map<String, JSONObject> testMap = new HashMap<>();
        testMap.put("test", null);
        assertThrows(NullPointerException.class, () -> UtilParser.generateMultiPathJSONString(testMap),
                "Input map cannot contain null JSONObjects");
    }

    @Test
    @DisplayName("getStundentAttributeJSONObject should return an empty JSONObject if the student set is empty")
    public void givenAnEmptyStudentSetReturnAnEmptyJSONObject() {
        Set<Student> testSet = new HashSet<>();
        assertEquals(0, UtilParser.getStudentAttributeJSONObject(testSet, "").size(),
                "JSONObject should be empty if the set is empty");
    }

    @Test
    @DisplayName("getStudentAttributJSONObject should throw a NullPointerException if the set is null")
    public void givenANullStudentSetThrowANullPointerException() {
        assertThrows(NullPointerException.class, () -> UtilParser.getStudentAttributeJSONObject(null, ""),
                "Student set cannot be null");
    }

    @Test
    @DisplayName("getStudentAttributeJSONObject should throw a NullPointerException if the set contains a null student")
    public void givenASetWithNullStudentsThrowANullPointerException() {
        Set<Student> testSet = new HashSet<>();
        testSet.add(null);
        assertThrows(NullPointerException.class, () -> UtilParser.getStudentAttributeJSONObject(testSet, ""),
                "Student set cannot contain null Students");
    }

    @Test
    @DisplayName("getStudentAttributeJSONObject should return a correct JSONObject if the attribute is empty")
    public void givenACorrectStudentSetAndAnEmptyAttributeReturnACorrectJSONObject() {
        Student testStudent = new Student("u99999999", "test@test.com");
        Set<Student> testSet = new HashSet<>();
        testSet.add(testStudent);
        assertEquals(1, UtilParser.getStudentAttributeJSONObject(testSet, "").size());
    }

    @Test
    @DisplayName("getStudentAttributeJSONObject should return a correct JSONObject if the attribute is null")
    public void givenACorrectStudentSetAndANullAttributeReturnACorrectJSONObject() {
        Student testStudent = new Student("u99999999", "test@test.com");
        Set<Student> testSet = new HashSet<>();
        testSet.add(testStudent);
        assertEquals(1, UtilParser.getStudentAttributeJSONObject(testSet, null).size());
    }

    @Test
    @DisplayName("studentSetToStringSet should throw if the input set is null")
    public void givenANullStudentSetWhenCreatingAStringSetThrowAnException() {
        assertThrows(NullPointerException.class, () -> UtilParser.studentSetToStringSet(null),
                "Student set cannot be null");
    }

    @Test
    @DisplayName("studentSetToStringSet should return an empty String Set if the input set is empty")
    public void givenAnEmptyStudentSetReturnAnEmptyStringSet() {
        Set<Student> testSet = new HashSet<>();
        assertEquals(0, UtilParser.studentSetToStringSet(testSet).size(),
                "Cant make a String set out of an empty Student Set");
    }

    @Test
    @DisplayName("studentSetToStringSet should throw if input set contains null students")
    public void givenAStudentSetWithNullItemsThrowAnException() {
        Set<Student> testSet = new HashSet<>();
        testSet.add(null);
        assertThrows(NullPointerException.class, () -> UtilParser.studentSetToStringSet(testSet),
                "Input Student set cannot have null items");
    }
}
