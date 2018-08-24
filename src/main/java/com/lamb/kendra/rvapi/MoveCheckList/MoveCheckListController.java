package com.lamb.kendra.rvapi.MoveCheckList;

import io.swagger.models.Model;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ModelAndView addItemToList(@RequestParam String name, @RequestParam String description) {

        ModelAndView modelAndView = new ModelAndView();
        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        moveCheckListService.addItem(item);
        modelAndView.setViewName("redirect:/moveLocations");

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/editItem")
    public ModelAndView editItem(@RequestBody Item item, @RequestParam String name, @RequestParam String description) {
        moveCheckListService.updateItem(item);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/moveLocations");

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/deleteItem")
    public ModelAndView deleteItem(@RequestParam String name) {
        moveCheckListService.deleteItem(name);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/moveLocations");
        return modelAndView;
    }
}
