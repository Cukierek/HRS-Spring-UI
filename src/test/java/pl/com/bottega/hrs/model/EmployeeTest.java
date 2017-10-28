package pl.com.bottega.hrs.model;

import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmployeeTest {

    public static final int SALARY = 50000 * 12;
    private final Employee sut = new Employee();

    @Test
    public void shouldReturnNoSalaryIfNoSalaryDefined() {
        assertFalse(getCurrentSalary().isPresent());
    }

    @Test
    public void shouldAddAndReturnEmployeeSalary() {
        // when
        sut.changeSalary(SALARY);

        // then
        Optional<Salary> salaryOptional = getCurrentSalary();
        assertTrue(salaryOptional.isPresent());
        assertEquals(SALARY, salaryOptional.get().getValue());
    }


    private Optional<Salary> getCurrentSalary() {
        return sut.getCurrentSalary();
    }

}
