package com.exercise.controllers;

import com.exercise.models.GenerateCriteria;
import com.exercise.exceptions.TaskNotFoundException;
import com.exercise.models.TaskDetails;
import com.exercise.services.DescendingOrderNumberGenerator;
import com.exercise.services.NumberGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(tags = "Number Generator for given goal and step")
@RestController
@RequestMapping("/api")
public class NumberGeneratorController {

    @Autowired
    @Qualifier("descendingOrderNumberGenerator")
    private DescendingOrderNumberGenerator numberGenerator;

    @ApiOperation(value = "Submit a task to generate number for given goal generate criteria.", response = TaskDetails.class)
    @ApiResponses(value = {
            @ApiResponse(code = 202, message = "Accepted"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server error")
    })
    @CrossOrigin
    @PostMapping
    public ResponseEntity<TaskDetails> generate(@RequestBody GenerateCriteria criteria) {
        final TaskDetails taskDetails = new TaskDetails(numberGenerator.generateNumber(criteria));
        return new ResponseEntity<>(taskDetails, HttpStatus.ACCEPTED);
    }

    @ApiOperation(value = "Return the status for given taskId", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server error")
    })
    @CrossOrigin
    @GetMapping("/tasks/{taskId}/status")
    public String status(@PathVariable("taskId") final String taskId) throws TaskNotFoundException {
        return numberGenerator.getStatusForTask(taskId);
    }

    @ApiOperation(value = "Return the list of generated numbers", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 500, message = "Internal Server error")
    })
    @CrossOrigin
    @GetMapping("/tasks/{taskId}")
    public String numberList(@PathVariable("taskId") final String taskId, @RequestParam("action") final String action) throws IOException {
        return numberGenerator.getNumberList(taskId);
    }

}
