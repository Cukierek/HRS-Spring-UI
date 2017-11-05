package pl.com.bottega.hrs.acceptance;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pl.com.bottega.hrs.application.*;
import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Gender;
import pl.com.bottega.hrs.model.commands.AddDepartmentCommand;
import pl.com.bottega.hrs.model.commands.AddEmployeeCommand;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CreateEmployeeTest {

    @Autowired
    private AddEmployeeHandler addEmployeeHandler;

    @Autowired
    private EmployeeFinder employeeFinder;

    @Autowired
    private AddDepartmentHandler addDepartmentHandler;

    @Test
    public void shouldCreateEmployee() {
        // given
        AddDepartmentCommand addDepartmentCommand = new AddDepartmentCommand();
        addDepartmentCommand.setName("Marketing");
        addDepartmentCommand.setNumber("d1");
        addDepartmentHandler.handle(addDepartmentCommand);

        // when
        AddEmployeeCommand addEmployeeCommand = new AddEmployeeCommand();
        addEmployeeCommand.setFirstName("Janek");
        addEmployeeCommand.setLastName("Nowak");
        addEmployeeCommand.setAddress(new Address("test", "test"));
        addEmployeeCommand.setBirthDate(LocalDate.parse("1990-01-01"));
        addEmployeeCommand.setDeptNo("d1");
        addEmployeeCommand.setGender(Gender.M);
        addEmployeeCommand.setSalary(50000);
        addEmployeeCommand.setTitle("Junior Developer");
        addEmployeeHandler.handle(addEmployeeCommand);

        // then
        EmployeeSearchCriteria criteria = new EmployeeSearchCriteria();
        criteria.setFirstNameQuery("Janek");
        EmployeeSearchResults results = employeeFinder.search(criteria);
        assertEquals(1, results.getTotalCount());
        assertEquals("Janek", results.getResults().get(0).getFirstName());
    }

}
