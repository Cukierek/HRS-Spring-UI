
package pl.com.bottega.hrs.application;

import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.commands.ChangeEmployeeProfileCommand;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

public class ChangeEmployeeProfileHandler {

    private EmployeeRepository repository;

    public ChangeEmployeeProfileHandler(EmployeeRepository repository) {

        this.repository = repository;
    }

    public void handle(ChangeEmployeeProfileCommand cmd) {
        Employee employee = repository.get(cmd.getEmpNo());
        employee.updateProfile(cmd.getFirstName(), cmd.getLastName(), cmd.getBirthDate(), cmd.getAddress(), cmd.getGender());

        repository.save(employee);

    }

}

