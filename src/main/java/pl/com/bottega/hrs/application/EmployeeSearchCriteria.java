package pl.com.bottega.hrs.application;

import java.time.LocalDate;
import java.util.Collection;

public class EmployeeSearchCriteria {

    private String lastNameQuery, firstNameQuery;

    private LocalDate birthDateFrom, birthDateTo;

    private LocalDate hireDateFrom, hireDateTo;

    private Integer salaryFrom, salaryTo;

    private Collection<String> titles;

    private Collection<String> departmentNumbers;

    private int pageSize = 20;

    private int pageNumber = 1;

    public String getLastNameQuery() {
        return lastNameQuery;
    }

    public void setLastNameQuery(String lastNameQuery) {
        this.lastNameQuery = lastNameQuery;
    }

    public String getFirstNameQuery() {
        return firstNameQuery;
    }

    public void setFirstNameQuery(String firstNameQuery) {
        this.firstNameQuery = firstNameQuery;
    }

    public LocalDate getBirthDateFrom() {
        return birthDateFrom;
    }

    public void setBirthDateFrom(LocalDate birthDateFrom) {
        this.birthDateFrom = birthDateFrom;
    }

    public LocalDate getBirthDateTo() {
        return birthDateTo;
    }

    public void setBirthDateTo(LocalDate birthDateTo) {
        this.birthDateTo = birthDateTo;
    }

    public LocalDate getHireDateFrom() {
        return hireDateFrom;
    }

    public void setHireDateFrom(LocalDate hireDateFrom) {
        this.hireDateFrom = hireDateFrom;
    }

    public LocalDate getHireDateTo() {
        return hireDateTo;
    }

    public void setHireDateTo(LocalDate hireDateTo) {
        this.hireDateTo = hireDateTo;
    }

    public Integer getSalaryFrom() {
        return salaryFrom;
    }

    public void setSalaryFrom(Integer salaryFrom) {
        this.salaryFrom = salaryFrom;
    }

    public Integer getSalaryTo() {
        return salaryTo;
    }

    public void setSalaryTo(Integer salaryTo) {
        this.salaryTo = salaryTo;
    }

    public Collection<String> getTitles() {
        return titles;
    }

    public void setTitles(Collection<String> titles) {
        this.titles = titles;
    }

    public Collection<String> getDepartmentNumbers() {
        return departmentNumbers;
    }

    public void setDepartmentNumbers(Collection<String> departmentNumbers) {
        this.departmentNumbers = departmentNumbers;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
