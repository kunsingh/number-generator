package com.exercise.task;

import com.exercise.models.GenerateCriteria;
import com.exercise.utils.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

public class GenerateTask implements Callable<String> {

    private String id;
    private GenerateCriteria criteria;
    private String filePathToWrite;

    public GenerateTask(final String id, final GenerateCriteria criteria, final String filePathToWrite) {
        this.id = id;
        this.criteria = criteria;
        this.filePathToWrite = filePathToWrite;
    }

    @Override
    public String call() throws Exception {
        FileUtil.writeToFile(generate(), filePathToWrite + id + "_output.txt");
        return "SUCCESS";
    }

    public String generate() {
        final List<Integer> numbers = new ArrayList<>();
        for (int i = criteria.getGoal(); i > 0; i = i - criteria.getStep()) {
            numbers.add(i);
            //if last no is not 0
            if (i <= criteria.getStep()) {
                i=0;
                numbers.add(0);
            }
        }
        return numbers.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

}
