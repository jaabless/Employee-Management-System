package services;

import exceptions.EmployeeNotFoundException;
import exceptions.InvalidDepartmentException;
import models.Employee;
import services.comparators.EmployeePerformanceComparator;
import services.comparators.EmployeeSalaryComparator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;

public class EmployeeDatabase<T> {
    private Map<T, Employee<T>> employeeMap = new HashMap<>();

    public void addEmployee(Employee<T> employee) {
        employeeMap.put(employee.getEmployeeId(), employee);
    }

    public boolean removeEmployee(T employeeId) {
        return employeeMap.remove(employeeId) != null;
    }

    public boolean updateEmployeeDetails(Employee<T> updatedEmployee) {
        Employee<T> existing = employeeMap.get(updatedEmployee.getEmployeeId());
        if (existing == null) return false;
        existing.setName(updatedEmployee.getName());
        existing.setDepartment(updatedEmployee.getDepartment());
        existing.setSalary(updatedEmployee.getSalary());
//        existing.setSalary(updatedEmployee.getSalary()*10); //bug - multiplies salary by 10
        existing.setPerformanceRating(updatedEmployee.getPerformanceRating());
        existing.setYearsOfExperience(updatedEmployee.getYearsOfExperience());
        existing.setActive(updatedEmployee.isActive());
        return true;
    }

    public List<Employee<T>> getAllEmployees() {
        return new ArrayList<>(employeeMap.values());
    }

    public List<Employee<T>> searchByName(String name) {
        return employeeMap.values().stream()
                .filter(emp -> emp.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Employee<T>> searchByDepartment(String department) throws InvalidDepartmentException {
        List<Employee<T>> results = employeeMap.values().stream()
                .filter(emp -> emp.getDepartment().equalsIgnoreCase(department))
                .collect(Collectors.toList());

        if (results.isEmpty()) {
            throw new InvalidDepartmentException("No employee found in the given department.");
        }
        return results;
    }


    public List<Employee<T>> searchByPerformance(double rating) {
        return employeeMap.values().stream()
                .filter(emp -> emp.getPerformanceRating() >= rating)
                .collect(Collectors.toList());
    }

    public List<Employee<T>> searchBySalaryRange(double min, double max) {
        return employeeMap.values().stream()
                .filter(emp -> emp.getSalary() >= min && emp.getSalary() <= max)
                .collect(Collectors.toList());
    }

    public List<Employee<T>> sortByPerformance() {
        return employeeMap.values().stream().sorted(new EmployeePerformanceComparator<>()).collect(Collectors.toList());
    }

    public List<Employee<T>> sortBySalary() {
        return employeeMap.values().stream().sorted(new EmployeeSalaryComparator<>()).collect(Collectors.toList());
    }

    public void giveRaise(double threshold, double increment) {
        employeeMap.values().forEach(emp -> {
            if (emp.getPerformanceRating() >= threshold) {
                emp.setSalary(emp.getSalary() + increment);
            }
        });
    }

    public List<Employee<T>> getTopPaid() {
        return employeeMap.values().stream()
                .sorted(new EmployeeSalaryComparator<>())
                .limit(5)
                .collect(Collectors.toList());
    }

    public double averageSalaryByDepartment(String dept) {
        return employeeMap.values().stream()
                .filter(emp -> emp.getDepartment().equalsIgnoreCase(dept))
                .mapToDouble(Employee::getSalary)
                .average().orElse(0.0);
    }

    public Employee<T> getEmployeeById(T id) throws EmployeeNotFoundException {
        Employee<T> employee = employeeMap.get(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with ID " + id + " not found.");
        }
        return employee;
    }

    public List<Employee<T>> sortByExperience() {
        return employeeMap.values().stream().sorted().collect(Collectors.toList());
    }


    public Iterator<Employee<T>> iterator() {
        return employeeMap.values().iterator();
    }
}
