package pl.com.bottega.hrs.infrastructure;

import org.springframework.stereotype.Component;
import pl.com.bottega.hrs.application.*;
import pl.com.bottega.hrs.model.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;

@Component
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
                employee.get(Employee_.empNo),
                employee.get(Employee_.firstName),
                employee.get(Employee_.lastName)));

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

    @Override
    @Transactional
    public DetailedEmployeeDto getEmployeeDetails(Integer empNo) {
        Employee employee = entityManager.find(Employee.class, empNo);
        if(employee == null)
            throw new NoSuchEntityException();
        return new DetailedEmployeeDto(employee);
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
            Join deptAsgn = employee.join(Employee_.departmentAssignments);
            Join dept = deptAsgn.join(DepartmentAssignment_.id).join("department");
            predicate = cb.and(predicate, dept.get(Department_.deptNo).in(criteria.getDepartmentNumbers()));
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
