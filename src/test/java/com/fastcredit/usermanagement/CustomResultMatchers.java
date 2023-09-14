package com.fastcredit.usermanagement;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class CustomResultMatchers {

    public static ResultMatcher jsonContentEquals(final String expectedJson) {
        return result -> {
            String actualJson = result.getResponse().getContentAsString();
            // Remove all whitespace from both expected and actual JSON
            String expectedJson2 = expectedJson.replaceAll("\\s", "");
            actualJson = actualJson.replaceAll("\\s", "");
            // Compare the two JSON strings
            if (!expectedJson2.equals(actualJson)) {
                throw new AssertionError("JSON content does not match. Expected: " + expectedJson2 + ", Actual: " + actualJson);
            }
        };
    }
}
