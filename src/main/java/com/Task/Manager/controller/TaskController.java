package com.Task.Manager.controller;

import com.Task.Manager.model.Task;
import com.Task.Manager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public String index(Model model) {
        List<Task> tasks = taskService.getAllTasks();
        model.addAttribute("tasks", tasks);
        return "index"; // This corresponds to index.html
    }

    @GetMapping("/add_task")
    public String showAddTaskForm(Model model) {
        model.addAttribute("task", new Task());
        return "add_task"; // This would correspond to the add_task.html template
    }

    @PostMapping("/add_task")
    public String addTask(@ModelAttribute Task task) {
        taskService.saveTask(task);
        return "redirect:/"; // Redirect to the index page after adding a task
    }

    @GetMapping("/edit_task/{id}")
    public String showEditTaskForm(@PathVariable Long id, Model model) {
        Task task = taskService.getTaskById(id);
        model.addAttribute("task", task);
        return "edit_task"; // This would correspond to the edit_task.html template
    }

    @PostMapping("/edit_task/{id}")
    public String editTask(@PathVariable Long id, @ModelAttribute Task task) {
        task.setId(id); // Set the id of the task to edit
        taskService.saveTask(task);
        return "redirect:/"; // Redirect to the index page after editing
    }

    @PostMapping("/delete_task/{id}")
    public String deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return "redirect:/"; // Redirect to the index page after deleting
    }
}
