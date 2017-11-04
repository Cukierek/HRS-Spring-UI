package pl.com.bottega.hrs.model.repositories;

import pl.com.bottega.hrs.model.Employee;

public interface EmployeeRepository {

    Integer generateNumber();

    void save(Employee employee);

    Employee get(Integer empNo);

}
