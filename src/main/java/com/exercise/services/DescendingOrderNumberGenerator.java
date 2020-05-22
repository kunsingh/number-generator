package com.exercise.services;

import com.exercise.exceptions.TaskNotFoundException;
import com.exercise.models.GenerateCriteria;
import com.exercise.task.GenerateTask;
import com.exercise.utils.Assert;
import com.exercise.utils.Constants;
import com.exercise.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component("descendingOrderNumberGenerator")
public class DescendingOrderNumberGenerator implements NumberGenerator {

    private static final Logger LOGGER = LoggerFactory.getLogger(DescendingOrderNumberGenerator.class);

    final private Map<String, Future<String>> futureByTaskId = new ConcurrentHashMap<>();

    private ExecutorService executor;


    @Value("${fileLocation}")
    private String fileLocation;

    @Value("${noOfThread}")
    private int noOfThread;

    @PostConstruct
    public void init(){
        executor = Executors.newFixedThreadPool(noOfThread);
        if(!fileLocation.endsWith("/")){
            fileLocation = fileLocation + "/";
        }
    }

    @Override
    public String  generateNumber(final GenerateCriteria criteria) {
        Assert.requireNonNull(criteria, "GenerateCriteria");
        Assert.requirePositiveInteger(criteria.getGoal(), "goal");
        Assert.requirePositiveInteger(criteria.getStep(), "step");
        Assert.goalShouldBeGreaterThanStep(criteria.getGoal(), criteria.getStep());
        LOGGER.info("File location is set to {}", fileLocation);
        final String taskId = UUID.randomUUID().toString();
        final GenerateTask generateTask = new GenerateTask(taskId, criteria, fileLocation);
        LOGGER.info("Created a task to generate numbers with goal:{} and step:{}", criteria.getGoal(), criteria.getStep());
        final Future<String> future = executor.submit(generateTask);
        futureByTaskId.putIfAbsent(taskId, future);
        return taskId;
    }

    @Override
    public String getStatusForTask(final String taskId) throws TaskNotFoundException {
        Assert.requireNonNull(taskId, "taskId");
        if(FileUtil.isExist(fileLocation + taskId + "_output.txt")){
            futureByTaskId.remove(taskId);
            return Constants.SUCCESS;
        }
        synchronized (taskId.intern()) {
            if (futureByTaskId.containsKey(taskId)) {
                final Future<String> future = futureByTaskId.get(taskId);
                if (future.isDone()) {
                    try {
                        future.get();
                        futureByTaskId.remove(taskId);
                        return Constants.SUCCESS;
                    } catch (final InterruptedException | ExecutionException ex) {
                        LOGGER.error("Exception occurred while generating numbers for taskId:{} Exception is: {}", taskId, ex.getMessage());
                        return Constants.ERROR;
                    }
                } else {
                    return Constants.IN_PROGRESS;
                }
            }
        }
        LOGGER.error("No task submitted with taskId:{}", taskId);
        throw new TaskNotFoundException("No task submitted with id: "+taskId);
    }

    @Override
    public String getNumberList(final String taskId) throws IOException {
        Assert.requireNonNull(taskId, "taskId");
        return FileUtil.readFromFile(fileLocation + taskId + "_output.txt");
    }
}
