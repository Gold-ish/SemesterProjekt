package facades;

import entities.Review;
import errorhandling.NotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
    private static Review r1, r2, r3, r4, r5, r6;

    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = ReviewFacade.getReviewFacade(EMF);
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        r1 = new Review("MovieID1", "Very good movie");
        r2 = new Review("MovieID2", "Very bad movie");
        r3 = new Review("MovieID3", "Speciel movie");
        r4 = new Review("MovieID1", "Not good acting");
        r5 = new Review("MovieID2", "disappointed");
        r6 = new Review("MovieID4", "");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Review.deleteAllRows").executeUpdate();
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

    @Test
    public void testGetReview_ReturnsReviewOfNonExistingReview_EqualResults() throws Exception {
        System.out.println("testGetReview_ReturnsReviewOfNonExistingReview_EqualResults");
        String movieID = "NonExistingID";
        String review = FACADE.getReview(movieID);
        String expectedReview = null;
        assertEquals(expectedReview, review);
    }
    
    @Test
    public void testGetReviews_ReturnsNumberOfReviews_EqualResults() {
        System.out.println("testGetReview_ReturnsReviewOfNonExistingReview_EqualResults");
        String movieID = "MovieID2";
        int reviewNo = FACADE.getReviews(movieID).size();
        int expectedReviewNo = 2;
        assertEquals(expectedReviewNo, reviewNo);
    }
    
    @Test
    public void testGetReviews_ReturnsNumberOfReviewsWhereEmpty_EqualResults() {
        System.out.println("testGetReviews_ReturnsNumberOfReviewsWhereEmpty_EqualResults");
        String movieID = "MovieID4";
        int reviewNo = FACADE.getReviews(movieID).size();
        int expectedReviewNo = 0;
        assertEquals(expectedReviewNo, reviewNo);
    }
    
    @Test
    public void testAddReview_ReturnsTheReview_EqualResults() {
        System.out.println("testAddReview_ReturnsTheReview_EqualResults");
        Review dummy = new Review("DummyRev", "It was very good");
        String addReviewReturn = FACADE.addReview(dummy.getMovieID(), dummy.getReview());
        String expectedReviewReturned = dummy.getReview();
        assertEquals(expectedReviewReturned, addReviewReturn);
    }
    
    
    @Test
    public void testEditReview_ReturnsTheReview_EqualResults() throws NotFoundException {
        System.out.println("testEditReview_ReturnsTheReview_EqualResults");
        String editReviewReturn = FACADE.editReview(r1.getId(), r1.getMovieID(), "I LOVE THE THIS MOVIE!");
        assertEquals("I LOVE THE THIS MOVIE!", editReviewReturn);
    }
    
    @Test
    public void testDeleteReview_ReturnsTheReview_EqualResults() throws NotFoundException {
        System.out.println("testDeleteReview_ReturnsTheReview_EqualResults");
        String deleteReviewReturn = FACADE.deleteReview(r1.getId());
        assertEquals("review " + r1.getId()+ " deleted", deleteReviewReturn);
    }
    
    //Neagive testing
    //@Test
    //public void testNegative_NegativeTesting_Negative() {
    //      System.out.println("ReviewNegativeTesting");
    //}
    

}
