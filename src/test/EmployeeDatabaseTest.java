package test;

import exceptions.InvalidDepartmentException;
import models.Employee;
import services.EmployeeDatabase;
import exceptions.EmployeeNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeDatabaseTest {

    private EmployeeDatabase<Integer> database;

    @BeforeEach
    public void setUp() {
        database = new EmployeeDatabase<>();
        database.addEmployee(new Employee<>(1, "Jeff", "IT", 5000.0, 4.2, 3, true));
        database.addEmployee(new Employee<>(2, "Sandra", "HR", 4500.0, 4.0, 2, true));
    }

    @Test
    public void testAddEmployee() {
        Employee<Integer> newEmp = new Employee<>(3, "Mike", "Finance", 4700.0, 4.1, 4, true);
        database.addEmployee(newEmp);
        assertEquals(3, database.getAllEmployees().size());
    }

    @Test
    public void testSearchByDepartment() throws InvalidDepartmentException {
        List<Employee<Integer>> results = database.searchByDepartment("CR");
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Sandra", results.get(0).getName());
    }

    @Test
    public void testDeleteEmployee() throws EmployeeNotFoundException {
        database.removeEmployee(1);
        assertThrows(EmployeeNotFoundException.class, () -> database.getEmployeeById(1));
        assertEquals(1, database.getAllEmployees().size());
    }
    @Test
    public void testGetEmployeeByIdThrowsWhenNotFound() {
        assertThrows(EmployeeNotFoundException.class, () -> database.getEmployeeById(999));
    }
}
