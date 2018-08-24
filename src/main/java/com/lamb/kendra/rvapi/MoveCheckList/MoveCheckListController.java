package com.lamb.kendra.rvapi.MoveCheckList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MoveCheckListController {

    //TODO: handle bad requests once View is up and running
    @Autowired
    private MoveCheckListService moveCheckListService;

    @RequestMapping(method = RequestMethod.GET, value = "/moveLocations")
    public ModelAndView getCheckList() {
        List<Item> items = new ArrayList<>();
        moveCheckListService.getAllItems().forEach(items::add);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("moveLocations");
        modelAndView.addObject("items", items);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/addItem")
    public ModelAndView addItemToList(@RequestParam String description) {

        ModelAndView modelAndView = new ModelAndView();
        Item item = new Item();
        item.setDescription(description);
        moveCheckListService.addItem(item);
        modelAndView.setViewName("redirect:/moveLocations");

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/editItem")
    public ModelAndView editItem(@RequestParam Long taskId, @RequestParam String description) {
        Item item = moveCheckListService.findItemById(taskId);
        if(item != null) {
            item.setDescription(description);
            moveCheckListService.updateItem(item);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/moveLocations");

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/deleteItem")
    public ModelAndView deleteItem(@RequestParam Long taskId) {
        moveCheckListService.deleteItem(taskId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/moveLocations");
        return modelAndView;
    }
}
