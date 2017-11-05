package pl.com.bottega.hrs;

import pl.com.bottega.hrs.model.Address;
import pl.com.bottega.hrs.model.Employee;
import pl.com.bottega.hrs.infrastructure.StandardTimeProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;

public class App2 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory( "HRS" );
        Address address = new Address("al. Warszawska 10", "Lublin");
        Employee employee = new Employee(500004, "Krzysiek", "Jerzyna", LocalDate.parse("1960-01-01"), address, new StandardTimeProvider());
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(employee);
        em.flush();

        em.getTransaction().commit();
        emf.close();
    }

}
