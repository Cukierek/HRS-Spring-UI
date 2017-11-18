package pl.com.bottega.hrs.infrastructure;

import pl.com.bottega.hrs.application.DetailedEmployeeDto;
import pl.com.bottega.hrs.application.EmployeeFinder;
import pl.com.bottega.hrs.application.EmployeeSearchCriteria;
import pl.com.bottega.hrs.application.EmployeeSearchResults;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class JPQLEmployeeFinder implements EmployeeFinder {

    private EntityManager entityManager;

    public JPQLEmployeeFinder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public EmployeeSearchResults search(EmployeeSearchCriteria criteria) {
        EmployeeSearchResults results = new EmployeeSearchResults();
        String jpql = "SELECT " +
                "NEW pl.com.bottega.hrs.application.BasicEmployeeDto(e.empNo, e.firstName, e.lastName) " +
                "FROM Employee e ";
        String whereJpql = " 1 = 1 ";

        if(criteria.getLastNameQuery() != null) {
            whereJpql += "AND e.lastName LIKE :lastName ";
        }

        if(criteria.getFirstNameQuery() != null) {
            whereJpql += " AND e.firstName LIKE :firstName ";
        }

        if(criteria.getBirthDateFrom() != null) {
        	whereJpql += " AND e.birthDate >= :birthDateFrom";
        }

	    if(criteria.getBirthDateTo() != null) {
		    whereJpql += " AND e.birthDate <= :birthDateTo";
	    }

	    if(criteria.getHireDateFrom() != null) {
		    whereJpql += " AND e.hireDate >= :hireDateFrom";
	    }

	    if(criteria.getHireDateTo() != null) {
		    whereJpql += " AND e.hireDate <= :hireDateTo";
	    }

        Query query = entityManager.createQuery(jpql + "WHERE" +  whereJpql);
        if(criteria.getLastNameQuery() != null) {
            query.setParameter("lastName", criteria.getLastNameQuery() + "%");
        }
        if(criteria.getFirstNameQuery() != null) {
            query.setParameter("firstName", criteria.getFirstNameQuery() + "%");
        }
	    if(criteria.getBirthDateFrom() != null) {
		    query.setParameter("birthDateFrom", criteria.getBirthDateFrom());
	    }
	    if(criteria.getBirthDateTo() != null) {
		    query.setParameter("birthDateTo", criteria.getBirthDateTo());
	    }
	    if(criteria.getHireDateFrom() != null) {
		    query.setParameter("hireDateFrom", criteria.getHireDateFrom() + "%");
	    }
	    if(criteria.getHireDateTo() != null) {
		    query.setParameter("hireDateTo", criteria.getHireDateTo() + "%");
	    }

        results.setResults(query.getResultList());
        return results;
    }

    @Override
    public DetailedEmployeeDto getEmployeeDetails(Integer empNo) {
        return null;
    }

}
