package pl.com.bottega.hrs.model;

import org.junit.Test;
import org.mockito.Mockito;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private Department d1 = Mockito.mock(Department.class);
    private Department d2 = Mockito.mock(Department.class);

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
        assertEquals(1, sut.getSalaries().size());
    }

    @Test
    public void shouldKeepSalaryHistory() {
        // when
        timeMachine.travel(Duration.ofDays(-365 * 2));
        LocalDate t0 = timeMachine.today();
        sut.changeSalary(SALARY);
        timeMachine.travel(Duration.ofDays(365));
        LocalDate t1 = timeMachine.today();
        sut.changeSalary(SALARY / 2);
        timeMachine.travel(Duration.ofDays(100));
        LocalDate t2 = timeMachine.today();
        sut.changeSalary(SALARY * 2);

        // then
        Collection<Salary> history = sut.getSalaries();
        assertEquals(3, history.size());
        assertEquals(
                Arrays.asList(SALARY, SALARY / 2, SALARY * 2),
                history.stream().map((s) -> s.getValue()).collect(Collectors.toList())
        );
        assertEquals(
                Arrays.asList(t0, t1, t2),
                history.stream().map((s) -> s.getFromDate()).collect(Collectors.toList())
        );
        assertEquals(
                Arrays.asList(t1, t2, TimeProvider.MAX_DATE),
                history.stream().map((s) -> s.getToDate()).collect(Collectors.toList())
        );
    }

    private int getCurrentSalaryValue() {
        return getCurrentSalary().get().getValue();
    }

    private Optional<Salary> getCurrentSalary() {
        return sut.getCurrentSalary();
    }

    @Test
    public void shouldReturnEmptyDepartmentsWhenNoAssignment() {
        assertEquals(0, sut.getCurrentDepartments().size());
    }

    @Test
    public void shouldAssignToManyDepartments() {
        // when
        sut.assignDepartment(d1);
        sut.assignDepartment(d2);

        // then
        assertEquals(Arrays.asList(d1, d2), sut.getCurrentDepartments());
    }

    @Test
    public void shouldNotAssignTwiceToTheSameDepartment() {
        // when
        sut.assignDepartment(d1);
        sut.assignDepartment(d1);

        // then
        assertEquals(Arrays.asList(d1), sut.getCurrentDepartments());
    }

    @Test
    public void shouldUnassignDepratments() {
        // when
        sut.assignDepartment(d1);
        sut.assignDepartment(d2);
        sut.unassignDepartment(d2);

        // then
        assertEquals(Arrays.asList(d1), sut.getCurrentDepartments());
    }

    @Test
    public void shouldKeepDepartmentsHistory() {
        // when
        timeMachine.travel(Duration.ofDays(-365 * 2));
        LocalDate t0 = timeMachine.today();
        sut.assignDepartment(d1);
        timeMachine.travel(Duration.ofDays(365));
        LocalDate t1 = timeMachine.today();
        sut.assignDepartment(d2);
        timeMachine.travel(Duration.ofDays(100));
        LocalDate t2 = timeMachine.today();
        sut.unassignDepartment(d2);

        // then
        Collection<DepartmentAssignment> history = sut.getDepartmentsHistory();
        assertEquals(
                Arrays.asList(t0, t1),
                history.stream().map(DepartmentAssignment::getFromDate).collect(Collectors.toList())
        );
        assertEquals(
                Arrays.asList(TimeProvider.MAX_DATE, t2),
                history.stream().map(DepartmentAssignment::getToDate).collect(Collectors.toList())
        );
    }

    @Test
    public void shouldReturnEmptyTitleIfNoTitleAssigned() {
        assertFalse(getCurrentTitle().isPresent());
    }

    @Test
    public void shouldChangeAndReturnTitle() {
        //when
        sut.changeTitle("CEO");

        //then
        assertTrue(getCurrentTitle().isPresent());
        assertEquals("CEO", getCurrentTitleName());
    }

    @Test
    public void shouldChangeTitleOnTheSameDay() {
        // when
        sut.changeTitle("CEO");
        sut.changeTitle("cleaner");

        // then
        assertEquals("cleaner", getCurrentTitleName());
    }

    @Test
    public void shouldKeepTitleHistory() {
        timeMachine.travel(Duration.ofDays(-365 * 2));
        LocalDate t0 = timeMachine.today();
        sut.changeTitle("cleaner");
        timeMachine.travel(Duration.ofDays(365));
        LocalDate t1 = timeMachine.today();
        sut.changeTitle("senior cleaner");
        timeMachine.travel(Duration.ofDays(100));
        LocalDate t2 = timeMachine.today();
        sut.changeTitle("CEO");

        // then
        Collection<Title> history = sut.getTitleHistory();
        assertEquals(3, history.size());
        assertEquals(
                Arrays.asList("cleaner", "senior cleaner", "CEO"),
                history.stream().map(Title::getName).collect(Collectors.toList())
        );
        assertEquals(
                Arrays.asList(t0, t1, t2),
                history.stream().map(Title::getFromDate).collect(Collectors.toList())
        );
        assertEquals(
                Arrays.asList(t1, t2, TimeProvider.MAX_DATE),
                history.stream().map(Title::getToDate).collect(Collectors.toList())
        );
    }

    private String getCurrentTitleName() {
        return getCurrentTitle().get().getName();
    }

    private Optional<Title> getCurrentTitle() {
        return sut.getCurrentTitle();
    }

}
