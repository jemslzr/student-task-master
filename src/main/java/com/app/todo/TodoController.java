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
    public String addTodo(@RequestParam String title, @RequestParam String dueDate, Model model) {
        System.out.println("ADDING NEW TASK: " + title);
        saveTask(null, title, dueDate);
        updateModel(model, "all");
        return "index";
    }

    // --- EDIT ENDPOINT ---
    @PostMapping("/update/{id}")
    public String updateTodo(@PathVariable Long id,
                             @RequestParam String title,
                             @RequestParam String dueDate,
                             @RequestParam(defaultValue = "all") String filter,
                             Model model) {

        System.out.println(">>> EDITING TASK ID: " + id);
        System.out.println(">>> New Title: " + title);
        System.out.println(">>> New Date: " + dueDate);

        saveTask(id, title, dueDate);

        updateModel(model, filter);
        return "index"; // Reloads the page with new data
    }

    private void saveTask(Long id, String title, String dateStr) {
        try {
            if (dateStr != null && dateStr.length() == 16) dateStr += ":00";

            LocalDateTime parsedDate = (dateStr == null || dateStr.isEmpty())
                    ? LocalDateTime.now()
                    : LocalDateTime.parse(dateStr);

            Todo todo;
            if (id == null) {
                todo = new Todo(null, title, false, parsedDate);
            } else {
                todo = repository.findById(id).orElseThrow();
                todo.setTitle(title);
                todo.setDueDate(parsedDate);
                todo.setUpdatedAt(LocalDateTime.now());
            }
            repository.saveAndFlush(todo);
            System.out.println(">>> SUCCESS: SAVED TO DATABASE");

        } catch (Exception e) {
            System.out.println("!!! ERROR SAVING: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PostMapping("/toggle/{id}")
    public String toggleTodo(@PathVariable Long id, @RequestParam(defaultValue = "all") String filter, Model model) {
        Todo todo = repository.findById(id).orElseThrow();
        todo.setCompleted(!todo.isCompleted());
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

    @GetMapping("/filter/{type}")
    public String filterTodos(@PathVariable String type, Model model) {
        updateModel(model, type);
        return "index";
    }

    private void updateModel(Model model, String filterType) {
        List<Todo> allTodos = repository.findAll();
        // Remove corrupted data to prevent crash
        List<Todo> validTodos = allTodos.stream().filter(t -> t.getDueDate() != null).collect(Collectors.toList());

        long completedTasks = validTodos.stream().filter(Todo::isCompleted).count();
        int xpPercentage = validTodos.isEmpty() ? 0 : (int) ((completedTasks * 100) / validTodos.size());
        int level = (int) (completedTasks / 5) + 1;

        Todo aiSuggestion = validTodos.stream()
                .filter(t -> !t.isCompleted())
                .max(Comparator.comparingInt(Todo::getPriorityScore))
                .orElse(null);

        List<Todo> filteredList = validTodos.stream()
                .filter(t -> {
                    LocalDate d = t.getDueDate().toLocalDate();
                    LocalDate today = LocalDate.now();
                    if (filterType.equals("today")) return d.isEqual(today);
                    if (filterType.equals("week")) return d.isAfter(today.minusDays(1)) && d.isBefore(today.plusDays(7));
                    if (filterType.equals("month")) return d.getMonth() == today.getMonth();
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