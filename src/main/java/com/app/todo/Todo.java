package com.app.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	private boolean completed;
	private LocalDateTime dueDate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Todo() {}

	public Todo(Long id, String title, boolean completed, LocalDateTime dueDate) {
		this.id = id;
		this.title = title;
		this.completed = completed;
		this.dueDate = dueDate;
		this.createdAt = LocalDateTime.now();
		this.updatedAt = LocalDateTime.now();
	}

	// --- SAFETY LOGIC (Prevents Crashes) ---
	public int getPriorityScore() {
		if (completed) return -1;
		if (dueDate == null) return 0;
		try {
			int score = 0;
			LocalDate dueDay = dueDate.toLocalDate();
			LocalDate today = LocalDate.now();
			if (dueDay.isEqual(today)) score += 100;
			else if (dueDay.isBefore(today)) score += 150;
			else if (dueDay.isEqual(today.plusDays(1))) score += 50;
			return score;
		} catch (Exception e) { return 0; }
	}

	public String getNiceDate() {
		if (dueDate == null) return "No Date";
		return dueDate.format(DateTimeFormatter.ofPattern("MMM d, h:mm a"));
	}

	public String getFormattedDueDate() {
		return getNiceDate();
	}

	// For Edit Form
	public String getHtmlValue() {
		if (dueDate == null) return "";
		return dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
	}

	// For Creation Time
	public String getNiceCreated() {
		if (createdAt == null) return "";
		return createdAt.format(DateTimeFormatter.ofPattern("h:mm a"));
	}

	public String getFormattedCreatedTime() { return getNiceCreated(); } // Alias

	// --- GETTERS & SETTERS ---
	public Long getId() { return id; }
	public void setId(Long id) { this.id = id; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public boolean isCompleted() { return completed; }
	public void setCompleted(boolean completed) { this.completed = completed; }
	public LocalDateTime getDueDate() { return dueDate; }
	public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
	public LocalDateTime getCreatedAt() { return createdAt; }
	public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
	public LocalDateTime getUpdatedAt() { return updatedAt; }
	public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}