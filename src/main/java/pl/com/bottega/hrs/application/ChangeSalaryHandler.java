package pl.com.bottega.hrs.application;

import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.commands.ChangeSalaryCommand;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

public class ChangeSalaryHandler {

    private EmployeeRepository repository;

    public ChangeSalaryHandler(EmployeeRepository repository) {
        this.repository = repository;
    }

    public void handle(ChangeSalaryCommand cmd) {
        Employee employee = repository.get(cmd.getEmpNo());
        employee.changeSalary(cmd.getAmount());
        repository.save(employee);
    }
}
