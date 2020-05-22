package com.exercise.controllers;

import com.exercise.models.GenerateCriteria;
import com.exercise.services.DescendingOrderNumberGenerator;
import com.exercise.utils.FileUtilTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(NumberGeneratorController.class)
@ComponentScan(basePackages = "com.exercise")
public class NumberGeneratorControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DescendingOrderNumberGenerator numberGenerator;

    @BeforeAll
    public static void setup(){
        final ClassLoader classLoader = FileUtilTest.class.getClassLoader();
        final String file = Objects.requireNonNull(classLoader.getResource("testdata")).getPath();
        System.setProperty("fileLocation", file+"/");
    }

    @Test
    public void validCallForSubmittingTaskShouldReturn_202() throws Exception{
        Mockito.when(numberGenerator.generateNumber(Mockito.any(GenerateCriteria.class))).thenReturn("testTaskId");
        this.mockMvc.perform(post("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"goal\": 10, \"step\": 2 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.task").value("testTaskId"));

    }

    @Test
    public void inValidCallForSubmittingTaskShouldReturn_400() throws Exception{
        Mockito.when(numberGenerator.generateNumber(Mockito.any(GenerateCriteria.class))).thenThrow(IllegalArgumentException.class);
        this.mockMvc.perform(post("/api")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"goal\": -10, \"step\": 2 }")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void validCallForSubmittingTaskShouldReturnStatus200AndResponseAsSuccess() throws Exception{
        Mockito.when(numberGenerator.getStatusForTask(Mockito.any(String.class))).thenReturn("SUCCESS");
        mockMvc.perform(get("/api/tasks/{taskId}/status", "taskId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("SUCCESS"));

    }

    @Test
    public void validCallForSubmittingTaskShouldReturnStatus200AndResponseAsInProgress() throws Exception{
        Mockito.when(numberGenerator.getStatusForTask(Mockito.any(String.class))).thenReturn("IN_PROGRESS");
        mockMvc.perform(get("/api/tasks/{taskId}/status", "taskId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("IN_PROGRESS"));

    }

    @Test
    public void validCallForSubmittingTaskShouldReturnStatus200AndResponseAsError() throws Exception{
        Mockito.when(numberGenerator.getStatusForTask(Mockito.any(String.class))).thenReturn("ERROR");
        mockMvc.perform(get("/api/tasks/{taskId}/status", "taskId")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("ERROR"));

    }

    @Test
    public void validCallForNumberListShouldReturnStatus200AndResponseListOfNumbers() throws Exception{
        Mockito.when(numberGenerator.getNumberList(Mockito.any(String.class))).thenReturn("10,8,6,,4,2,0");
        mockMvc.perform(get("/api/tasks/{taskId}", "taskId")
                .param("action", "get_numlist")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("10,8,6,,4,2,0"));

    }


}