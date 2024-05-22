
package easykanban;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class EasyKanban {
    private static final List<Task> tasks = new ArrayList<>();
    private static int taskCounter = 0;
    private static Account currentUser = null;

    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to EasyKanban");

        // Register User
        String username = JOptionPane.showInputDialog("Enter username:");
        String password = JOptionPane.showInputDialog("Enter password:");
        Account account = new Account(username, password);
        JOptionPane.showMessageDialog(null, account.registerUser());

        // Login
        boolean loggedIn = false;
        while (!loggedIn) {
            String enteredUsername = JOptionPane.showInputDialog("Enter username to login:");
            String enteredPassword = JOptionPane.showInputDialog("Enter password to login:");
            String loginStatus = account.returnLoginStatus(enteredUsername, enteredPassword);
            if (loginStatus.equals("Login successful!")) {
                loggedIn = true;
                JOptionPane.showMessageDialog(null, loginStatus);
                currentUser = account;
            } else {
                JOptionPane.showMessageDialog(null, loginStatus);
            }
        }

        // Proceed to task management if logged in
        if (currentUser != null) {
            boolean quit = false;
            while (!quit) {
                String option = JOptionPane.showInputDialog("Choose an option:\n1) Add tasks\n2) Show report\n3) Logout");
                switch (option) {
                    case "1":
                        addTasks();
                        break;
                    case "2":
                        JOptionPane.showMessageDialog(null, "Report feature is still in development. Coming soon!");
                        break;
                    case "3":
                        currentUser = null;
                        quit = true;
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "Invalid option. Please try again.");
                }
            }
        }
    }

    private static void addTasks() {
        int numberOfTasks = Integer.parseInt(JOptionPane.showInputDialog("Enter the number of tasks to add:"));
        for (int i = 0; i < numberOfTasks; i++) {
            String taskName = JOptionPane.showInputDialog("Enter task name:");
            String taskDescription = JOptionPane.showInputDialog("Enter task description:");
            String developerDetails = JOptionPane.showInputDialog("Enter developer details (first and last name):");
            int taskDuration = Integer.parseInt(JOptionPane.showInputDialog("Enter task duration in hours:"));
            String[] statuses = {"To Do", "Doing", "Done"};
            String taskStatus = (String) JOptionPane.showInputDialog(null, "Select task status:", "Task Status", JOptionPane.QUESTION_MESSAGE, null, statuses, statuses[0]);

            Task task = new Task(taskName, taskCounter++, taskDescription, developerDetails, taskDuration, taskStatus);

            if (!task.checkTaskDescription()) {
                JOptionPane.showMessageDialog(null, "Please enter a task description of less than 50 characters");
                taskCounter--; // Revert the counter as the task was not added
            } else {
                tasks.add(task);
                JOptionPane.showMessageDialog(null, "Task successfully captured");
                JOptionPane.showMessageDialog(null, task.printTaskDetails());
            }
        }
        int totalHours = Task.returnTotalHours(tasks);
        JOptionPane.showMessageDialog(null, "Total hours for all tasks: " + totalHours);
    }
}

// Task class definition
class Task {
    private String taskName;
    private int taskNumber;
    private String taskDescription;
    private String developerDetails;
    private int taskDuration;
    private String taskID;
    private String taskStatus;

    public Task(String taskName, int taskNumber, String taskDescription, String developerDetails, int taskDuration, String taskStatus) {
        this.taskName = taskName;
        this.taskNumber = taskNumber;
        this.taskDescription = taskDescription;
        this.developerDetails = developerDetails;
        this.taskDuration = taskDuration;
        this.taskStatus = taskStatus;
        this.taskID = createTaskID();
    }

    public boolean checkTaskDescription() {
        return taskDescription.length() <= 50;
    }

    public String createTaskID() {
        String taskNamePart = taskName.length() >= 2 ? taskName.substring(0, 2) : taskName;
        String developerPart = developerDetails.length() >= 3 ? developerDetails.substring(developerDetails.length() - 3) : developerDetails;
        return (taskNamePart + ":" + taskNumber + ":" + developerPart).toUpperCase();
    }

    public String printTaskDetails() {
        return "Task Status: " + taskStatus + "\n" +
                "Developer Details: " + developerDetails + "\n" +
                "Task Number: " + taskNumber + "\n" +
                "Task Name: " + taskName + "\n" +
                "Task Description: " + taskDescription + "\n" +
                "Task ID: " + taskID + "\n" +
                "Duration: " + taskDuration + " hours";
    }

    public static int returnTotalHours(List<Task> tasks) {
        int totalHours = 0;
        for (Task task : tasks) {
            totalHours += task.taskDuration;
        }
        return totalHours;
    }
}

// Account class definition
class Account {
    private String username;
    private String password;

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public boolean checkUsername() {
        return username.length() <= 5 && username.contains("_");
    }

    public boolean checkPasswordComplexity() {
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()\\-_+=\\[\\]{};:'\"\\\\|,.<>/?].*")) {
            return false;
        }
        return true;
    }

    public String registerUser() {
        if (checkUsername() && checkPasswordComplexity()) {
            return "User registered successfully!";
        } else {
            return "Username or password does not meet the criteria.";
        }
    }

    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return enteredUsername.equals(username) && enteredPassword.equals(password);
    }

    public String returnLoginStatus(String enteredUsername, String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Login successful!";
        } else {
            return "Incorrect username or password.";
        }
    }
}
