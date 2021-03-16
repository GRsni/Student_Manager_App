package uca.esi.dni.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
