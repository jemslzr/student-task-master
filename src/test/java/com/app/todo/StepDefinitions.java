package com.app.todo;

import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class StepDefinitions {
    @Autowired TodoRepository repository;

    @Given("the database is empty")
    public void cleanDb() {
        repository.deleteAll();
    }

    @When("I add a todo with title {string}")
    public void addToDo(String title) {

        repository.save(new Todo(null, title, false, LocalDateTime.now()));
    }

    @Then("the todo list should contain {string}")
    public void checkList(String title) {
        assertTrue(repository.findAll().stream()
                .anyMatch(t -> t.getTitle().equals(title)));
    }
}