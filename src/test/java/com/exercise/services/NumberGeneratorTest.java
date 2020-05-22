package com.exercise.services;

import com.exercise.exceptions.TaskNotFoundException;
import com.exercise.models.GenerateCriteria;
import com.exercise.utils.FileUtilTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.util.Assert;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Objects;

@SpringBootTest
@Profile("test")
public class NumberGeneratorTest {

    @Autowired
    private DescendingOrderNumberGenerator numberGenerator;

    private String fileLocation;

    @BeforeEach
    public void setup() throws Exception{
        final ClassLoader classLoader = FileUtilTest.class.getClassLoader();
        final String file = Objects.requireNonNull(classLoader.getResource("testdata")).getPath();
        fileLocation = file + "/";
        Field field = numberGenerator.getClass().getDeclaredField("fileLocation");
        field.setAccessible(true);
        field.set(numberGenerator, fileLocation);
    }

    @Test
    public void shouldBeAbleToSubmitNumberGenerateTaskForGivenCriteria() {
        final String taskId = numberGenerator.generateNumber(new GenerateCriteria(10, 2));
        Assert.isTrue(taskId != null, "generated taskId is null");
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNullCriteria() {
        Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> numberGenerator.generateNumber(null)
        );
        Assert.isTrue(exception.getMessage().contains("Argument 'GenerateCriteria' may not be null"), "Wrong message!");
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForNegativeGoal() {
        Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> numberGenerator.generateNumber(new GenerateCriteria(-10, 2))
        );
        Assert.isTrue(exception.getMessage().contains("Argument 'goal' may not be negative"), "Wrong message!");
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionForStepGreaterThanGoal() {
        Exception exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> numberGenerator.generateNumber(new GenerateCriteria(10, 12))
        );
        Assert.isTrue(exception.getMessage().contains("Step can't be greater than goal"), "Wrong message!");
    }

    @Test
    public void shouldBeAbleToGetStatusForTaskForGivenTaskId() throws Exception {
        final String taskId = numberGenerator.generateNumber(new GenerateCriteria(10, 2));
        String status = numberGenerator.getStatusForTask(taskId);
        Assert.isTrue(status.equals("IN_PROGRESS"), "Status should be in progress!");
        Thread.sleep(2000);
        status = numberGenerator.getStatusForTask(taskId);
        Assert.isTrue(status.equals("SUCCESS"), "Status should be success!");
    }

    @Test
    public void shouldThrowTaskNotFoundExceptionForGivenWrongTaskId() {
        Exception exception = Assertions.assertThrows(
                TaskNotFoundException.class,
                () -> numberGenerator.getStatusForTask("wrongtaskid")
        );
        Assert.isTrue(exception.getMessage().contains("No task submitted with id: wrongtaskid"), "Wrong message!");
    }

    @Test
    public void shouldBeAbleToGetTheNumberListForGivenTaskId() throws Exception {
        final String taskId = numberGenerator.generateNumber(new GenerateCriteria(10, 2));
        Thread.sleep(2000);
        final String result = numberGenerator.getNumberList(taskId);
        Assert.isTrue(result.equals("10,8,6,4,2,0"), "Number list should be '10,8,6,4,2,0'");
    }

    @AfterEach
    private void cleanup(){
        final File[] files = new File(fileLocation).listFiles();
        for(File file:files) {
            file.delete();
        }
    }

}
