package com.mimacom.taskmanager.service.impl;

import com.mimacom.taskmanager.dao.TaskManagerDAO;
import com.mimacom.taskmanager.model.Task;
import com.mimacom.taskmanager.service.TaskManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class TaskManagerServiceImpl implements TaskManagerService {

    @Autowired
    protected TaskManagerDAO taskManagerDAO;

    @Override
    public Task getTaskById(final Long id) {
        log.info("Getting task by id: {}", id);
        return findById(id);
    }

    @Override
    public List<Task> getAllTasks() {
        log.info("Getting all tasks");
        return (List<Task>) taskManagerDAO.findAll();
    }

    @Override
    public Task createTask(final Task task) {
        log.info("Creating new task: {}", task.getTitle());
        try{
            return taskManagerDAO.save(task);
        } catch (Exception e) {
            log.info("Error creating new task");
            throw new ValidationException("Error creating new task", e);
        }
    }

    @Override
    public Task updateTask(final Task task)  {
        log.info("Updating task with id: {}", task.getId());
        findById(task.getId());
        try{
            task.setUpdateDate(LocalDateTime.now());
            return taskManagerDAO.save(task);
        } catch (Exception e) {
            log.info("Error updating task");
            throw new ValidationException("Error updating task", e);
        }
    }

    @Override
    public Task setTaskAsFinished(final Long id) {
        log.info("Setting task with id: {} as finished", id);
        final Task task = findById(id);
        if(!task.getFinished()){
            task.setFinished(true);
            task.setUpdateDate(LocalDateTime.now());
            return taskManagerDAO.save(task);
        } else{
            throw new ValidationException("Task already finished");
        }
    }

    @Override
    public void removeTask(final Long id) {
        log.info("Removing task with id: {}", id);
        findById(id);
        taskManagerDAO.deleteById(id);
    }

    private Task findById(final Long taskId){
        return taskManagerDAO.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
    }
}
