package facades;

import dto.ReviewDTO;
import entities.Review;
import entities.Role;
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
    private static User u1, u2;
    private static Role userRole, criticRole;

    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = ReviewFacade.getReviewFacade(EMF);
        EntityManager em = EMF.createEntityManager();
        userRole = new Role("user");
        criticRole = new Role("critic");
        u1 = new User("UserNameTest", "UserPassword1", "male", "05-05-2020");
        u1.addRole(userRole);
        u2 = new User("UserNameTestCritic", "UserPassword2", "female", "50-50-2020");
        u2.addRole(criticRole);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(userRole);
            em.persist(criticRole);
            em.persist(u1);
            em.persist(u2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        r1 = new Review("MovieID1", u1.getUserName(), "Very good movie");
        r2 = new Review("MovieID2", u1.getUserName(), "Very bad movie");
        r3 = new Review("MovieID3", u2.getUserName(), "Speciel movie");
        r4 = new Review("MovieID1", u2.getUserName(), "Not good acting");
        r5 = new Review("MovieID2", u2.getUserName(), "disappointed");
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

    @AfterAll
    public static void cleanDatabase() {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            //em.createNamedQuery("Review.deleteAllRows").executeUpdate();
            //em.createNamedQuery("User.deleteAllRows").executeUpdate();
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
    public void testGetReviews_RoleOnReviewIsCritic_EqualResults() {
        System.out.println("testGetReviews_RoleOnReviewIsCritic_EqualResults");
        String movieID = "MovieID3";
        List<ReviewDTO> dtolist = FACADE.getReviews(movieID);
        int expectedReviewNo = 1;
        assertEquals(expectedReviewNo, dtolist.size());
        assertEquals("critic", dtolist.get(0).getRole());
        assertEquals(r3.getId(), dtolist.get(0).getId());
        assertEquals(r3.getMovieID(), dtolist.get(0).getMovieID());
        assertEquals(r3.getReview(), dtolist.get(0).getReview());
        assertEquals(r3.getUser(), dtolist.get(0).getUser());
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
