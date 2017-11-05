package pl.com.bottega.hrs.infrastructure;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import pl.com.bottega.hrs.model.Department;
import pl.com.bottega.hrs.model.repositories.DepartmentRepository;

import javax.persistence.EntityManager;

public class JPADepartmentRepository implements DepartmentRepository {

    private EntityManager entityManager;

    public JPADepartmentRepository(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    public Department get(String deptNo) {
        Department department = entityManager.find(Department.class, deptNo);
        if(department == null)
            throw new NoSuchEntityException();
        return department;
    }


    @Override
    public void save(Department department) {
        entityManager.persist(department);
    }
}
