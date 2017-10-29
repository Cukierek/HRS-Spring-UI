package pl.com.bottega.hrs.infrastructure;

import pl.com.bottega.hrs.application.BasicEmployeeDto;
import pl.com.bottega.hrs.application.EmployeeFinder;
import pl.com.bottega.hrs.application.EmployeeSearchCriteria;
import pl.com.bottega.hrs.application.EmployeeSearchResults;
import pl.com.bottega.hrs.model.Employee;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class JPACriteriaEmployeeFinder implements EmployeeFinder {

    private EntityManager entityManager;

    public JPACriteriaEmployeeFinder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public EmployeeSearchResults search(EmployeeSearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BasicEmployeeDto> cq = cb.createQuery(BasicEmployeeDto.class);
        Root employee = cq.from(Employee.class);
        cq.select(cb.construct(BasicEmployeeDto.class,
                employee.get("empNo"), employee.get("firstName"), employee.get("lastName")));

        Predicate predicate = buildPredicate(criteria, cb, employee);

        cq.where(predicate);
        Query query = entityManager.createQuery(cq);
        List<BasicEmployeeDto> results = query.getResultList();
        EmployeeSearchResults employeeSearchResults = new EmployeeSearchResults();
        employeeSearchResults.setResults(results);
        return employeeSearchResults;
    }

    private Predicate buildPredicate(EmployeeSearchCriteria criteria, CriteriaBuilder cb, Root employee) {
        Predicate predicate = cb.conjunction();
        predicate = addFirstNamePredicate(criteria, cb, employee, predicate);
        predicate = addLastNamePredicate(criteria, cb, employee, predicate);
        predicate = addBirthDateFromPredicate(criteria, cb, employee, predicate);
        predicate = addBirthDateToPredicate(criteria, cb, employee, predicate);
        return predicate;
    }

    private Predicate addBirthDateToPredicate(EmployeeSearchCriteria criteria, CriteriaBuilder cb, Root employee, Predicate predicate) {
        if(criteria.getBirthDateTo() != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(employee.get("birthDate"), criteria.getBirthDateTo()));
        }
        return predicate;
    }

    private Predicate addBirthDateFromPredicate(EmployeeSearchCriteria criteria, CriteriaBuilder cb, Root employee, Predicate predicate) {
        if(criteria.getBirthDateFrom() != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(employee.get("birthDate"), criteria.getBirthDateFrom()));
        }
        return predicate;
    }

    private Predicate addLastNamePredicate(EmployeeSearchCriteria criteria, CriteriaBuilder cb, Root employee, Predicate predicate) {
        if(criteria.getLastNameQuery() != null) {
            predicate = cb.and(predicate,
                    cb.like(employee.get("lastName"), criteria.getLastNameQuery() + "%"));
        }
        return predicate;
    }

    private Predicate addFirstNamePredicate(EmployeeSearchCriteria criteria, CriteriaBuilder cb, Root employee, Predicate predicate) {
        if(criteria.getFirstNameQuery() != null) {
            predicate = cb.and(predicate,
                    cb.like(employee.get("firstName"), criteria.getFirstNameQuery() + "%"));
        }
        return predicate;
    }
}
