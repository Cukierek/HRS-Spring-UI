package pl.com.bottega.hrs;

import pl.com.bottega.hrs.model.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App3 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Nazwisko: ");
        String lastName = scanner.nextLine();
        System.out.print("Data ur od: ");
        LocalDate from = LocalDate.parse(scanner.nextLine());
        System.out.print("Data ur do: ");
        LocalDate to = LocalDate.parse(scanner.nextLine());
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("HRS");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        List<Employee> employees = em.createQuery("FROM Employee e " +
                "WHERE (e.birthDate BETWEEN :from AND :to) " +
                "AND e.lastName LIKE :lastName").
                setMaxResults(100).
                setParameter("from", from).
                setParameter("to", to).
                setParameter("lastName", lastName + "%").
                getResultList();
        if(employees.size() == 0)
            System.out.println("No matching employees");
        for(Employee employee : employees)
            System.out.println(employee);
        em.close();
        emf.close();
    }

}
