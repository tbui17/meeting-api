package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;


class AppControllerTests extends BaseIntegrationTest {



    @Test
    void contextLoads() {
    }

    @Test
    void seedWorks() throws Exception {

        mockMvc.perform(post("/seed")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isCreated());
        assertThat(meetingRepository.count()).isGreaterThan(0);
        assertThat(userRepository.count()).isGreaterThan(0);
    }
}