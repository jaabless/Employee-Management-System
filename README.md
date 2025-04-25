# 🧑‍💼 Employee Management System

A Java-based desktop application for managing employee data, built using JavaFX for the UI and faciliatates Java Generics, Comparable, Comparator, Collections, and Streams for efficient data processing.

---

## 📌 Features

✅ Add/Edit/Delete Employees
Sort employees by years of experience using Comparable, and by salary or performance rating using Comparator.
🔍 Search & Filter employees by ID, name, department, performance rating, salary range, and employment status
📊 Salary Analytics: Implement salary raise logic, retrieve top-paid employees, and calculate department-wise salary averages with Streams.
📋 JavaFX GUI: A user-friendly interface for managing employee records.
⚠️ Error handling dialogs for invalid inputs and exceptions
✅ JUnit 5 Unit Tests for database logic

---

## 🚀 Getting Started

**Tools and Technologies Used**
- **Java (JDK 21)**
- **JavaFX 21** for the GUI
- **JUnit 5** for testing configurations
- **IntelliJ IDEA** as IDE

**How to Run**

1. Make sure Java and JavaFX are installed and configured.
2. Clone this repository: 
   ```bash
   git clone https:https://github.com/jaabless/Employee-Management-System.git
3. Open the project in your preferred IDE
4. Run the BankManagement.java file to launch the JavaFX application.

---

## 🚀 Usage

**Employee Class Implementation**

- The Employee<T> class supports generic identifiers (Integer).
- Implements Comparable<Employee<T>> for sorting by years of experience.
- Provides Comparator implementations for sorting by salary and performance rating.

**Sorting Employees**

- Comparable: Sorts employees by years of experience in descending order.
- Comparator Implementations:
  - EmployeeSalaryComparator: Sorts employees by highest salary first.
  - EmployeePerformanceComparator: Sorts employees by best performance rating first.
 
**Salary Management & Analytics**

- Salary Raise Logic: Employees with performance rating ≥ 4.5 receive a raise.
- Top 5 Highest-Paid Employees: Identified using Streams & Lists.
- Average Department Salary Calculation: Uses Stream API.
