package pl.com.bottega.hrs.infrastructure;

import org.junit.Test;
import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Employee;

import javax.persistence.EntityManager;
import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JPAEmployeeRepositoryTest extends InfrastructureTest {

    private EntityManager entityManager = createEntityManager();
    private JPAEmployeeRepository sut = new JPAEmployeeRepository(entityManager);

    @Test
    public void firstNumberShouldBeOne() {
        // when
        Integer number = sut.generateNumber();

        // then
        assertEquals(new Integer(1), number);
    }

    @Test
    public void shouldSaveEmployee() {
        // given
        Employee employee = createEmployee(1, "Kowalski");

        // when
        executeInTransaction(entityManager, () -> sut.save(employee));

        // then
        executeInTransaction(entityManager, () -> {
            Employee employeeFromRepo = sut.get(1);
            assertNotNull(employeeFromRepo);
            assertEquals("Kowalski", employeeFromRepo.getLastName());
        });
    }

    @Test
    public void shouldGenerateNextEmpNo() {
        //given
        Employee e1 = createEmployee(1, "Kowalski");
        Employee e2 = createEmployee(2, "Janowski");
        executeInTransaction(entityManager, () -> sut.save(e1));
        executeInTransaction(entityManager, () -> sut.save(e2));

        //when
        Integer number = sut.generateNumber();

        //then
        assertEquals(new Integer(3), number);
    }

    @Test(expected = NoSuchEntityException.class)
    public void shouldThrowExceptionWhenNoSuchEmployee() {
        sut.get(1);
    }

    private Employee createEmployee(Integer no, String lastName) {
        Address address = new Address("al. Warszawska 10", "Lublin");
        return new Employee(no, "Jan", lastName, LocalDate.now(), address, new StandardTimeProvider());
    }

}
