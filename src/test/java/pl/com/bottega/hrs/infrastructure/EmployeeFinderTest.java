package pl.com.bottega.hrs.infrastructure;

import org.junit.Test;
import pl.com.bottega.hrs.application.BasicEmployeeDto;
import pl.com.bottega.hrs.application.EmployeeFinder;
import pl.com.bottega.hrs.application.EmployeeSearchCriteria;
import pl.com.bottega.hrs.application.EmployeeSearchResults;
import pl.com.bottega.hrs.model.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class EmployeeFinderTest extends InfrastructureTest {

    private int number = 1;
    //private EmployeeFinder employeeFinder = new JPQLEmployeeFinder(createEntityManager());
    private EmployeeFinder employeeFinder = new JPACriteriaEmployeeFinder(createEntityManager());
    private EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
    private EmployeeSearchResults results;
    private Department d1, d2, d3;
    private TimeMachine timeMachine = new TimeMachine();

    @Test
    public void shouldFindByLastNameQuery() {
        // given
        createEmployee("Nowak");
        createEmployee("Nowacki");
        createEmployee("Kowalski");

        // when
        criteria.setLastNameQuery("nowa");
        search();

        // then
        assertLastNames("Nowak", "Nowacki");
    }

    @Test
    public void shouldFindByFirstNameAndLastNameQuery() {
        // given
        createEmployee("Jan", "Nowak");
        createEmployee("Stefan", "Nowacki");
        createEmployee("Kowalski");

        // when
        criteria.setLastNameQuery("nowa");
        criteria.setFirstNameQuery("Ja");
        search();

        // then
        assertLastNames("Nowak");
    }

    @Test
    public void shouldFindByFirstName() {
        // given
        createEmployee("Nowak");
        createEmployee("Nowacki");
        createEmployee("Kowalski");

        // when
        criteria.setFirstNameQuery("cze");
        search();

        // then
        assertLastNames("Nowak", "Nowacki", "Kowalski");
    }

    @Test
    public void shouldFindByBirthDate() {
        // given
        createEmployee("Jan", "Nowak", "1960-01-01");
        createEmployee("Jan", "Nowacki", "1970-01-01");
        createEmployee("Jan", "Nowakowski", "1984-01-01");
        createEmployee("Jan", "Kowalski", "1990-01-01");
        createEmployee("Jan", "Kowalewski", "1995-01-01");

        // when
        criteria.setBirthDateFrom(LocalDate.parse("1970-01-01"));
        criteria.setBirthDateTo(LocalDate.parse("1990-01-01"));
        search();

        // then
        assertLastNames("Nowacki", "Nowakowski", "Kowalski");
    }

    @Test
    public void shouldPaginateResults() {
        // given
        createEmployee("Nowak");
        createEmployee("Nowacki");
        createEmployee("Kowalski");
        createEmployee("Kowalewski");
        createEmployee("Kowalewska");

        // when
        criteria.setPageSize(2);
        criteria.setPageNumber(2);
        criteria.setFirstNameQuery("Cze");
        search();

        // then
        assertLastNames("Kowalski", "Kowalewski");
        assertEquals(5, results.getTotalCount());
        assertEquals(3, results.getPagesCount());
        assertEquals(2, results.getPageNumber());
    }

    //@Test
    public void shouldSearchBySalary() {
        //given
        employee().withLastName("Nowak").withSalary(50000).create();
        employee().withLastName("Nowacki").withSalary(20000).create();
        createEmployee("Kowalski");

        //when
        criteria.setSalaryFrom(45000);
        criteria.setSalaryTo(60000);
        search();

        //then
        assertLastNames("Nowak");
    }

    //@Test
    public void shouldSearchByHistoricalSalary() {
        //given
        employee().withLastName("Nowak").withSalary(50000).create();
        employee().withLastName("Nowacki").withSalary(20000).create();
        ;
        employee().withLastName("Kowalski").withSalary(50000, "1990-01-01").
                withSalary(20000).create();

        //when
        criteria.setSalaryFrom(45000);
        criteria.setSalaryTo(60000);
        search();

        //then
        assertLastNames("Nowak");
    }

    @Test
    public void shouldSearchByDepartments() {
        //given
        createDepartments();
        employee().withLastName("Nowak").withDepartment(d1).create();
        employee().withLastName("Nowacki").
                withDepartment(d1).withDepartment(d2).create();
        employee().withLastName("Kowalski").withDepartment(d3).create();

        //when
        criteria.setDepartmentNumbers(Arrays.asList(d1.getNumber(), d2.getNumber()));
        search();

        //then
        assertLastNames("Nowak", "Nowacki");
    }

    @Test
    public void shouldSearchByHistoricalDepartments() {
        //given
        createDepartments();
        employee().withLastName("Nowak").withDepartment(d1, "1990-01-01").
                withoutDepartment(d1).
                withDepartment(d3).create();
        employee().withLastName("Nowacki").
                withDepartment(d1).withDepartment(d2).create();
        employee().withLastName("Kowalski").withDepartment(d3).create();

        //when
        criteria.setDepartmentNumbers(Arrays.asList(d1.getNumber(), d2.getNumber()));
        search();

        //then
        assertLastNames("Nowacki");
    }

    @Test
    public void np1Demo() {
        // given
        int n = 5;
        for (int i = 1; i <= 5; i++) {
            employee().
                    withSalary(20000, "1990-01-01").
                    withSalary(50000).withLastName("Nowak " + i).create();
        }

        // when
        executeInTransaction((entityManager) -> {
            List<Employee> emps = entityManager.createQuery("SELECT DISTINCT(e) FROM Employee e " +
                    "LEFT JOIN FETCH e.salaries " +
                    "JOIN e.salaries s " +
                    "LEFT JOIN FETCH e.address " +
                    "WHERE s.toDate > :from"
            ).setParameter("from", LocalDate.now()).
                    getResultList();
            for (Employee e : emps) {
                System.out.println(e.getCurrentSalary().get());
            }
        });


    }

    private void createDepartments() {
        d1 = new Department("d1", "Cleaning");
        d2 = new Department("d2", "Marketing");
        d3 = new Department("d3", "Development");
        executeInTransaction(em -> {
            em.persist(d1);
            em.persist(d2);
            em.persist(d3);
        });
    }


    private Employee createEmployee(String firstName, String lastName, String birthDate) {
        return employee().withName(firstName, lastName).withBirthDate(birthDate).create();
    }

    private Employee createEmployee(String lastName) {
        return createEmployee("Czesiek", lastName, "1990-01-01");
    }

    private Employee createEmployee(String firstName, String lastName) {
        return createEmployee(firstName, lastName, "1990-01-01");
    }

    private void search() {
        results = employeeFinder.search(criteria);
    }

    private void assertLastNames(String... lastNames) {
        assertEquals(
                Arrays.asList(lastNames),
                results.getResults().stream().
                        map(BasicEmployeeDto::getLastName).collect(Collectors.toList())
        );
    }

    class EmployeeBuilder {

        private String firstName = "Czesiek";
        private String lastName = "Nowak";
        private String birthDate = "1990-01-01";
        private Address address = new Address("al. Warszawska 10", "Lublin");
        private List<Consumer<Employee>> consumers = new LinkedList<>();

        EmployeeBuilder withName(String firstName, String lastName) {
            this.firstName = firstName;
            this.lastName = lastName;
            return this;
        }

        EmployeeBuilder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        EmployeeBuilder withFirstName(String firstName, String lastName) {
            this.firstName = firstName;
            return this;
        }

        EmployeeBuilder withBirthDate(String birthDate) {
            this.birthDate = birthDate;
            return this;
        }

        EmployeeBuilder withSalary(Integer salary) {
            consumers.add(employee -> employee.changeSalary(salary));
            return this;
        }

        EmployeeBuilder withSalary(Integer salary, String fromDate) {
            consumers.add(employee -> {
                timeMachine.travel(LocalDate.parse(fromDate));
                employee.changeSalary(salary);
                timeMachine.reset();
            });
            return this;
        }

        EmployeeBuilder withDepartment(Department dept) {
            consumers.add(employee -> employee.assignDepartment(dept));
            return this;
        }

        EmployeeBuilder withDepartment(Department dept, String fromDate) {
            consumers.add(employee -> {
                timeMachine.travel(LocalDate.parse(fromDate));
                employee.assignDepartment(dept);
                timeMachine.reset();
            });
            return this;
        }

        Employee create() {
            Employee employee = new Employee(number++, firstName, lastName, LocalDate.parse(birthDate), address, timeMachine);
            consumers.forEach(c -> c.accept(employee));
            executeInTransaction((em) -> {
                em.persist(employee);
            });
            return employee;
        }

        public EmployeeBuilder withoutDepartment(Department dept) {
            consumers.add(employee -> employee.unassignDepartment(dept));
            return this;
        }
    }

    private EmployeeBuilder employee() {
        return new EmployeeBuilder();
    }

}
