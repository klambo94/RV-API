package com.lamb.kendra.rvapi.MoveCheckList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class MoveCheckListController {

    @Autowired
    private MoveCheckListService moveCheckListService;

    @RequestMapping(method = RequestMethod.GET, value = "/moveLocations")
    public ModelAndView getCheckList() {
        List<Task> tasks = getTasksInOrder();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("moveLocations");
        modelAndView.addObject("tasks", tasks);
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/addTask")
    public ModelAndView addTask(@RequestParam String description) {
        ModelAndView modelAndView = new ModelAndView();
        Task task = new Task();
        //Add to the end of the list
        List<Task> tasks = getTasksInOrder();
        int taskSize = tasks.size();

        int orderNum;
        if(taskSize == 0) {
            orderNum = taskSize;
        } else {
            Task lastTask = tasks.get(taskSize - 1);
            orderNum = lastTask.getOrderNum() + 1;
        }
        task.setOrderNum(orderNum);
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

    @RequestMapping(method = RequestMethod.POST, value ="/moveLocations/updateTaskOrder")
    public ModelAndView updateTaskOrder(@RequestBody List<Task> tasks) {
        ModelAndView modelAndView = new ModelAndView();
        List<Task> db_tasks = getTasksInOrder();
        for(Task reqTask : tasks) {
            Task matchingDbTask = db_tasks.stream().filter(task -> task.getId().compareTo(reqTask.getId()) == 0).findFirst().orElse(null);

            if(matchingDbTask != null) {
                matchingDbTask.setOrderNum(reqTask.getOrderNum());
                moveCheckListService.updateTask(matchingDbTask);
            }
        }

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
        resetTaskOrderNums();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/moveLocations");
        return modelAndView;
    }

    private List<Task> getTasksInOrder() {
        List<Task> tasks = new ArrayList<>();
        moveCheckListService.getTasks().forEach(tasks::add);
        tasks.sort(Comparator.comparing(Task::getOrderNum));
        return tasks;
    }

    private void resetTaskOrderNums() {
        List<Task> tasks = getTasksInOrder();

        for(int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            task.setOrderNum(i);
            moveCheckListService.updateTask(task);
        }
    }
}
