package com.lamb.kendra.rvapi.MoveCheckList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@RestController
public class MoveCheckListController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private CategoryService categoryService;

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

        taskService.addTask(task);
        modelAndView.setViewName("redirect:/moveLocations");

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/moveLocations/editTask")
    public ModelAndView editTask(@RequestParam Long taskId) {
        ModelAndView modelAndView = new ModelAndView();
        Task task = taskService.findTaskById(taskId);
        modelAndView.addObject("task", task);
        modelAndView.setViewName("/editTask");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/addCategory")
    public ModelAndView addCategory(@RequestParam String categoryName, @RequestParam Long taskId) {
        ModelAndView modelAndView = new ModelAndView();
        Task task = taskService.findTaskById(taskId);
        Category category = categoryService.findCategoryByName(categoryName);

        if (category == null) {
            category = new Category();
            category.setCategoryName(categoryName);
        }

        Set<Task> categoryTasks = category.getTasks();
        if (categoryTasks == null) {
            categoryTasks = new HashSet<>();
        }

        categoryTasks.add(task);
        category.setTasks(categoryTasks);
        categoryService.addCategory(category);

        Set<Category>categories = task.getCategories();
        categories.add(category);
        task.setCategories(categories);
        taskService.updateTask(task);
        modelAndView.addObject("task", task);
        modelAndView.setViewName("/editTask");
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value ="/moveLocations/deleteCategory")
    public ModelAndView deleteCategoryFromTask(@RequestParam Long taskId, @RequestParam Long categoryId) {
        ModelAndView modelAndView = new ModelAndView();

        if(taskId != null && categoryId != null) {
            Task task = taskService.findTaskById(taskId);
            Category category = categoryService.findCategoryById(categoryId);

            //Get the categories that belong to the specific task
            Set<Category> taskCategories = task.getCategories();

            //Get the tasks that belong to the specific category we want to delete
            Set<Task> categoryTasks = category.getTasks();

            //Ensure the bi-directional relationship is present
            if(taskCategories.contains(category) && categoryTasks.contains(task)) {
                //Remove the category from the specific task
                taskCategories.remove(category);

                //remove the task from the category
                categoryTasks.remove(task);

                //Update the tasks belonging to the categories
                category.setTasks(categoryTasks);
                categoryService.updateCategory(category);

                //Update the categories belonging to the task
                task.setCategories(taskCategories);
                taskService.updateTask(task);
            }

            // set the model and view to then return
            modelAndView.addObject("task", task);
            modelAndView.setViewName("/editTask");
        }

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/editTask")
    public ModelAndView editTask(@RequestParam Long taskId, @RequestParam String description, @RequestParam String notes) {
        Task task = taskService.findTaskById(taskId);

        if(task != null) {
            if(!notes.isEmpty() && notes != null) {
                task.setNotes(notes);
            }

            task.setDescription(description);
            taskService.updateTask(task);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/moveLocations");

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, value ="/moveLocations/updateTaskOrder")
    public ModelAndView updateTaskOrder(@RequestBody List<Task> tasks) {
        ModelAndView modelAndView = new ModelAndView();
        List<Task> db_tasks = getTasksInOrder();
        for (Task reqTask : tasks) {
            Task matchingDbTask = db_tasks.stream().filter(task -> task.getId().compareTo(reqTask.getId()) == 0).findFirst().orElse(null);

            if (matchingDbTask != null) {
                matchingDbTask.setOrderNum(reqTask.getOrderNum());
                taskService.updateTask(matchingDbTask);
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
            Task task = taskService.findTaskById(taskId);
            if (task != null) {
                task.setCompleted(isCompleted);
                taskService.updateTask(task);
                modelAndView.setViewName("redirect:/moveLocations");
            }
        }
        return modelAndView;
    }


    @RequestMapping(method = RequestMethod.POST, value = "/moveLocations/deleteTask")
    public ModelAndView deleteTask(@RequestParam Long taskId) {
        taskService.deleteTask(taskId);
        resetTaskOrderNums();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/moveLocations");
        return modelAndView;
    }

    private List<Task> getTasksInOrder() {
        List<Task> tasks = new ArrayList<>();
        taskService.getTasks().forEach(tasks::add);
        tasks.sort(Comparator.comparing(Task::getOrderNum));
        return tasks;
    }

    private void resetTaskOrderNums() {
        List<Task> tasks = getTasksInOrder();

        for(int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            task.setOrderNum(i);
            taskService.updateTask(task);
        }
    }
}
