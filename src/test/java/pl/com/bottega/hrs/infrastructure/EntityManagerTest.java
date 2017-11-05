package pl.com.bottega.hrs.infrastructure;

import org.hibernate.LazyInitializationException;
import org.junit.Test;
import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Employee;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class EntityManagerTest extends InfrastructureTest {


    @Test
    public void tracksChangesToEntities() {
        // given
        executeInTransaction((em) -> {
            Employee employee = createEmployee("Jan");
            em.persist(employee);
        });

        // when
        executeInTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            updateFirstName("Janusz", employee);
        });

        // then
        executeInTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            assertEquals("Janusz", employee.getFirstName());
        });
    }

    @Test
    public void mergesEntities() {
        // given
        executeInTransaction((em) -> {
            Employee employee = createEmployee("Jan");
            em.persist(employee);
        });

        // when
        executeInTransaction((em) -> {
            Employee employee = createEmployee("Janusz");
            Employee employeeCopy = em.merge(employee);
            updateFirstName("Eustachy", employeeCopy);
            updateFirstName("Stefan", employee);
        });

        // then
        executeInTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            assertEquals("Eustachy", employee.getFirstName());
        });
    }

    @Test
    public void removesEntities() {
        // given
        executeInTransaction((em) -> {
            Employee employee = createEmployee("Jan");
            em.persist(employee);
        });

        // when
        executeInTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            em.remove(employee);
        });

        // then
        executeInTransaction((em) -> {
            Employee employee = em.find(Employee.class, 1);
            assertNull(employee);
        });
    }

    @Test
    public void cascadesOperations() {
        // given
        executeInTransaction((em) -> {
            Employee employee = createEmployee("Janek");
            em.persist(employee);
        });

        // then
        executeInTransaction((em) -> {
            Address address = em.find(Address.class, 1);
            assertNotNull(address);
        });
    }

    private Employee tmpEmployee;

    @Test(expected = LazyInitializationException.class)
    public void throwsLazyInitException() {
        // given
        executeInTransaction((em) -> {
            Employee employee = createEmployee("Janek");
            em.persist(employee);
        });

        executeInTransaction((em) -> {
            tmpEmployee = em.find(Employee.class, 1);
        });

        tmpEmployee.getSalaries().size();
    }

    private Employee createEmployee(String firstName) {
        Address address = new Address("al. Warszawska 10", "Lublin");
        return new Employee(1, firstName, "Nowak", LocalDate.now(), address, new StandardTimeProvider());
    }

    private void updateFirstName(String newName, Employee employee) {
        employee.updateProfile(newName, "Nowak", LocalDate.now());
    }

}
