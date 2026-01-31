package com.app.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TodoController {

    @Autowired
    private TodoRepository repository;

    @GetMapping("/")
    public String index(Model model) {
        updateModel(model, "all");
        return "index";
    }

    @PostMapping("/add")
    public String addTodo(@RequestParam String title, @RequestParam LocalDateTime dueDate, Model model) {
        Todo todo = new Todo(null, title, false, dueDate);
        repository.saveAndFlush(todo);
        updateModel(model, "all");
        return "index";
    }

    @PostMapping("/toggle/{id}")
    public String toggleTodo(@PathVariable Long id, @RequestParam(defaultValue = "all") String filter, Model model) {
        Todo todo = repository.findById(id).orElseThrow();
        todo.setCompleted(!todo.isCompleted());
        todo.setUpdatedAt(LocalDateTime.now());
        repository.saveAndFlush(todo);
        updateModel(model, filter);
        return "index";
    }

    @PostMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id, @RequestParam(defaultValue = "all") String filter, Model model) {
        repository.deleteById(id);
        updateModel(model, filter);
        return "index";
    }

    @PostMapping("/update/{id}")
    public String updateTodo(@PathVariable Long id, @RequestParam String title, @RequestParam LocalDateTime dueDate, @RequestParam(defaultValue = "all") String filter, Model model) {
        Todo todo = repository.findById(id).orElseThrow();
        todo.setTitle(title);
        todo.setDueDate(dueDate);
        todo.setUpdatedAt(LocalDateTime.now());
        repository.saveAndFlush(todo);
        updateModel(model, filter);
        return "index";
    }

    @GetMapping("/filter/{type}")
    public String filterTodos(@PathVariable String type, Model model) {
        updateModel(model, type);
        return "index";
    }

    private void updateModel(Model model, String filterType) {
        List<Todo> allTodos = repository.findAll();
        LocalDate today = LocalDate.now();

        // 1. GAMIFICATION
        long totalTasks = allTodos.size();
        long completedTasks = allTodos.stream().filter(Todo::isCompleted).count();
        int xpPercentage = totalTasks == 0 ? 0 : (int) ((completedTasks * 100) / totalTasks);
        int level = (int) (completedTasks / 5) + 1;

        // 2. AI SUGGESTION
        Todo aiSuggestion = allTodos.stream()
                .filter(t -> !t.isCompleted())
                .max(Comparator.comparingInt(Todo::getPriorityScore))
                .orElse(null);

        // 3. FILTERING
        List<Todo> filteredList = allTodos.stream()
                .filter(t -> {
                    LocalDate taskDate = t.getDueDate().toLocalDate();

                    if (filterType.equals("today")) return taskDate.isEqual(today);
                    if (filterType.equals("week")) return taskDate.isAfter(today.minusDays(1)) && taskDate.isBefore(today.plusDays(7));
                    // NEW: Month Filter
                    if (filterType.equals("month")) return taskDate.getMonth() == today.getMonth() && taskDate.getYear() == today.getYear();

                    return true;
                })
                .sorted(Comparator.comparing(Todo::isCompleted).thenComparing(Todo::getDueDate))
                .collect(Collectors.toList());

        model.addAttribute("todos", filteredList);
        model.addAttribute("xp", xpPercentage);
        model.addAttribute("level", level);
        model.addAttribute("completedCount", completedTasks);
        model.addAttribute("activeFilter", filterType);
        model.addAttribute("aiSuggestion", aiSuggestion);
    }
}