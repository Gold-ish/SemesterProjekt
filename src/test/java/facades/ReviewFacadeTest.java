package facades;

import dto.ReviewDTO;
import entities.Review;
import entities.User;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import java.util.List;
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

/*
 * @author Nina
 */
public class ReviewFacadeTest {

    private static EntityManagerFactory EMF;
    private static ReviewFacade FACADE;
    private static Review r1, r2, r3, r4, r5;
    private static User user1 = new User("testuser", "123", "other", "05-05-2020");

    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = ReviewFacade.getReviewFacade(EMF);
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        r1 = new Review("MovieID1", "user1", "Very good movie");
        r2 = new Review("MovieID2", "user1", "Very bad movie");
        r3 = new Review("MovieID3", "user1", "Speciel movie");
        r4 = new Review("MovieID1", "user1", "Not good acting");
        r5 = new Review("MovieID2", "user1", "disappointed");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Review.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(r1);
            em.persist(r2);
            em.persist(r3);
            em.persist(r4);
            em.persist(r5);
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
            em.createNamedQuery("Review.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testGetReviews_ReturnsNumberOfReviews_EqualResults() {
        System.out.println("testGetReviews_ReturnsNumberOfReviews_EqualResults");
        String movieID = "MovieID2";
        int reviewNo = FACADE.getReviews(movieID).size();
        int expectedReviewNo = 2;
        assertEquals(expectedReviewNo, reviewNo);
    }

    @Test
    public void testGetReviews_ReturnsEmptyListOfNonExistingMovieID_EqualResults() throws Exception {
        System.out.println("testGetReviews_ReturnsEmptyListOfNonExistingMovieID_EqualResults");
        String movieID = "NonExistingID";
        List<ReviewDTO> resultList = FACADE.getReviews(movieID);
        List<ReviewDTO> expectedList = new ArrayList<>();
        assertEquals(resultList, expectedList);
    }

    @Test
    public void testAddReview_ReturnsTheReview_EqualResults() {
        System.out.println("testAddReview_ReturnsTheReview_EqualResults");
        User user = new User("testuser1", "123", "other", "05-05-2020");
        ReviewDTO rdtoExpected = new ReviewDTO("DummyRev", user.getUserName(), "It was very good");
        ReviewDTO resultReview = FACADE.addReview(rdtoExpected);
        assertNotNull(resultReview.getId());
        assertEquals(resultReview.getMovieID(), rdtoExpected.getMovieID());
        assertEquals(resultReview.getReview(), rdtoExpected.getReview());
    }

    @Test
    public void testEditReview_ReturnsTheReview_EqualResults() throws NotFoundException {
        System.out.println("testEditReview_ReturnsTheReview_EqualResults");
        User user = new User("testuser2", "123", "other", "05-05-2020");
        ReviewDTO rdtoExpected = new ReviewDTO(r1.getId(), r1.getMovieID(), user.getUserName(), "ChangeReview");
        ReviewDTO resultReview = FACADE.editReview(rdtoExpected);
        assertEquals(resultReview.getId(), rdtoExpected.getId());
        assertEquals(resultReview.getMovieID(), rdtoExpected.getMovieID());
        assertEquals(resultReview.getReview(), rdtoExpected.getReview());
    }

    @Test
    public void testEditReview_ReturnsNotFoundException_ExceptionAssertion() {
        System.out.println("testEditReview_ReturnsTheReview_EqualResults");
        User user = new User("testuser3", "123", "other", "05-05-2020");
        Assertions.assertThrows(NotFoundException.class, () -> {
            FACADE.editReview(new ReviewDTO(-1, r1.getMovieID(), user.getUserName(), "ChangeReview"));

        });
    }

    @Test
    public void testDeleteReview_ReturnsReview_EqualResults() throws NotFoundException {
        System.out.println("testDeleteReview_ReturnsTheReview_EqualResults");
        ReviewDTO reviewDTO = new ReviewDTO(r1);
        ReviewDTO deleteReview = FACADE.deleteReview(reviewDTO);
        assertEquals(reviewDTO, deleteReview);
    }

    @Test
    public void testDeleteReview_ReturnsNotFoundException_ExceptionAssertion() {
        System.out.println("testDeleteReview_ReturnsNotFoundException_ExceptionAssertion");
        Assertions.assertThrows(NotFoundException.class, () -> {
            FACADE.deleteReview(new ReviewDTO());

        });
    }

}
