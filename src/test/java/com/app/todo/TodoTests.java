package com.app.todo;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime; // CHANGED IMPORT
import static org.junit.jupiter.api.Assertions.*;

class TodoTests {
    @Test
    void testTodoCreation() {

        Todo todo = new Todo(1L, "Test Task", false, LocalDateTime.now());

        assertEquals("Test Task", todo.getTitle());
        assertFalse(todo.isCompleted());
    }
}