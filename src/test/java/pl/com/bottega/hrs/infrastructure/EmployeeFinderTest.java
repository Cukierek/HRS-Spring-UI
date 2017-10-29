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

    private Employee createEmployee(String firstName, String lastName) {
        Address address = new Address("al. Warszawska 10", "Lublin");
        Employee employee = new Employee(number++, firstName, lastName,
                LocalDate.now(), address, new StandardTimeProvider());
        executeInTransaction((em) -> em.persist(employee));
        return employee;
    }

    private Employee createEmployee(String lastName) {
        return createEmployee("Czesiek", lastName);
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
