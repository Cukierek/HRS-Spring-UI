package pl.com.bottega.hrs.model.repositories;

import pl.com.bottega.hrs.model.Department;

public interface DepartmentRepository {

    Department get(String number);

    void save(Department department);

}
