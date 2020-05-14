package facades;

import dto.UserDTO;
import entities.Rating;
import entities.Review;
import entities.Role;
import entities.User;
import errorhandling.AuthenticationException;
import errorhandling.NotFoundException;
import errorhandling.UserException;
import errorhandling.WrongCriticCodeException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    private Rating ra1;
    private Review re1;

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
            em.persist(new Role("critic"));
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
        ra1 = new Rating("movie", "UserNameTest1", 8);
        re1 = new Review("movie", "UserNameTest1", "Bad movie");
        u1.addRole(new Role("user"));
        u2.addRole(new Role("critic"));
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Rating.deleteAllRows").executeUpdate();
            em.createNamedQuery("Review.deleteAllRows").executeUpdate();
            em.persist(ra1);
            em.persist(re1);
            em.persist(u1);
            em.persist(u2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //@Test
    public void testGetVerifiedUser_ReturnsUser_EqualsResults() throws AuthenticationException {
        System.out.println("testGetVerifiedUser_ReturnsUser_EqualsResults");
        User userResult = FACADE.getVerifiedUser(u1.getUserName(), "UserPassword1");
        assertEquals(u1, userResult);
    }
    
    @Test
    public void testGetVerifiedUser_UsernameNotCorrect_ThrowsAuthenticationException()
            throws AuthenticationException {
        System.out.println("testGetVerifiedUser_UsernameNotCorrect_ThrowsUserException");
        Assertions.assertThrows(AuthenticationException.class, () -> {
            FACADE.getVerifiedUser("WrongUserName", "UserPassword1");
        });
    }

    @Test
    public void testGetUser_ReturnsUser_EqualsResults() throws UserException {
        System.out.println("testGetUser_ReturnsUser_EqualsResults");
        UserDTO user1 = new UserDTO(u1);
        List<Rating> RaL = new ArrayList();
        List<Review> ReL = new ArrayList();
        RaL.add(ra1);
        ReL.add(re1);
        user1.setRatings(RaL);
        user1.setReviews(ReL);
        UserDTO user = FACADE.getUser(u1.getUserName());
        assertTrue(user1.equals(user));
        assertEquals(user1, user);
    }

    //@Test //Not possible anymore. You cant get the info unless you are logged in and we use the securityContext to get the username from the token.
    public void testGetUser_UsernameNotRegistered_ThrowsUserException() throws UserException {
        System.out.println("testGetUser_UsernameNotRegistered_ThrowsException");
        Assertions.assertThrows(UserException.class, () -> {
            FACADE.getUser("NotRealUsername");
        });
    }

    @Test
    public void testRegisterUser_ReturnsConfirmationString_EqualResults() throws UserException, WrongCriticCodeException {
        System.out.println("testRegisterUser_ReturnsConfirmationString_EqualResults");
        UserDTO udto = new UserDTO("registerUser", "registerPassword", "female", "08-08-2020", "");
        String actualResult = FACADE.registerUser(udto);
        String expectedResult = "User was created";
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testRegisterUser_UsernameAlreadyTaken_ThrowsUserException() {
        System.out.println("testRegisterUser_UsernameAlreadyTaken_ThrowsUserException");
        UserDTO udto = new UserDTO(u1.getUserName(), "registerPassword", "female", "08-08-2020", "");
        Assertions.assertThrows(UserException.class, () -> {
            FACADE.registerUser(udto);
        });
    }

    @Test
    public void testEditUser_ReturnsEditedUser_EqualResults() throws UserException {
        System.out.println("testEditUser_ReturnsEditedUser_EqualResults");
        String username = u1.getUserName();
        u1.setBirthday("11-09-2001");
        UserDTO udto = new UserDTO(u1);
        UserDTO result = FACADE.editUser(username, udto);
        String expectedResult = "11-09-2001";
        assertEquals(expectedResult, result.getBirthday());
    }

    @Test
    public void testEditUser_UserNotFound_ThrowsUserException() throws UserException {
        System.out.println("testEditUser_UserNotFound_ThrowsUserException");
        UserDTO udto = new UserDTO("NotRealName", "", "", "", "");
        Assertions.assertThrows(UserException.class, () -> {
            FACADE.editUser("NotRealName", udto);
        });
    }

    @Test
    public void testDeleteUser_ReturnsConfirmationString_EqualResults() throws UserException, NotFoundException {
        System.out.println("testDeleteUser_ReturnsConfirmationString_EqualResults");
        String result = FACADE.deleteUser(u1.getUserName());
        String expectedResult = "User: " + u1.getUserName() + " Ratings: 1 Reviews: 1 were deleted";
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDeleteUser_UserNotFound_ThrowsNotFoundException() throws UserException, NotFoundException {
        System.out.println("testDeleteUser_UserNotFound_ThrowsNotFoundException");
        Assertions.assertThrows(NotFoundException.class, () -> {
            FACADE.deleteUser("NotRealUsername");
        });
    }

}
