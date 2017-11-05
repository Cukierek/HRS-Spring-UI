package pl.com.bottega.hrs.application;

import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.Gender;

import java.time.LocalDate;
import java.util.List;

public class DetailedEmployeeDto extends BasicEmployeeDto {

    private LocalDate birthDate, hireDate;

    private Gender gender;

    private Address address;

    private Integer salary;

    private String title;

    private List<String> deptNo;

    private List<SalaryDto> salaryHistory;

    private List<DepartmentDto> departmentHistory;

    private List<TitleDto> titleHistory;

    public DetailedEmployeeDto(Employee employee) {
        super(null, null, null);

    }

}
