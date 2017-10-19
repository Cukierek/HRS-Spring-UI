package pl.com.bottega.hrs.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "salaries")
public class Salary {

    @Embeddable
    public static class SalaryId implements Serializable {

        @Column(name = "emp_no")
        private Integer empNo;

        @Column(name = "from_date")
        private LocalDate fromDate;

    }

    @EmbeddedId
    private SalaryId id;

    private Integer salary;

    @Column(name = "to_date")
    private LocalDate toDate;

}
