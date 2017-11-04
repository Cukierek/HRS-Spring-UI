package pl.com.bottega.hrs.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @Column(name = "dept_no", columnDefinition="char(4)")
    private String deptNo;

    @Column(name = "dept_name")
    private String name;

    Department() {
    }

    public Department(String deptNo, String name) {
        this.deptNo = deptNo;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Department that = (Department) o;

        return deptNo.equals(that.deptNo);
    }

    @Override
    public int hashCode() {
        return deptNo.hashCode();
    }

    public String getNumber() {
        return deptNo;
    }
}
