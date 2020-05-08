package facades;

import dto.UserDTO;
import entities.Rating;
import entities.Review;
import entities.Role;
import entities.User;
import errorhandling.UserException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author rando
 */
public class UserFacadeTest {

    private static EntityManagerFactory EMF;
    private static UserFacade FACADE;
    private User u1, u2;

    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = UserFacade.getUserFacade(EMF);
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.persist(new Role("user"));
            em.persist(new Role("admin"));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        u1 = new User("UserNameTest1", "UserPassword1", "male", "05-05-2020");
        u2 = new User("UserNameTest2", "UserPassword2", "female", "50-50-2020");
        Rating rating = new Rating("movie", "UserNameTest1", 8);
        Review review = new Review("movie", "UserNameTest1", "Bad movie");
        u1.addRole(new Role("user"));
        u2.addRole(new Role("user"));
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Rating.deleteAllRows").executeUpdate();
            em.createNamedQuery("Review.deleteAllRows").executeUpdate();
            em.persist(rating);
            em.persist(review);
            em.persist(u1);
            em.persist(u2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testRegisterUser_ReturnsConfirmationString_EqualResults() throws UserException {
        System.out.println("testRegisterUser_ReturnsConfirmationString_EqualResults");
        UserDTO udto = new UserDTO("registerUser", "registerPassword", "female", "08-08-2020");
        String actualResult = FACADE.registerUser(udto);
        String expectedResult = "User was created";
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testRegisterUser_ReturnsErrorMSG_ThrowsUserException() {
        System.out.println("testRegisterExistingUser_ReturnsErrorMSG_ThrowsUserException");
        UserDTO udto = new UserDTO(u1.getUserName(), "registerPassword", "female", "08-08-2020");
        Assertions.assertThrows(UserException.class, () -> {
            FACADE.registerUser(udto);
        });
    }

    @Test
    public void testEditUser_ReturnsEditedUser_EqualResults() throws UserException {
        System.out.println("testEditUser_ReturnsEditedUser_EqualResults");
        u1.setBirthday("11-09-2001");
        UserDTO udto = new UserDTO(u1);
        UserDTO result = FACADE.editUser(udto);
        String expectedResult = "11-09-2001";
        assertEquals(expectedResult, result.getBirthday());
    }

    @Test
    public void testDeleteUser_ReturnsConfirmationString_EqualResults() throws UserException {
        System.out.println("testDeleteUser_ReturnsConfirmationString_EqualResults");
        UserDTO udto = new UserDTO(u1);
        String result = FACADE.deleteUser(udto);
        String expectedResult = "User: " + u1.getUserName() + " Ratings: 1 Reviews: 1 were deleted";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testGetUser() throws UserException {
        System.out.println("testGetUser");
        UserDTO user = FACADE.getUser(u1.getUserName());
        System.out.println(user);
    }

}
