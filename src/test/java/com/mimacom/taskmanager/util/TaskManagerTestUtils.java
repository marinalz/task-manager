package com.mimacom.taskmanager.util;

import com.mimacom.taskmanager.model.Task;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@UtilityClass
public class TaskManagerTestUtils {

    public List<Task> mockTaskList(){
        final Task task1 = Task.builder().id(1L).title("title1").description("description1").createBy("test1@user.com")
                .finished(true).createDate(LocalDateTime.now()).build();
        final Task task2 = Task.builder().id(1L).title("title2").description("description2").createBy("test2@user.com")
                .finished(false).createDate(LocalDateTime.now()).build();

        final List<Task> taskList = new ArrayList<>();
        taskList.add(task1);
        taskList.add(task2);

        return taskList;
    }

    public Task mockTask(){
        return Task.builder().id(1L).title("title1").description("description1").createBy("test1@user.com")
                .finished(false).build();
    }

    public void checkTask(final Task task){
        assertNotNull(task);
        assertEquals("1", String.valueOf(task.getId()));
        assertEquals("title1", task.getTitle());
        assertEquals("description1", task.getDescription());
        assertEquals("test1@user.com", task.getCreateBy());
    }

    public void checkTaskList(final List<Task> taskList){
        assertNotNull(taskList);
        assertEquals(2, taskList.size());
    }
}
