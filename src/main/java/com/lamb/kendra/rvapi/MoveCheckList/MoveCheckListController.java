package com.lamb.kendra.rvapi.MoveCheckList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class MoveCheckListController {

    //TODO: handle bad requests once View is up and running
    @Autowired
    private MoveCheckListService moveCheckListService;

    @RequestMapping(method = RequestMethod.GET, value = "/moveLocations")
    public ModelAndView getCheckList() {
        List<Task> tasks = new ArrayList<>();
        moveCheckListService.getTasks().forEach(tasks::add);
        ModelAndView modelAndView = new ModelAndView();
        tasks.sort(Comparator.comparing(Task::getDescription));
        modelAndView.setViewName("moveLocations");
        modelAndView.addObject("tasks", tasks);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/addTask")
    public ModelAndView addTask(@RequestParam String description) {

        ModelAndView modelAndView = new ModelAndView();
        Task task = new Task();
        task.setDescription(description);
        moveCheckListService.addTask(task);
        modelAndView.setViewName("redirect:/moveLocations");

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/editTask")
    public ModelAndView editTask(@RequestParam Long taskId, @RequestParam String description) {
        Task task = moveCheckListService.findTaskById(taskId);
        if(task != null) {
            task.setDescription(description);
            moveCheckListService.updateTask(task);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/moveLocations");

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/editCompleteStatus")
    public ModelAndView editCompleteStatus(@RequestBody Task reqTask) {
        ModelAndView modelAndView = new ModelAndView();
        if(reqTask != null && reqTask.getId() != null) {
            Long taskId = reqTask.getId();
            boolean isCompleted = reqTask.isCompleted();
            Task task = moveCheckListService.findTaskById(taskId);
            if (task != null) {
                task.setCompleted(isCompleted);
                moveCheckListService.updateTask(task);
                modelAndView.setViewName("redirect:/moveLocations");
            }
        }
        return modelAndView;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/deleteTask")
    public ModelAndView deleteTask(@RequestParam Long taskId) {
        moveCheckListService.deleteTask(taskId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/moveLocations");
        return modelAndView;
    }
}
