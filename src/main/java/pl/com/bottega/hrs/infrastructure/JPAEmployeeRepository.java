package pl.com.bottega.hrs.infrastructure;

import org.springframework.stereotype.Component;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

import javax.persistence.EntityManager;

@Component
public class JPAEmployeeRepository implements EmployeeRepository {

    private EntityManager entityManager;

    public JPAEmployeeRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Integer generateNumber() {
        Integer n = (Integer) entityManager.createQuery(
                "SELECT MAX(e.empNo) + 1 FROM Employee e"
        ).getSingleResult();
        if (n == null)
            return 1;
        return n;
    }

    @Override
    public void save(Employee employee) {
        entityManager.persist(employee);
    }

    @Override
    public Employee get(Integer empNo) {
        Employee employee = entityManager.find(Employee.class, empNo);
        if(employee == null)
            throw new NoSuchEntityException();
        return employee;
    }

}
