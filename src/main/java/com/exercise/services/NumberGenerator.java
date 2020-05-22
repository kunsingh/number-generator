package com.exercise.services;

import com.exercise.exceptions.TaskNotFoundException;
import com.exercise.models.GenerateCriteria;

import java.io.IOException;

public interface NumberGenerator {

    /**
     * Generate number for given goal and step
     * @param criteria for goal snd step
     * @return task id
     */
    String generateNumber(final GenerateCriteria criteria);

    /**
     * Return current status of given task
     * @param taskId id of submitted task
     * @return status of the submitted task
     * @throws TaskNotFoundException
     */
    String getStatusForTask(final String taskId) throws TaskNotFoundException;

    /**
     * Return comma separated list of numbers for given taskId
     * @param taskId id of submitted task
     * @return comma separated list of numbers
     * @throws IOException
     */
    String getNumberList(final String taskId) throws IOException;
}
