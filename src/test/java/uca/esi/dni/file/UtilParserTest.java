package uca.esi.dni.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UtilParserTest {

    @Test
    @DisplayName("ExtractID should return an ID from a String with a valid ID")
    public void givenAStringWhenIDIsContainedThenReturnAValidID(){
        String test="u99999999";
        assertEquals(UtilParser.extractId(test), "u99999999",
                "ID returned should be u99999999");
    }

    @Test
    @DisplayName("ExtractID should return the empty string from a String with no valid IDs")
    public void givenAStringWhenNoIDIsContainedThenReturnEmptyString(){
        String test="aaaaaaaa";
        assertEquals(UtilParser.extractId(test), "",
                "String returned should be empty");
    }

    @Test
    @DisplayName("ExtractID should return the empty string from a String with partial IDs")
    public void givenAStringWhenPartialIDsAreContainedThenReturnEmptyString(){
        String test="123343242u343";
        assertEquals(UtilParser.extractId(test), "",
                "String returned should be empty");
    }

    @Test
    @DisplayName("ExtractID should return the first valid ID from a String with multiple concatenated IDs")
    public void givenAStringWhenValidAndInvalidIDsAreContainedThenReturnTheFirstValidID(){
        String test="u12345678u123";
        assertEquals(UtilParser.extractId(test), "u12345678",
                "ID returned should be u12345678");
    }

    @Test
    @DisplayName("ExtractID should return the first valid ID from a string with multiple concatenated valid IDs")
    public void givenAStringWhenValidIDsAreContainedThenReturnTheFirstValidID(){
        String test="u11111111u22222222";
        assertEquals(UtilParser.extractId(test), "u11111111",
                "ID returned should be u11111111");
    }

    @Test
    @DisplayName("checkFileExtension should return true when the extension matches the file extension")
    public void givenAFileAndMatchingExtensionReturnTrue(){
        String file="test.txt";
        assertTrue(UtilParser.checkFileExtension(file, "txt"),
                "Should return true");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the file provided has no extension")
    public void givenAFileWithNoExtensionReturnFalse(){
        String file="file";
        assertFalse(UtilParser.checkFileExtension(file, "txt"),
                "Should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when no file is provided")
    public void givenAnEmptyFileReturnFalse(){
        assertFalse(UtilParser.checkFileExtension("", "txt"),
                "Test should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the extension doesn't match the file extension")
    public void givenAnIncorrectExtensionReturnFalse(){
        String file="test.txt";
        assertFalse(UtilParser.checkFileExtension(file, "zip"),
                "Test should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the extension provided is empty")
    public void givenAnEmptyExtensionReturnFalse(){
        String file="file.txt";
        assertFalse(UtilParser.checkFileExtension(file, ""),
                "Test should return false");
    }

    @Test
    @DisplayName("checkFileExtension should return false when the extension provided is null")
    public void givenANullExtensionReturnFalse(){
        String file="file.txt";
        assertFalse(UtilParser.checkFileExtension(file, null),
                "Test should return false");
    }

}
