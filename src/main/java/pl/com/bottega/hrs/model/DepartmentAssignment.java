package pl.com.bottega.hrs.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "dept_emp")
public class DepartmentAssignment {

    @Embeddable
    public static class DepartmentAssignmentId implements Serializable {

        @Column(name = "emp_no")
        private Integer empNo;

        @ManyToOne
        @JoinColumn(name = "dept_no")
        private Department department;

    }

    @EmbeddedId
    private DepartmentAssignmentId id;

    @Column(name = "from_date")
    private LocalDate fromDate;

    @Column(name = "to_date")
    private LocalDate toDate;

    public Department getDepartment() {
        return id.department;
    }

}
