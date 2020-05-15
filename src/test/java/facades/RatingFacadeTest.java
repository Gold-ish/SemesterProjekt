package facades;

import dto.RatingDTO;
import entities.Rating;
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
    private static Rating r1, r2, r3, r4, r5, r6;
    private static User user1 = new User("testuser", "123", "other", "05-05-2020");

    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = RatingFacade.getRatingFacade(EMF);
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        r1 = new Rating("MovieID1", "user1", 8);
        r2 = new Rating("MovieID2", "user1", 8);
        r3 = new Rating("MovieID3", "user1", 8);
        r4 = new Rating("MovieID1", "user1", 3);
        r5 = new Rating("MovieID2", "user1", 7);
        r6 = new Rating("MovieID3", "user1", 5);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Rating.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(r1);
            em.persist(r2);
            em.persist(r3);
            em.persist(r4);
            em.persist(r5);
            em.persist(r6);
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
            em.createNamedQuery("Rating.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
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
        double expectedRating = ((double) r1.getRating() + (double) r4.getRating()) / 2;
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
    public void testAddRating_ReturnsTheRating_EqualResults() {
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
