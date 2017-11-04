package pl.com.bottega.hrs.infrastructure;

import pl.com.bottega.hrs.application.BasicEmployeeDto;
import pl.com.bottega.hrs.application.EmployeeFinder;
import pl.com.bottega.hrs.application.EmployeeSearchCriteria;
import pl.com.bottega.hrs.application.EmployeeSearchResults;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.model.TimeProvider;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
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
        cq.distinct(true);
        Query query = entityManager.createQuery(cq);
        query.setMaxResults(criteria.getPageSize());
        query.setFirstResult((criteria.getPageNumber() - 1) * criteria.getPageSize());
        List<BasicEmployeeDto> results = query.getResultList();
        EmployeeSearchResults employeeSearchResults = new EmployeeSearchResults();
        employeeSearchResults.setResults(results);
        int total = searchTotalCount(criteria);
        employeeSearchResults.setTotalCount(total);
        employeeSearchResults.setPageNumber(criteria.getPageNumber());
        employeeSearchResults.setPagesCount(total / criteria.getPageSize() +
                (total % criteria.getPageSize() == 0 ? 0 : 1));
        return employeeSearchResults;
    }

    private int searchTotalCount(EmployeeSearchCriteria criteria) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root employee = cq.from(Employee.class);
        cq.select(cb.count(employee));
        Predicate predicate = buildPredicate(criteria, cb, employee);
        cq.where(predicate);
        Query query = entityManager.createQuery(cq);
        return ((Long) query.getSingleResult()).intValue();
    }

    private Predicate buildPredicate(EmployeeSearchCriteria criteria, CriteriaBuilder cb, Root employee) {
        Predicate predicate = cb.conjunction();
        predicate = addFirstNamePredicate(criteria, cb, employee, predicate);
        predicate = addLastNamePredicate(criteria, cb, employee, predicate);
        predicate = addBirthDateFromPredicate(criteria, cb, employee, predicate);
        predicate = addBirthDateToPredicate(criteria, cb, employee, predicate);
        predicate = addDepartmentsPredicate(criteria, cb, employee, predicate);
        return predicate;
    }

    private Predicate addDepartmentsPredicate(EmployeeSearchCriteria criteria, CriteriaBuilder cb, Root employee, Predicate predicate) {
        if(criteria.getDepartmentNumbers() != null && criteria.getDepartmentNumbers().size() > 0) {
            Join deptAsgn = employee.join("departmentAssignments");
            Join dept = deptAsgn.join("id").join("department");
            predicate = cb.and(predicate, dept.get("deptNo").in(criteria.getDepartmentNumbers()));
            predicate = cb.and(predicate, cb.equal(deptAsgn.get("toDate"), TimeProvider.MAX_DATE));
        }
        return predicate;
    }

    private Predicate addBirthDateToPredicate(EmployeeSearchCriteria criteria, CriteriaBuilder cb, Root employee, Predicate predicate) {
        if (criteria.getBirthDateTo() != null) {
            predicate = cb.and(predicate, cb.lessThanOrEqualTo(employee.get("birthDate"), criteria.getBirthDateTo()));
        }
        return predicate;
    }

    private Predicate addBirthDateFromPredicate(EmployeeSearchCriteria criteria, CriteriaBuilder cb, Root employee, Predicate predicate) {
        if (criteria.getBirthDateFrom() != null) {
            predicate = cb.and(predicate, cb.greaterThanOrEqualTo(employee.get("birthDate"), criteria.getBirthDateFrom()));
        }
        return predicate;
    }

    private Predicate addLastNamePredicate(EmployeeSearchCriteria criteria, CriteriaBuilder cb, Root employee, Predicate predicate) {
        if (criteria.getLastNameQuery() != null) {
            predicate = cb.and(predicate,
                    cb.like(employee.get("lastName"), criteria.getLastNameQuery() + "%"));
        }
        return predicate;
    }

    private Predicate addFirstNamePredicate(EmployeeSearchCriteria criteria, CriteriaBuilder cb, Root employee, Predicate predicate) {
        if (criteria.getFirstNameQuery() != null) {
            predicate = cb.and(predicate,
                    cb.like(employee.get("firstName"), criteria.getFirstNameQuery() + "%"));
        }
        return predicate;
    }
}
