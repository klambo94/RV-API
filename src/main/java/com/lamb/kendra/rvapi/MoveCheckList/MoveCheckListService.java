package com.lamb.kendra.rvapi.MoveCheckList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MoveCheckListService {

    @Autowired
    private MoveCheckListRepository moveCheckListRepository;

    public List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        moveCheckListRepository.findAll().forEach(tasks::add);
        return tasks;
    }

    public Task findTaskById(Long taskId) {
        return moveCheckListRepository.findById(taskId).orElse(null);
    }

    public void addTask(Task task) {
        moveCheckListRepository.save(task);
    }

    public void updateTask(Task task) {
        moveCheckListRepository.save(task);
    }

    public void deleteTask(Long id) {
        moveCheckListRepository.deleteById(id);
    }
}
