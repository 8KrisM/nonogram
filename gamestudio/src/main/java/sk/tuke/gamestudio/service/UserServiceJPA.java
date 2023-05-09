package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
public class UserServiceJPA implements UserService {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addUser(Customer customer) {
        try {
            TypedQuery<Customer> query = entityManager.createQuery("SELECT u FROM Customer u WHERE u.login = :login", Customer.class);
            query.setParameter("login", customer.getLogin());
            List<Customer> result = query.getResultList();
            if (result.isEmpty()) {
                entityManager.persist(customer);
            } else throw new UserException("User already exists");
        } catch (Exception e) {
            throw new UserException("Error adding user");
        }
    }

    @Override
    public boolean validCredentials(Customer customer) {
        try {
            List<Customer> result = entityManager.createQuery("SELECT u FROM Customer u WHERE u.login = :login AND u.password= :password", Customer.class)
                    .setParameter("login", customer.getLogin())
                    .setParameter("password", customer.getPassword())
                    .getResultList();
            return !result.isEmpty();
        } catch (Exception e) {
            throw new ScoreException("Error validating credentials", e);
        }

    }


    @Override
    public void reset() {
        try {
            entityManager.createNativeQuery("DELETE FROM customer").executeUpdate();
        } catch (Exception e) {
            throw new ScoreException("Error resetting user", e);
        }
    }
}
