package com.mimacom.taskmanager.service;

import com.mimacom.taskmanager.dao.TaskManagerDAO;
import com.mimacom.taskmanager.model.Task;
import com.mimacom.taskmanager.service.impl.TaskManagerServiceImpl;
import com.mimacom.taskmanager.util.TaskManagerTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class TaskManagerServiceUnitTest {

    @InjectMocks
    private TaskManagerServiceImpl taskManagerService;

    @Mock
    private TaskManagerDAO taskManagerDAO;

    private Task mockTask;
    private List<Task> mockTaskList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockTask = TaskManagerTestUtils.mockTask();
        mockTaskList = TaskManagerTestUtils.mockTaskList();
    }

    @Test
    public void getAllTasksTest() {
        when(taskManagerDAO.findAll()).thenReturn(mockTaskList);
        final List<Task> taskList = taskManagerService.getAllTasks();
        TaskManagerTestUtils.checkTaskList(taskList);
    }

    @Test
    public void getAllTasksEmptyListTest() {
        when(taskManagerDAO.findAll()).thenReturn(new ArrayList<>());
        final List<Task> taskList = taskManagerService.getAllTasks();
        assertEquals(0, taskList.size());
    }

    @Test
    public void getTaskByIdTest() {
        when(taskManagerDAO.findById(Mockito.anyLong())).thenReturn(Optional.of(mockTask));
        final Task task = taskManagerService.getTaskById(1l);
        TaskManagerTestUtils.checkTask(task);
    }

    @Test
    public void getTaskByIdNotFoundTest() {
        when(taskManagerDAO.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        try {
            Task task = taskManagerService.getTaskById(1l);
        } catch (EntityNotFoundException e) {
            assertEquals("Task not found", e.getMessage());
        }
    }

    @Test
    public void createTaskTest() {
        when(taskManagerDAO.save(Mockito.any())).thenReturn(mockTask);
        final Task task = taskManagerService.createTask(mockTask);
        TaskManagerTestUtils.checkTask(task);
    }

    @Test
    public void createTaskBadRequestTest() {
        when(taskManagerDAO.save(Mockito.any())).thenThrow();
        try {
            final Task task = taskManagerService.createTask(mockTask);
        } catch (Exception e){
            assertEquals("Error creating new task", e.getMessage());
        }
    }

    @Test
    public void updateTaskTest() {
        when(taskManagerDAO.findById(Mockito.anyLong())).thenReturn(Optional.of(mockTask));
        when(taskManagerDAO.save(Mockito.any())).thenReturn(mockTask);
        mockTask.setTitle("Updated Title");
        final Task task = taskManagerService.updateTask(mockTask);
        assertNotNull(task);
        assertEquals("1", String.valueOf(task.getId()));
        assertEquals("Updated Title", task.getTitle());
    }

    @Test
    public void updateTaskNotFoundTest() {
        when(taskManagerDAO.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        try {
            final Task task = taskManagerService.updateTask(mockTask);
        } catch (EntityNotFoundException ex) {
            assertEquals("Task not found", ex.getMessage());
        }
    }

    @Test
    public void setTaskAsFinishedTest() {
        when(taskManagerDAO.findById(Mockito.anyLong())).thenReturn(Optional.of(mockTask));
        when(taskManagerDAO.save(Mockito.any())).thenReturn(mockTask);
        final Task task = taskManagerService.setTaskAsFinished(1L);
        assertNotNull(task);
        assertEquals(Boolean.TRUE, task.getFinished());
    }

    @Test
    public void setTaskAsFinishedAlreadyFinishedTest() {
        when(taskManagerDAO.findById(Mockito.anyLong())).thenReturn(Optional.of(mockTask));
        when(taskManagerService.setTaskAsFinished(Mockito.anyLong())).thenThrow(ValidationException.class);
        try {
            final Task task = taskManagerService.setTaskAsFinished(1L);
        } catch (ValidationException ex) {
            assertEquals("Task already finished", ex.getMessage());
        }
    }

    @Test
    public void deleteTaskTest() {
        when(taskManagerDAO.findById(Mockito.anyLong())).thenReturn(Optional.of(mockTask));
        taskManagerService.removeTask(mockTask.getId());
    }

    @Test
    public void deleteTaskNotFoundTest() {
        when(taskManagerDAO.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        try {
            taskManagerService.removeTask(1L);
        } catch (EntityNotFoundException e) {
            assertEquals("Task not found", e.getMessage());
        }
    }
}
