package com.lamb.kendra.rvapi.MoveCheckList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MoveCheckListController {

    //TODO: handle bad requests once View is up and running
    @Autowired
    private MoveCheckListService moveCheckListService;

    @RequestMapping(method = RequestMethod.GET, value = "/moveLocations")
    public List<Item> getCheckList() {
        return moveCheckListService.getAllItems();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations")
    public void addItemToList(@RequestBody Item item) {
        moveCheckListService.addItem(item);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/moveLocations")
    public void updateItem(@RequestBody Item item) {
        moveCheckListService.updateItem(item);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/moveLocations")
    public void deleteItem(@RequestBody String name) {
        moveCheckListService.deleteItem(name);
    }
}
