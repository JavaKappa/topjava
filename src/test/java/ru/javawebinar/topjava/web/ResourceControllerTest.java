package ru.javawebinar.topjava.web;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Капу пк
 * 29.03.2020
 */
public class ResourceControllerTest extends AbstractControllerTest {
    @Test
    void getStyleCss() throws Exception {
        perform(MockMvcRequestBuilders.get("/resources/css/style.css"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.content().contentType("text/css"));
    }
}
