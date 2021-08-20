package uca.esi.dni.handlers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import uca.esi.dni.types.Student;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Util test.
 */
class UtilTest {

    /**
     * Given a valid 8 digit format id return true.
     *
     * @param input the input
     */
    @ParameterizedTest
    @DisplayName("CheckID should return true for a valid user ID")
    @ValueSource(strings = {"u99999999", "uy9999999", "uY9999999", "ux9999999", "uX9999999", "uz9999999", "uZ9999999"})
    void givenAValid8DigitFormatIDReturnTrue(String input) {
        assertTrue(Util.checkId(input),
                "CheckId should return true");
    }

    /**
     * Given an empty id string return false.
     */
    @Test
    @DisplayName("CheckID should return false for an empty ID String")
    void givenAnEmptyIDStringReturnFalse() {
        assertFalse(Util.checkId(""),
                "Empty String should return false");
    }

    /**
     * Given an invalid user id then return false.
     *
     * @param input the input
     */
    @ParameterizedTest
    @DisplayName("CheckID should return false for invalid user IDs")
    @ValueSource(strings = {"1111111", "111111111", "uuuu99999999"})
    void givenAnInvalidUserIDThenReturnFalse(String input) {
        assertFalse(Util.checkId(input),
                "CheckID should return false for " + input);
    }

    /**
     * Given a valid string id return the valid id.
     *
     * @param input the input
     */
    @ParameterizedTest
    @DisplayName("ExtractId should return the correct ID for valid ID Strings")
    @ValueSource(strings = {"u99999999", "uy9999999", "uY9999999", "ux9999999", "uX9999999", "uz9999999", "uZ9999999"})
    void givenAValidStringIDReturnTheValidID(String input) {
        assertEquals(input, Util.extractId(input),
                "ExtractID should return " + input);
    }

    /**
     * Given a string when no id is contained then return empty string.
     */
    @Test
    @DisplayName("ExtractID should return the empty string from a String with no valid IDs")
    void givenAStringWhenNoIDIsContainedThenReturnEmptyString() {
        String test = "aaaaaaaa";
        assertEquals("", Util.extractId(test),
                "String returned should be empty");
    }

    /**
     * Given a string when partial i ds are contained then return empty string.
     */
    @Test
    @DisplayName("ExtractID should return the empty string from a String with partial IDs")
    void givenAStringWhenPartialIDsAreContainedThenReturnEmptyString() {
        String test = "123343242u343";
        assertEquals("", Util.extractId(test),
                "String returned should be empty");
    }

    /**
     * Given a string when valid and invalid i ds are contained then return the first valid id.
     */
    @Test
    @DisplayName("ExtractID should return the first valid ID from a String with multiple concatenated IDs")
    void givenAStringWhenValidAndInvalidIDsAreContainedThenReturnTheFirstValidID() {
        String test = "u12345678u123";
        assertEquals("u12345678", Util.extractId(test),
                "ID returned should be u12345678");
    }

    /**
     * Given a string when valid i ds are contained then return the first valid id.
     */
    @Test
    @DisplayName("ExtractID should return the first valid ID from a string with multiple concatenated valid IDs")
    void givenAStringWhenValidIDsAreContainedThenReturnTheFirstValidID() {
        String test = "u11111111u22222222";
        assertEquals("u11111111", Util.extractId(test),
                "ID returned should be u11111111");
    }

    /**
     * Given a file and matching extension return true.
     */
    @Test
    @DisplayName("checkFileExtension should return true when the extension matches the file extension")
    void givenAFileAndMatchingExtensionReturnTrue() {
        String file = "test.txt";
        assertTrue(Util.checkFileExtension(file, "txt"),
                "Should return true");
    }

    /**
     * Given a file with no extension return false.
     */
    @Test
    @DisplayName("checkFileExtension should return false when the file provided has no extension")
    void givenAFileWithNoExtensionReturnFalse() {
        String file = "file";
        assertFalse(Util.checkFileExtension(file, "txt"),
                "Should return false");
    }

    /**
     * Given an empty file return false.
     */
    @Test
    @DisplayName("checkFileExtension should return false when no file is provided")
    void givenAnEmptyFileReturnFalse() {
        assertFalse(Util.checkFileExtension("", "txt"),
                "Test should return false");
    }

    /**
     * Given an incorrect extension return false.
     */
    @Test
    @DisplayName("checkFileExtension should return false when the extension doesn't match the file extension")
    void givenAnIncorrectExtensionReturnFalse() {
        String file = "test.txt";
        assertFalse(Util.checkFileExtension(file, "zip"),
                "Test should return false");
    }

    /**
     * Given an empty extension return false.
     */
    @Test
    @DisplayName("checkFileExtension should return false when the extension provided is empty")
    void givenAnEmptyExtensionReturnFalse() {
        String file = "file.txt";
        assertFalse(Util.checkFileExtension(file, ""),
                "Test should return false");
    }

    /**
     * Given a null extension return false.
     */
    @Test
    @DisplayName("checkFileExtension should return false when the extension provided is null")
    void givenANullExtensionReturnFalse() {
        String file = "file.txt";
        assertFalse(Util.checkFileExtension(file, null),
                "Test should return false");
    }

    /**
     * Given a plain text string return a hashed string.
     */
    @Test
    @DisplayName("getSHA256 should return a 256 bit hashed from source")
    void givenAPlainTextStringReturnAHashedString() {
        String plain = "test";
        assertEquals(64, Util.getSHA256HashedString(plain).length(),
                "Hashed text length should be 256 characters");
    }

    /**
     * Given a plain string when repeated tests return the same hash.
     */
    @Test
    @DisplayName("getSHA256 should return the same key for the same input")
    void givenAPlainStringWhenRepeatedTestsReturnTheSameHash() {
        String plain = "test";
        assertEquals(Util.getSHA256HashedString(plain),
                Util.getSHA256HashedString(plain),
                "Output should be the same for the same imputs");
    }

    /**
     * Given an empty string return a hashed key.
     */
    @Test
    @DisplayName("getSha256 should return a key when receiving an empty input")
    void givenAnEmptyStringReturnAHashedKey() {
        String emptyHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
        assertEquals(emptyHash, Util.getSHA256HashedString(""),
                "Empty plain string returns a 256b hash");
    }

    /**
     * Given a null string throw.
     */
    @Test
    @DisplayName("getSha256 should throw when receiving a null input")
    void givenANullStringThrow() {
        assertThrows(NullPointerException.class,
                () -> Util.getSHA256HashedString(null),
                "getSHA256 does not allow for null input");
    }

    /**
     * Given an empty student set return an empty json object.
     */
    @Test
    @DisplayName("getStudentAttributeJSONObject should return an empty JSONObject if the student set is empty")
    void givenAnEmptyStudentSetReturnAnEmptyJSONObject() {
        Set<Student> testSet = new HashSet<>();
        assertEquals(0, Util.getStudentAttributeJSONObject(testSet, "").size(),
                "JSONObject should be empty if the set is empty");
    }

    /**
     * Given a null student set throw a null pointer exception.
     */
    @Test
    @DisplayName("getStudentAttributeJSONObject should throw a NullPointerException if the set is null")
    void givenANullStudentSetThrowANullPointerException() {
        assertThrows(NullPointerException.class, () -> Util.getStudentAttributeJSONObject(null, ""),
                "Student set cannot be null");
    }

    /**
     * Given a set with null students throw a null pointer exception.
     */
    @Test
    @DisplayName("getStudentAttributeJSONObject should throw a NullPointerException if the set contains a null student")
    void givenASetWithNullStudentsThrowANullPointerException() {
        Set<Student> testSet = new HashSet<>();
        testSet.add(null);
        assertThrows(NullPointerException.class, () -> Util.getStudentAttributeJSONObject(testSet, ""),
                "Student set cannot contain null Students");
    }

    /**
     * Given a correct student set and an empty attribute return a correct json object.
     */
    @Test
    @DisplayName("getStudentAttributeJSONObject should return a correct JSONObject if the attribute is empty")
    void givenACorrectStudentSetAndAnEmptyAttributeReturnACorrectJSONObject() {
        Student testStudent = new Student("u99999999", "manual.laboratorio.rme.uca@gmail.com");
        Set<Student> testSet = new HashSet<>();
        testSet.add(testStudent);
        assertEquals(1, Util.getStudentAttributeJSONObject(testSet, "").size());
    }

    /**
     * Given a correct student set and a null attribute return a correct json object.
     */
    @Test
    @DisplayName("getStudentAttributeJSONObject should return a correct JSONObject if the attribute is null")
    void givenACorrectStudentSetAndANullAttributeReturnACorrectJSONObject() {
        Student testStudent = new Student("u99999999", "manual.laboratorio.rme.uca@gmail.com");
        Set<Student> testSet = new HashSet<>();
        testSet.add(testStudent);
        assertEquals(1, Util.getStudentAttributeJSONObject(testSet, null).size());
    }

    /**
     * Given a null student set when creating a string set throw an exception.
     */
    @Test
    @DisplayName("studentSetToStringSet should throw if the input set is null")
    void givenANullStudentSetWhenCreatingAStringSetThrowAnException() {
        assertThrows(NullPointerException.class, () -> Util.studentSetToStringSet(null),
                "Student set cannot be null");
    }

    /**
     * Given an empty student set return an empty string set.
     */
    @Test
    @DisplayName("studentSetToStringSet should return an empty String Set if the input set is empty")
    void givenAnEmptyStudentSetReturnAnEmptyStringSet() {
        Set<Student> testSet = new HashSet<>();
        assertEquals(0, Util.studentSetToStringSet(testSet).size(),
                "Cant make a String set out of an empty Student Set");
    }

    /**
     * Given a student set with null items throw an exception.
     */
    @Test
    @DisplayName("studentSetToStringSet should throw if input set contains null students")
    void givenAStudentSetWithNullItemsThrowAnException() {
        Set<Student> testSet = new HashSet<>();
        testSet.add(null);
        assertThrows(NullPointerException.class, () -> Util.studentSetToStringSet(testSet),
                "Input Student set cannot have null items");
    }

    /**
     * Given the first student set is null when getting an unique set throw a null pointer exception.
     */
    @Test
    @DisplayName("getUniqueStudentSet should throw a NPE if any of the sets is null")
    void givenTheFirstStudentSetIsNullWhenGettingAnUniqueSetThrowANullPointerException() {
        Set<Student> set = new HashSet<>();
        assertThrows(NullPointerException.class, () -> Util.getUniqueStudentSet(null, set),
                "First student set cannot be null");

    }

    /**
     * Given the second student set is null when getting an unique set throw a null pointer exception.
     */
    @Test
    @DisplayName("getUniqueStudentSet should throw a NPE if any of the sets is null")
    void givenTheSecondStudentSetIsNullWhenGettingAnUniqueSetThrowANullPointerException() {
        Set<Student> set = new HashSet<>();
        assertThrows(NullPointerException.class, () -> Util.getUniqueStudentSet(set, null),
                "Second student set cannot be null");
    }

    /**
     * Given any two student sets with null objects when getting an unique set throw a null pointer exception.
     */
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

    /**
     * Given the first student set is null when getting an intersection set throw a null pointer exception.
     */
    @Test
    @DisplayName("getUniqueStudentSet should throw a NPE if any of the sets is null")
    void givenTheFirstStudentSetIsNullWhenGettingAnIntersectionSetThrowANullPointerException() {
        Set<Student> set = new HashSet<>();
        assertThrows(NullPointerException.class, () -> Util.getIntersectionOfStudentSets(null, set),
                "First student set cannot be null");
    }

    /**
     * Given the second student set is null when getting an intersection set throw a null pointer exception.
     */
    @Test
    @DisplayName("getUniqueStudentSet should throw a NPE if any of the sets is null")
    void givenTheSecondStudentSetIsNullWhenGettingAnIntersectionSetThrowANullPointerException() {
        Set<Student> set = new HashSet<>();
        assertThrows(NullPointerException.class, () -> Util.getIntersectionOfStudentSets(set, null),
                "Second student set cannot be null");
    }

    /**
     * Given any two student sets with null objects when getting an intersection set throw a null pointer exception.
     */
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
