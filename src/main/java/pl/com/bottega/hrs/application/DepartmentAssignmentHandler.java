package pl.com.bottega.hrs.application;

import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.commands.AssignDepartmentToEmployeeCommand;
import pl.com.bottega.hrs.model.repositories.DepartmentRepository;
import pl.com.bottega.hrs.model.repositories.EmployeeRepository;

public class DepartmentAssignmentHandler {

    private EmployeeRepository employeeRepository;
    private DepartmentRepository departmentRepository;

    public DepartmentAssignmentHandler(
            DepartmentRepository departmentRepository,
            EmployeeRepository employeeRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    public void handle(AssignDepartmentToEmployeeCommand cmd) {
        Employee employee = employeeRepository.get(cmd.getEmpNo());
        employee.assignDepartment(departmentRepository.get(cmd.getDeptNo()));
        employeeRepository.save(employee);
    }

}

