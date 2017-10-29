package pl.com.bottega.hrs.infrastructure;

import org.junit.Test;
import pl.com.bottega.hrs.application.BasicEmployeeDto;
import pl.com.bottega.hrs.application.EmployeeFinder;
import pl.com.bottega.hrs.application.EmployeeSearchCriteria;
import pl.com.bottega.hrs.application.EmployeeSearchResults;
import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.StandardTimeProvider;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class EmployeeFinderTest extends InfrastructureTest {

    private int number = 1;
    //private EmployeeFinder employeeFinder = new JPQLEmployeeFinder(createEntityManager());
    private EmployeeFinder employeeFinder = new JPACriteriaEmployeeFinder(createEntityManager());
    private EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
    private EmployeeSearchResults results;

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
        Employee nowak = createEmployee("Nowak");
        Employee nowacki = createEmployee("Nowacki");
        createEmployee("Kowalski");
        executeInTransaction((em) -> {
            nowak.changeSalary(50000);
            em.merge(nowak);
        });
        executeInTransaction((em) -> {
            nowacki.changeSalary(20000);
            em.merge(nowacki);
        });

        //when
        criteria.setSalaryFrom(45000);
        criteria.setSalaryTo(60000);
        search();

        //then
        assertLastNames("Nowak");
    }


    private Employee createEmployee(String firstName, String lastName, String birthDate) {
        Address address = new Address("al. Warszawska 10", "Lublin");
        Employee employee = new Employee(number++, firstName, lastName,
                LocalDate.parse(birthDate), address, new StandardTimeProvider());
        executeInTransaction((em) -> em.persist(employee));
        return employee;
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

}
