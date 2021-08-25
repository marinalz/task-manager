package com.mimacom.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mimacom.taskmanager.model.Task;
import com.mimacom.taskmanager.service.TaskManagerService;
import com.mimacom.taskmanager.util.TaskManagerTestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TaskManagerControllerITest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskManagerService taskService;

    private ObjectMapper objectMapper;
    private Task mockTask;
    private List<Task> mockTaskList;

    private static final String URL = "/api/mimacom/task";

    @Before
    public void setup(){
        objectMapper = new ObjectMapper();
        mockTask = TaskManagerTestUtils.mockTask();
        mockTaskList = TaskManagerTestUtils.mockTaskList();
    }

    @Test
    public void getAllTasksTest() throws Exception {
        when(taskService.getAllTasks()).thenReturn(mockTaskList);
        mockMvc.perform(get(URL))
                .andExpect(status().isOk());
    }

    @Test
    public void getTaskByIdNotFoundTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(taskService).getTaskById(Mockito.any());
        mockMvc.perform(get(URL.concat("/1")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getTaskByIdTest() throws Exception {
        when(taskService.getTaskById(Mockito.any())).thenReturn(mockTask);
        mockMvc.perform(get(URL.concat("/1")))
                .andExpect(status().isOk());
    }

    @Test
    public void createTaskTest() throws Exception {
        when(taskService.createTask(Mockito.any())).thenReturn(mockTask);
        mockMvc.perform(post(URL)
                .content(objectMapper.writeValueAsString(mockTask))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void createTaskBadRequestTest() {
        doThrow(ValidationException.class).when(taskService).createTask(Mockito.any());
        try {
            mockMvc.perform(post(URL)
                    .content(objectMapper.writeValueAsString(mockTask))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
        }
    }

    @Test
    public void updateTaskTest() throws Exception {
        when(taskService.getTaskById(Mockito.anyLong())).thenReturn(mockTask);
        when(taskService.updateTask(Mockito.any())).thenReturn(mockTask);
        mockMvc.perform(put(URL)
                .content(objectMapper.writeValueAsString(mockTask))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateTaskNotFoundTest() {
        doThrow(EntityNotFoundException.class).when(taskService).updateTask(Mockito.any());
        try {
            mockMvc.perform(put(URL)
                    .content(objectMapper.writeValueAsString(mockTask))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        } catch (Exception e) {
        }
    }

    @Test
    public void setTaskAsFinishedTest() throws Exception {
        when(taskService.getTaskById(Mockito.anyLong())).thenReturn(mockTask);
        mockTask.setFinished(true);
        when(taskService.setTaskAsFinished(Mockito.anyLong())).thenReturn(mockTask);
        mockMvc.perform(put(URL.concat("/1"))
                .content(objectMapper.writeValueAsString(mockTask))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void setTaskAsFinishedAlreadyFinishedTest() throws Exception {
        doThrow(ValidationException.class).when(taskService).setTaskAsFinished(Mockito.anyLong());
        try {
            mockMvc.perform(put(URL.concat("/1"))
                    .content(objectMapper.writeValueAsString(mockTask))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest());
        } catch (Exception e) {
        }
    }

    @Test
    public void removeTaskTest() throws Exception {
        doNothing().when(taskService).removeTask(Mockito.anyLong());
        mockMvc.perform(delete(URL.concat("/1")))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    public void removeTaskNotFoundTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(taskService).removeTask(Mockito.anyLong());
        mockMvc.perform(delete(URL.concat("/1")))
                .andExpect(status().isNotFound());
    }
}
