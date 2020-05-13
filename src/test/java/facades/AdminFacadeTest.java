package facades;

import entities.CriticCode;
import entities.Role;
import entities.User;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author rando
 */
public class AdminFacadeTest {
    private static EntityManagerFactory EMF;
    private static AdminFacade FACADE;
    private User u1, u2;

    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = AdminFacade.getAdminFacade(EMF);
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.persist(new Role("user"));
            em.persist(new Role ("critic"));
            em.persist(new Role("admin"));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        /*u1 = new User("UserNameTest1", "UserPassword1", "male", "05-05-2020");
        u2 = new User("UserNameTest2", "UserPassword2", "female", "50-50-2020");
        Rating rating = new Rating("movie", "UserNameTest1", 8);
        Review review = new Review("movie", "UserNameTest1", "Bad movie");
        u1.addRole(new Role("user"));
        u2.addRole(new Role("user"));*/
        try {
            em.getTransaction().begin();
            /*em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Rating.deleteAllRows").executeUpdate();
            em.createNamedQuery("Review.deleteAllRows").executeUpdate();*/
            em.createNamedQuery("Critic.deleteAllRows").executeUpdate();
            /*em.persist(rating);
            em.persist(review);*/
            /*em.persist(u1);
            em.persist(u2);*/
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }


    @Test
    public void testGenerateUniqueCriticCode_ReturnsCriticCode_EqualResults(){
        //Arrange
        double randomValue = 0.2;
        //Act
        CriticCode cc = FACADE.generateUniqueCriticCode(randomValue);
        //Assert
        assertEquals(cc.getCode(), "MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM");
    }
    
}
