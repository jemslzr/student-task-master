Feature: Manage Todos
  Scenario: Add a new todo
    Given the database is empty
    When I add a todo with title "Buy Milk"
    Then the todo list should contain "Buy Milk"