package pl.com.bottega.hrs.model;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EmployeeTest {

    public static final int SALARY = 50000 * 12;
    private final Address address = new Address("Północna", "Lublin");
    private final TimeMachine timeMachine = new TimeMachine();
    private final Employee sut = new Employee(1,
            "Jan",
            "Nowak",
            LocalDate.parse("1960-01-01"),
            address,
            timeMachine);

    @Test
    public void shouldReturnNoSalaryIfNoSalaryDefined() {
        assertFalse(getCurrentSalary().isPresent());
    }

    @Test
    public void shouldAddAndReturnEmployeeSalary() {
        // when
        sut.changeSalary(SALARY);

        // then
        assertTrue(getCurrentSalary().isPresent());
        assertEquals(SALARY, getCurrentSalaryValue());
    }

    @Test
    public void shouldAllowMultipleChangesOfSalary() {
        // when
        sut.changeSalary(SALARY);
        sut.changeSalary(SALARY / 2);

        // then
        assertEquals(SALARY / 2, getCurrentSalaryValue());
    }


    private int getCurrentSalaryValue() {
        return getCurrentSalary().get().getValue();
    }

    private Optional<Salary> getCurrentSalary() {
        return sut.getCurrentSalary();
    }

}
