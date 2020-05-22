package com.exercise.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.File;
import java.util.Objects;

@SpringBootTest
public class FileUtilTest {

    @Value("${fileLocation}")
    private String fileLocation;

    @BeforeAll
    public static void setup(){
        final ClassLoader classLoader = FileUtilTest.class.getClassLoader();
        final String file = Objects.requireNonNull(classLoader.getResource("testdata")).getPath();
        System.setProperty("fileLocation", file+"/");
    }

    @Test
    public void shouldWriteAndReadContentToGivenPath() throws Exception{
        FileUtil.writeToFile("Test Content", fileLocation + "testOutput.txt");
        Assert.isTrue(FileUtil.isExist(fileLocation + "testOutput.txt"), "File doesn't exist!");
        final String content = FileUtil.readFromFile(fileLocation + "testOutput.txt");
        Assert.isTrue(content != null, "content should not be null!");
        Assert.isTrue(content.equals("Test Content"), "content is not same as written!");
    }

    @AfterEach
    public void cleanup(){
        final File file = new File(fileLocation + "testOutput.txt");
        file.delete();
    }
}
