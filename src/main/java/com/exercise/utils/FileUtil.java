package com.exercise.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

    private FileUtil() {

    }

    public static void writeToFile(final String content, final String path) throws IOException {
        try {
            Files.write(Paths.get(path), content.getBytes());
        } catch (final IOException ex) {
            LOGGER.error("Error occurred while writing numbers: {} to the file. Exception is {}", content, ex);
            throw ex;
        }
    }

    public static String readFromFile(final String path) throws IOException {
        String result;
        try {
            result = new String(Files.readAllBytes(Paths.get(path)));
            LOGGER.info("content of file:{} is: {}", path, result);
        } catch (final IOException ex) {
            LOGGER.error("Error occurred while reading numbers from file: {}, Exception is {}", path, ex);
            throw ex;
        }
        return result;
    }

    public static boolean isExist(final String path) {
        return new File(path).exists();
    }


}
