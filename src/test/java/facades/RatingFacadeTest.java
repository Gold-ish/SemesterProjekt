package facades;

import dto.RatingDTO;
import entities.Rating;
import entities.Role;
import entities.User;
import errorhandling.NotFoundException;
import errorhandling.UserException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author rando
 */
public class RatingFacadeTest {

    private static EntityManagerFactory EMF;
    private static RatingFacade FACADE;
    private static Rating r1, r2, r3, r4;
    private static User u1, u2, u3, u4;
    private static Role userRole, criticRole;

    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = RatingFacade.getRatingFacade(EMF);
         EntityManager em = EMF.createEntityManager();
        userRole = new Role("user");
        criticRole = new Role("critic");
        u1 = new User("UserNameTest", "UserPassword1", "male", "05-05-2020");
        u1.addRole(userRole);
        u2 = new User("UserNameTestCritic", "UserPassword2", "female", "50-50-2020");
        u2.addRole(criticRole);
        u3 = new User("UserNameTest2", "UserPassword1", "male", "05-05-2020");
        u3.addRole(userRole);
        u4 = new User("UserNameTestCritic2", "UserPassword2", "female", "50-50-2020");
        u4.addRole(criticRole);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(userRole);
            em.persist(criticRole);
            em.persist(u1);
            em.persist(u2);
            em.persist(u3);
            em.persist(u4);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        r1 = new Rating("MovieID1", u1.getUserName(), 8);
        r2 = new Rating("MovieID1", u2.getUserName(), 10);
        r3 = new Rating("MovieID1", u3.getUserName(), 4);
        r4 = new Rating("MovieID1", u4.getUserName(), 8);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Rating.deleteAllRows").executeUpdate();
            em.persist(r1);
            em.persist(r2);
            em.persist(r3);
            em.persist(r4);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    
    @AfterAll
    public static void cleanDatabase() {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            //em.createNamedQuery("Rating.deleteAllRows").executeUpdate();
            //em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testGetRatingAvg_ReturnsTheAvgRating_EqualResults() {
        System.out.println("testGetRatingAvg_ReturnsTheAvgRating_EqualResults");
        String movieID = "MovieID1";
        double avgRating = FACADE.getRatingAvg(movieID);
        double expectedRating = ((double)r1.getRating() + (double)r2.getRating()+ (double)r3.getRating() + (double)r4.getRating()) / 4;
        assertEquals(expectedRating, avgRating);
    }

    @Test
    public void testGetRatingAvg_ReturnsTheAvgRatingOfNonExistingRating_EqualResults() throws Exception {
        System.out.println("testGetRatingAvg_ReturnsTheAvgRatingOfNonExistingRating_EqualResults");
        String movieID = "NonExistingID";
        double avgRating = FACADE.getRatingAvg(movieID);
        double expectedRating = -1.0;
        assertEquals(expectedRating, avgRating);
    }
    
     @Test
    public void testGetRatingAvgUser_ReturnsTheAvgRatingForUsers_EqualResults() {
        System.out.println("testGetRatingAvgUser_ReturnsTheAvgRatingForUsers_EqualResults");
        String movieID = "MovieID1";
        double avgRating = FACADE.getRatingAvgUser(movieID);
        double expectedRating = ( (double)r1.getRating() + (double)r3.getRating()) / 2;
        assertEquals(expectedRating, avgRating);
    }

    @Test
    public void testGetRatingAvgUser_ReturnsTheAvgRatingOfNonExistingMovie_EqualResults() throws Exception {
        System.out.println("testGetRatingAvgUser_ReturnsTheAvgRatingOfNonExistingMovie_EqualResults");
        String movieID = "NonExistingID";
        double avgRating = FACADE.getRatingAvgUser(movieID);
        double expectedRating = -1.0;
        assertEquals(expectedRating, avgRating);
    }

     @Test
    public void testGetRatingAvgCritic_ReturnsTheAvgRatingForCritic_EqualResults() {
        System.out.println("testGetRatingAvgCritic_ReturnsTheAvgRatingForCritic_EqualResults");
        String movieID = "MovieID1";
        double avgRating = FACADE.getRatingAvgCritic(movieID);
        double expectedRating = ( (double)r2.getRating() + (double)r4.getRating()) / 2;
        assertEquals(expectedRating, avgRating);
    }

    @Test
    public void testGetRatingAvgCritic_ReturnsTheAvgRatingOfNonExistingMovie_EqualResults() throws Exception {
        System.out.println("testGetRatingAvgCritic_ReturnsTheAvgRatingOfNonExistingMovie_EqualResults");
        String movieID = "NonExistingID";
        double avgRating = FACADE.getRatingAvgUser(movieID);
        double expectedRating = -1.0;
        assertEquals(expectedRating, avgRating);
    }
    @Test
    public void testAddRatingCritic_ReturnsTheRating_EqualResults() {
        System.out.println("testAddRating_ReturnsTheRating_EqualResults");
        User user = new User("testuser1", "123", "other", "05-05-2020");
        RatingDTO rdtoExpected = new RatingDTO("MovieID1", user.getUserName(), 10);
        RatingDTO rdtoResult = FACADE.addRating(rdtoExpected);
        assertNotNull(rdtoResult.getId());
        assertEquals(rdtoResult.getMovieID(), rdtoExpected.getMovieID());
        assertEquals(rdtoResult.getRating(), rdtoExpected.getRating());
    }

    //@Test (Nothing we can test yet. After user implementation then mby)
    public void testAddRating_CantThinkOfNegativeTest_ThrowSomeException() throws Exception {
        System.out.println("test-negative");
    }
    
    @Test
    public void testEditRating_ReturnsTheNewRating_EqualResults() throws NotFoundException {
        System.out.println("testEditRating_ReturnsTheNewRating_EqualResults");
        User user = new User("testuser2", "123", "other", "05-05-2020");
        RatingDTO rdtoExpected = new RatingDTO(r1.getId(), r1.getMovieID(), user.getUserName(), 10);
        RatingDTO rdtoResult = FACADE.editRating(rdtoExpected);
        assertEquals(rdtoResult.getId(), rdtoExpected.getId());
        assertEquals(rdtoResult.getMovieID(), rdtoExpected.getMovieID());
        assertEquals(rdtoResult.getRating(), rdtoExpected.getRating());
    }

    @Test
    public void testEditRating_ReturnsNotFoundException_ExceptionAssertion() {
        System.out.println("testEditRating_ReturnsNotFoundException_ExceptionAssertion");
        User user = new User("testuser3", "123", "other", "05-05-2020");
        Assertions.assertThrows(NotFoundException.class, () -> {
            FACADE.editRating(new RatingDTO(-1, r1.getMovieID(), user.getUserName(), 10));

        });
    }

    @Test
    public void testDeleteRating_ReturnsConfirmationString_EqualResults() throws NotFoundException, UserException {
        System.out.println("testDeleteRating_ReturnsConfirmationString_EqualResults");
        RatingDTO ratingDTO = new RatingDTO(r1);
        RatingDTO deleteRating = FACADE.deleteRating(new RatingDTO(r1));
        assertEquals(ratingDTO, deleteRating);
    }

    @Test
    public void testDeleteReview_ReturnsNotFoundException_ExceptionAssertion() {
        System.out.println("testDeleteReview_ReturnsNotFoundException_ExceptionAssertion");
        Assertions.assertThrows(NotFoundException.class, () -> {
            FACADE.deleteRating(new RatingDTO());

        });
    }
}
