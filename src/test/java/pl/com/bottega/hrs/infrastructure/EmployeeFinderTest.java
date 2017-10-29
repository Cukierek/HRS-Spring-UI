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

    @Test
    public void shouldFindByLastNameQuery() {
        // given
        createEmployee("Nowak");
        createEmployee("Nowacki");
        createEmployee("Kowalski");

        // when
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setLastNameQuery("nowa");
        EmployeeFinder employeeFinder = new JPQLEmployeeFinder(createEntityManager());
        EmployeeSearchResults results = employeeFinder.search(criteria);

        // then
        assertEquals(
                Arrays.asList("Nowak", "Nowacki"),
                results.getResults().stream().
                        map(BasicEmployeeDto::getLastName).collect(Collectors.toList())
        );
    }

    @Test
    public void shouldFindByFirstNameAndLastNameQuery() {
        // given
        createEmployee("Jan", "Nowak");
        createEmployee("Stefan", "Nowacki");
        createEmployee("Kowalski");

        // when
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setLastNameQuery("nowa");
        criteria.setFirstNameQuery("Ja");
        EmployeeFinder employeeFinder = new JPQLEmployeeFinder(createEntityManager());
        EmployeeSearchResults results = employeeFinder.search(criteria);

        // then
        assertEquals(
                Arrays.asList("Nowak"),
                results.getResults().stream().
                        map(BasicEmployeeDto::getLastName).collect(Collectors.toList())
        );
    }

    @Test
    public void shouldFindByFirstName() {
        // given
        createEmployee("Nowak");
        createEmployee("Nowacki");
        createEmployee("Kowalski");

        // when
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setFirstNameQuery("cze");
        EmployeeFinder employeeFinder = new JPQLEmployeeFinder(createEntityManager());
        EmployeeSearchResults results = employeeFinder.search(criteria);

        // then
        assertEquals(
                Arrays.asList("Nowak", "Nowacki", "Kowalski"),
                results.getResults().stream().
                        map(BasicEmployeeDto::getLastName).collect(Collectors.toList())
        );
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

}
