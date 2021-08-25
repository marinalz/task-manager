package com.mimacom.taskmanager.service;

import com.mimacom.taskmanager.model.Task;

import java.util.List;

public interface TaskManagerService {

    Task getTaskById(Long taskId);
    List<Task> getAllTasks();
    Task createTask(Task task);
    Task updateTask(Task task);
    Task setTaskAsFinished(Long taskId);
    void removeTask(Long taskId);
}
