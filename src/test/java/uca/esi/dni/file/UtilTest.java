package uca.esi.dni.file;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import processing.core.PApplet;
import processing.data.JSONObject;
import uca.esi.dni.types.Student;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class UtilTest {

    private static PApplet mockedPApplet;

    @BeforeAll
    public static void initMockPApplet() {
        mockedPApplet = mock(PApplet.class);
    }

    @Test
    @DisplayName("ExtractID should return an ID from a String with a valid ID")
    void givenAStringWhenIDIsContainedThenReturnAValidID() {
        String test = "u99999999";
        assertEquals("u99999999", Util.extractId(test),
                "ID returned should be u99999999");
    }

    @Test
    @DisplayName("ExtractID should return the empty string from a String with no valid IDs")
    void givenAStringWhenNoIDIsContainedThenReturnEmptyString() {
        String test = "aaaaaaaa";
        assertEquals("", Util.extractId(test),
                "String returned should be empty");
    }

    @Test
    @DisplayName("ExtractID should return the empty string from a String with partial IDs")
    void givenAStringWhenPartialIDsAreContainedThenReturnEmptyString() {
        String test = "123343242u343";
        assertEquals("", Util.extractId(test),
                "String returned should be empty");
    }

    @Test
    @DisplayName("ExtractID should return the first valid ID from a String with multiple concatenated IDs")
    void givenAStringWhenValidAndInvalidIDsAreContainedThenReturnTheFirstValidID() {
        String test = "u12345678u123";
        assertEquals("u12345678", Util.extractId(test),
                "ID returned should be u12345678");
    }

    @Test
    @DisplayName("ExtractID should return the first valid ID from a string with multiple concatenated valid IDs")
    void givenAStringWhenValidIDsAreContainedThenReturnTheFirstValidID() {
        String test = "u11111111u22222222";
        assertEquals("u11111111", Util.extractId(test),
                "ID returned should be u11111111");
    }

    @Test
    @DisplayName("checkFileExtension should return true when the extension matches the file extension")
    void givenAFileAndMatchingExtensionReturnTrue() {
        String file = "test.txt";
        assertTrue(Util.checkFileExtension(file, "txt"),
                "Should return true");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the file provided has no extension")
    void givenAFileWithNoExtensionReturnFalse() {
        String file = "file";
        assertFalse(Util.checkFileExtension(file, "txt"),
                "Should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when no file is provided")
    void givenAnEmptyFileReturnFalse() {
        assertFalse(Util.checkFileExtension("", "txt"),
                "Test should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the extension doesn't match the file extension")
    void givenAnIncorrectExtensionReturnFalse() {
        String file = "test.txt";
        assertFalse(Util.checkFileExtension(file, "zip"),
                "Test should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the extension provided is empty")
    void givenAnEmptyExtensionReturnFalse() {
        String file = "file.txt";
        assertFalse(Util.checkFileExtension(file, ""),
                "Test should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the extension provided is null")
    void givenANullExtensionReturnFalse() {
        String file = "file.txt";
        assertFalse(Util.checkFileExtension(file, null),
                "Test should return false");
    }

    @Test
    @DisplayName("getSHA256 should return a 256 bit hashed from source")
    void givenAPlainTextStringReturnAHashedString() {
        String plain = "test";
        assertEquals(64, Util.getSHA256HashedString(plain).length(),
                "Hashed text length should be 256 characters");
    }

    @Test
    @DisplayName("getSHA256 should return the same key for the same input")
    void givenAPlainStringWhenRepeatedTestsReturnTheSameHash() {
        String plain = "test";
        assertEquals(Util.getSHA256HashedString(plain),
                Util.getSHA256HashedString(plain),
                "Output should be the same for the same imputs");
    }

    @Test
    @DisplayName("getSha256 should return a key when receiving an empty input")
    void givenAnEmptyStringReturnAHashedKey() {
        String emptyHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        assertEquals(emptyHash, Util.getSHA256HashedString(""),
                "Empty plain string returns a 256b hash");
    }

    @Test
    @DisplayName("getSha256 should throw when receiving a null input")
    void givenANullStringThrow() {
        assertThrows(NullPointerException.class,
                () -> Util.getSHA256HashedString(null),
                "getSHA256 does not allow for null input");
    }

    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject when no input file is given")
    void givenAnEmptyInputFilepathReturnAnEmptyJSONObject() {
        assertEquals(0, Util.loadJSONObject("").size(),
                "JSONObject should be empty");
    }

    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject from a null file")
    void givenANullInputFilepathReturnAnEmptyJSONObject() {
        assertEquals(0, Util.loadJSONObject(null).size(),
                "JSONObject should be empty");
    }

    @Test
    @DisplayName("loadJSONObject should return an empty JSONObject if the file extension is not .json")
    void givenAFilepathWithAnIncorrectFileExtensionReturnAnEmptyJSONObject() {
        String file = "data/files/jsonTestFile.txt";
        assertEquals(0, Util.loadJSONObject(file).size(),
                "JSONObject should be empty");
    }

    @Test
    @DisplayName("parseJSONObject should return an empty JSONObject from an empty string")
    void givenAnEmptyStringReturnAnEmptyJSONObject() {
        assertEquals(0, Util.parseJSONObject("").size(),
                "Parsed JSONObject should be empty");
    }

    @Test
    @DisplayName("parseJSONObject should return an empty JSONObject from a null input String")
    void givenANullInputStringReturnAnEmptyJSONObject() {
        assertEquals(0, Util.parseJSONObject(null).size(),
                "Parsed JSONObject should be empty");
    }

    @Test
    @DisplayName("createStudentDataTables should return an empty map if JSONObject is empty")
    void givenAnEmptyJSONObjectReturnAnEmptyMap() {
        JSONObject testObject = JSONObject.parse("{}");
        assertEquals(0, Util.createStudentsDataTables(testObject).size(),
                "Map should be empty");
    }

    @Test
    @DisplayName("createStudentDataTables should throw a NPE if the JSONObject is null")
    void givenANullJSONObjectReturnAnEmptyMap() {
        assertThrows(NullPointerException.class, () -> Util.createStudentsDataTables(null),
                "JSONObject cannot be null in createStudentDataTables");
    }

    @Test
    @DisplayName("createStudentDataTables should return an empty map if the JSONObject doesnt contain practicas innerJSONObjects")
    void givenAJSONObjectWhenTheContentsDontMatchReturnAnEmptyMap() {
        JSONObject testObject = JSONObject.parse("{ \"u11111111\":{}}");
        assertEquals(0, Util.createStudentsDataTables(testObject).size(),
                "Map should be empty if contents dont match");
    }

    @Test
    @DisplayName("generateMultiPath should return an empty string if the input map is empty")
    void givenAnEmptyMapReturnAnEmptyPathString() {
        Map<String, JSONObject> testMap = new HashMap<>();
        String emptyJSONString = new JSONObject().toString();
        assertEquals(emptyJSONString, Util.generateMultiPathJSONString(testMap),
                "String should be empty if input map is empty");
    }

    @Test
    @DisplayName("generateMultiPath should throw a NullPointerException if the input map is null")
    void givenANullInputMapThrowANullPointerException() {
        assertThrows(NullPointerException.class, () -> Util.generateMultiPathJSONString(null),
                "Input map cannot be null in generateMultiPath");
    }

    @Test
    @DisplayName("generateMultiPath should throw and exception if the map contains null JSONObjects")
    void givenAMapWithNullInputJSONObjectsThrowANullPointerException() {
        Map<String, JSONObject> testMap = new HashMap<>();
        testMap.put("test", null);
        assertThrows(NullPointerException.class, () -> Util.generateMultiPathJSONString(testMap),
                "Input map cannot contain null JSONObjects");
    }

    @Test
    @DisplayName("getStundentAttributeJSONObject should return an empty JSONObject if the student set is empty")
    void givenAnEmptyStudentSetReturnAnEmptyJSONObject() {
        Set<Student> testSet = new HashSet<>();
        assertEquals(0, Util.getStudentAttributeJSONObject(testSet, "").size(),
                "JSONObject should be empty if the set is empty");
    }

    @Test
    @DisplayName("getStudentAttributJSONObject should throw a NullPointerException if the set is null")
    void givenANullStudentSetThrowANullPointerException() {
        assertThrows(NullPointerException.class, () -> Util.getStudentAttributeJSONObject(null, ""),
                "Student set cannot be null");
    }

    @Test
    @DisplayName("getStudentAttributeJSONObject should throw a NullPointerException if the set contains a null student")
    void givenASetWithNullStudentsThrowANullPointerException() {
        Set<Student> testSet = new HashSet<>();
        testSet.add(null);
        assertThrows(NullPointerException.class, () -> Util.getStudentAttributeJSONObject(testSet, ""),
                "Student set cannot contain null Students");
    }

    @Test
    @DisplayName("getStudentAttributeJSONObject should return a correct JSONObject if the attribute is empty")
    void givenACorrectStudentSetAndAnEmptyAttributeReturnACorrectJSONObject() {
        Student testStudent = new Student("u99999999", "manual.laboratorio.rme.uca@gmail.com");
        Set<Student> testSet = new HashSet<>();
        testSet.add(testStudent);
        assertEquals(1, Util.getStudentAttributeJSONObject(testSet, "").size());
    }

    @Test
    @DisplayName("getStudentAttributeJSONObject should return a correct JSONObject if the attribute is null")
    void givenACorrectStudentSetAndANullAttributeReturnACorrectJSONObject() {
        Student testStudent = new Student("u99999999", "manual.laboratorio.rme.uca@gmail.com");
        Set<Student> testSet = new HashSet<>();
        testSet.add(testStudent);
        assertEquals(1, Util.getStudentAttributeJSONObject(testSet, null).size());
    }

    @Test
    @DisplayName("studentSetToStringSet should throw if the input set is null")
    void givenANullStudentSetWhenCreatingAStringSetThrowAnException() {
        assertThrows(NullPointerException.class, () -> Util.studentSetToStringSet(null),
                "Student set cannot be null");
    }

    @Test
    @DisplayName("studentSetToStringSet should return an empty String Set if the input set is empty")
    void givenAnEmptyStudentSetReturnAnEmptyStringSet() {
        Set<Student> testSet = new HashSet<>();
        assertEquals(0, Util.studentSetToStringSet(testSet).size(),
                "Cant make a String set out of an empty Student Set");
    }

    @Test
    @DisplayName("studentSetToStringSet should throw if input set contains null students")
    void givenAStudentSetWithNullItemsThrowAnException() {
        Set<Student> testSet = new HashSet<>();
        testSet.add(null);
        assertThrows(NullPointerException.class, () -> Util.studentSetToStringSet(testSet),
                "Input Student set cannot have null items");
    }

    @Test
    @DisplayName("getUniqueStudentSet should throw a NPE if any of the sets is null")
    void givenTheFirstStudentSetIsNullWhenGettingAnUniqueSetThrowANullPointerException() {
        Set<Student> set = new HashSet<>();
        assertThrows(NullPointerException.class, () -> Util.getUniqueStudentSet(null, set),
                "First student set cannot be null");

    }

    @Test
    @DisplayName("getUniqueStudentSet should throw a NPE if any of the sets is null")
    void givenTheSecondStudentSetIsNullWhenGettingAnUniqueSetThrowANullPointerException() {
        Set<Student> set = new HashSet<>();
        assertThrows(NullPointerException.class, () -> Util.getUniqueStudentSet(set, null),
                "Second student set cannot be null");
    }

    @Test
    @DisplayName("getUniqueStudentSet should throw a NPE if any of the student objects are null")
    void givenAnyTwoStudentSetsWithNullObjectsWhenGettingAnUniqueSetThrowANullPointerException() {
        Set<Student> testSetNull = new HashSet<>();
        Set<Student> testSet = new HashSet<>();
        testSetNull.add(null);
        testSet.add(new Student("u99999999", "test"));
        assertThrows(NullPointerException.class, () -> Util.getUniqueStudentSet(testSetNull, testSet),
                "First set cannot contain null objects");
        assertThrows(NullPointerException.class, () -> Util.getUniqueStudentSet(testSet, testSetNull),
                "Second set cannot contain null objects");
    }

    @Test
    @DisplayName("getUniqueStudentSet should throw a NPE if any of the sets is null")
    void givenTheFirstStudentSetIsNullWhenGettingAnIntersectionSetThrowANullPointerException() {
        Set<Student> set=new HashSet<>();
        assertThrows(NullPointerException.class, () -> Util.getIntersectionOfStudentSets(null,set),
                "First student set cannot be null");
    }

    @Test
    @DisplayName("getUniqueStudentSet should throw a NPE if any of the sets is null")
    void givenTheSecondStudentSetIsNullWhenGettingAnIntersectionSetThrowANullPointerException() {
        Set<Student> set=new HashSet<>();
        assertThrows(NullPointerException.class, () -> Util.getIntersectionOfStudentSets(set, null),
                "Second student set cannot be null");
    }

    @Test
    @DisplayName("getUniqueStudentSet should throw a NPE if any of the student objects are null")
    void givenAnyTwoStudentSetsWithNullObjectsWhenGettingAnIntersectionSetThrowANullPointerException() {
        Set<Student> testSetNull = new HashSet<>();
        Set<Student> testSet = new HashSet<>();
        testSetNull.add(null);
        testSet.add(new Student("u99999999", "test"));
        assertThrows(NullPointerException.class, () -> Util.getIntersectionOfStudentSets(testSetNull, testSet),
                "First set cannot contain null objects");
        assertThrows(NullPointerException.class, () -> Util.getIntersectionOfStudentSets(testSet, testSetNull),
                "Second set cannot contain null objects");
    }
}
