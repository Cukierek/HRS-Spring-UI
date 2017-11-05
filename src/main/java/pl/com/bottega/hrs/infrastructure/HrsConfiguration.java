package pl.com.bottega.hrs.infrastructure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.com.bottega.hrs.model.repositories.DepartmentRepository;

import javax.persistence.EntityManager;

@Configuration
public class HrsConfiguration {

    @Value("${hrs.departmentRepository}")
    private String departmentRepositoryStrategy;

    @Bean
    public DepartmentRepository departmentRepository(EntityManager entityManager) {
        if(departmentRepositoryStrategy.equals("csv"))
            return new CSVDepartmentRepository();
        else if (departmentRepositoryStrategy.equals("jpa"))
            return new JPADepartmentRepository(entityManager);
        else
            throw new IllegalArgumentException("I don't know ;(");
    }

}
