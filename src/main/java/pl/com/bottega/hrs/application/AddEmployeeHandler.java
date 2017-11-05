package pl.com.bottega.hrs.application;

import org.springframework.stereotype.Component;
import pl.com.bottega.hrs.model.Department;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.TimeProvider;
import pl.com.bottega.hrs.model.commands.AddEmployeeCommand;
import pl.com.bottega.hrs.model.repositories.DepartmentRepository;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

@Component
public class AddEmployeeHandler {

    private EmployeeRepository repository;
    private DepartmentRepository departmentRepository;
    private TimeProvider timeProvider;

    public AddEmployeeHandler(EmployeeRepository repository,
                              DepartmentRepository departmentRepository,
                              TimeProvider timeProvider) {
        this.repository = repository;
        this.departmentRepository = departmentRepository;
        this.timeProvider = timeProvider;
    }

    public void handle(AddEmployeeCommand cmd) {
        Employee employee = new Employee(
                repository.generateNumber(),
                cmd.getFirstName(),
                cmd.getLastName(),
                cmd.getBirthDate(),
                cmd.getAddress(),
                timeProvider
        );
        employee.changeSalary(cmd.getSalary());
        employee.changeTitle(cmd.getTitle());
        Department department = departmentRepository.get(cmd.getDeptNo());
        employee.assignDepartment(department);
        repository.save(employee);
    }


}
