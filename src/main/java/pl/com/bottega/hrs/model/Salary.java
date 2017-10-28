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

        SalaryId() {
        }

        public SalaryId(Integer empNo) {
            this.empNo = empNo;
            this.fromDate = LocalDate.now();
        }

        public boolean startsToday() {
            return fromDate.isEqual(LocalDate.now());
        }
    }

    @EmbeddedId
    private SalaryId id;

    private Integer salary;

    @Column(name = "to_date")
    private LocalDate toDate;

    Salary() {
    }

    public Salary(Integer empNo, Integer salary) {
        id = new SalaryId(empNo);
        this.salary = salary;
        toDate = Constants.MAX_DATE;
    }

    public boolean isCurrent() {
        return toDate.isAfter(LocalDate.now());
    }

    public void terminate() {
        toDate = LocalDate.now();
    }

    public boolean startsToday() {
        return id.startsToday();
    }

    public int getValue() {
        return salary;
    }

}
