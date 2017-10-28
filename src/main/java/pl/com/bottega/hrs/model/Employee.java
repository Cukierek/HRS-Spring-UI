package pl.com.bottega.hrs.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @Column(name = "emp_no")
    private Integer empNo;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Transient
    private TimeProvider timeProvider;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(value = EnumType.STRING)
    @Column(columnDefinition = "enum('M', 'F')")
    private Gender gender;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "emp_no")
    private Collection<Salary> salaries = new LinkedList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "emp_no")
    private Collection<DepartmentAssignment> departmentAssignments = new LinkedList<>();

    Employee() {
    }

    public Employee(Integer empNo, String firstName, String lastName, LocalDate birthDate, Address address, TimeProvider timeProvider) {
        this.empNo = empNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.timeProvider = timeProvider;
        this.hireDate = timeProvider.today();
        this.address = address;
    }

    public void updateProfile(String firstName, String lastName, LocalDate birthDate) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public Address getAddress() {
        return address;
    }

    public Collection<Salary> getSalaries() {
        return salaries;
    }

    public void changeSalary(Integer newSalary) {
        Optional<Salary> optionalSalary = getCurrentSalary();
        if (optionalSalary.isPresent()) {
            Salary currentSalary = optionalSalary.get();
            removeOrTerminateSalary(newSalary, currentSalary);
        }
        addNewSalary(newSalary);
    }

    private void addNewSalary(Integer newSalary) {
        salaries.add(new Salary(empNo, newSalary, timeProvider));
    }

    private void removeOrTerminateSalary(Integer newSalary, Salary currentSalary) {
        if(currentSalary.startsToday()) {
            salaries.remove(currentSalary);
        }
        else {
            currentSalary.terminate();
        }
    }

    public void assignDepartment(Department department) {

    }

    public void unassignDepartment(Department department) {

    }

    public Collection<Department> getCurrentDepartments() {
        return departmentAssignments.stream().
                map((assignment) -> assignment.getDepartment()).collect(Collectors.toList());
    }

    public Optional<Salary> getCurrentSalary() {
        /*for(Salary salary : salaries) {
            if(salary.getToDate().isAfter(timeProvider.today()))
                return Optional.of(salary);
        }
        return Optional.empty();*/

        return salaries.stream().
                filter((salary) -> salary.isCurrent()).
                findFirst();
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empNo=" + empNo +
                ", birthDate=" + birthDate +
                ", hireDate=" + hireDate +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", salaries= " + salaries.size() +
                '}';
    }
}
