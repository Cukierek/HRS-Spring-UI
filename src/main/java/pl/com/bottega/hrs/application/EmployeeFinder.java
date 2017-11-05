package pl.com.bottega.hrs.application;

public interface EmployeeFinder {

    EmployeeSearchResults search(EmployeeSearchCriteria criteria);

    DetailedEmployeeDto getEmployeeDetails(Integer empNo);

}
