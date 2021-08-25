package com.mimacom.taskmanager.controller;

import com.mimacom.taskmanager.model.Task;
import com.mimacom.taskmanager.service.TaskManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/mimacom")
public class TaskManagerController {

    @Autowired
    private TaskManagerService taskManagerService;

    @ResponseBody
    @GetMapping(value="task", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Task> getAllTask(){
        return taskManagerService.getAllTasks();
    }

    @GetMapping(value = "task/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Task getTaskById(@PathVariable(value = "id") String id){
        return taskManagerService.getTaskById(Long.parseLong(id));
    }

    @PostMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        return new ResponseEntity<>(taskManagerService.createTask(task), HttpStatus.OK);
    }

    @PutMapping(value = "/task", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> updateTask(@RequestBody Task task){
        return new ResponseEntity<>(taskManagerService.updateTask(task), HttpStatus.OK);
    }

    @PutMapping(value = "/task/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> setTaskAsFinished(@PathVariable(value = "id") String id){
        return new ResponseEntity<>(taskManagerService.setTaskAsFinished(Long.parseLong(id)), HttpStatus.OK);
    }

    @DeleteMapping(value = "/task/{id}")
    public boolean deleteTask(@PathVariable(value = "id") String id){
        taskManagerService.removeTask(Long.parseLong(id));
        return true;
    }

}
