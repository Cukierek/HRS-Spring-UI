package pl.com.bottega.hrs.application;


import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.commands.ChangeEmployeeTitleCommand;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

public class ChangeEmployeeTitleHandler {

    private EmployeeRepository employeeRepository;

    public ChangeEmployeeTitleHandler(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void handle(ChangeEmployeeTitleCommand cmd) {
        Employee employee = employeeRepository.get(cmd.getEmpNo());
        employee.changeTitle(cmd.getTitle());
        employeeRepository.save(employee);
    }

}
