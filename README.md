# 🚀 Student Task Master

**Student Task Master** is a gamified productivity application designed to help students track assignments, manage deadlines, and stay motivated through RPG-style leveling mechanics.

---

## 🎥 Gameplay Demo

![Application Demo](demo.gif)

---

## ✨ Key Features

### 🎮 Gamification Engine
* **XP & Leveling System:** Earn XP for every task completed. Level up from "Level 1 Student" as you crush more tasks.
* **Streak Counter:** Visual feedback on how many tasks you've completed in total.
* **Dynamic UI:** The interface reacts to your progress with engaging animations.

### 🤖 Smart Heuristics Prioritization
* **Heuristic Algorithm:** The app analyzes your tasks based on **Due Date** (Urgency) and **Creation Date** (Stagnation).
* **Priority Card:** A glowing "AI Priority Pick" card automatically floats to the top, suggesting the single most important task you should tackle right now.

### 📅 Time Management
* **Precision Scheduling:** Set specific Dates and Times (e.g., "Oct 25, 2:30 PM") for deadlines.
* **Smart Filters:** Instantly switch views between **All**, **Today**, **Week**, and **Month**.

### ⚡ Seamless User Experience
* **HTMX Powered:** Interactions (Checking off tasks, Creating, Updating, Deleting, Filtering) happen instantly without reloading the page.
* **Split Views:**
    * **Active Missions:** Bright, active cards for pending to-dos.
    * **Mission Archive:** Greyed-out history for completed tasks (undoable at any time).

---

## 🛠️ Tech Stack

* **Backend:** Java, Spring Boot (Web, JPA, Validation).
* **Frontend:** Thymeleaf (Template Engine), Bootstrap 5 (CSS), HTMX.
* **Database:** H2 In-Memory Database.
* **Build Tool:** Maven.

---

## 🏃‍♂️ How to Run Locally

1.  **Clone the repository**
    ```bash
    git clone [https://github.com/YOUR_USERNAME/student-task-master.git](https://github.com/YOUR_USERNAME/student-task-master.git)
    cd student-task-master
    ```

2.  **Run the App**
    ```bash
    ./mvnw spring-boot:run
    ```

3.  **Access the Dashboard**
    Open your browser and go to: `http://localhost:8080`
