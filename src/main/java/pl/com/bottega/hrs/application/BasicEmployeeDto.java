package pl.com.bottega.hrs.application;

public class BasicEmployeeDto {

    private Integer empNo;

    private String firstName, lastName;

    public BasicEmployeeDto(Integer empNo, String firstName, String lastName) {
        this.empNo = empNo;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Integer empNo) {
        this.empNo = empNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
