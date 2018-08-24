package com.lamb.kendra.rvapi.MoveCheckList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MoveCheckListService {

    @Autowired
    private MoveCheckListRepository moveCheckListRepository;

    public List<Item> getAllItems() {
        List<Item> items = new ArrayList<>();
        moveCheckListRepository.findAll().forEach(items::add);
        return items;
    }

    public Item findItemById(Long taskId) {
        return moveCheckListRepository.findById(taskId).orElse(null);
    }

    public void addItem(Item item) {
        moveCheckListRepository.save(item);
    }

    public void updateItem(Item item) {
        moveCheckListRepository.save(item);
    }

    public void deleteItem(Long id) {
        moveCheckListRepository.deleteById(id);
    }
}
