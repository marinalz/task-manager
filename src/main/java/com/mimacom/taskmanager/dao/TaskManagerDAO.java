package com.mimacom.taskmanager.dao;

import com.mimacom.taskmanager.model.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskManagerDAO extends CrudRepository<Task, Long> {

}
