package facades;

import dto.MovieListDTO;
import dto.RatingDTO;
import dto.ReviewDTO;
import dto.SpecificMovieDTO;
import dto.TopTenMoviesDTO;
import entities.Rating;
import entities.Review;
import entities.Role;
import entities.User;
import errorhandling.MovieNotFoundException;
import errorhandling.NotFoundException;
import errorhandling.UserException;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author rando
 */
public class MovieFacadeTest {

    private static EntityManagerFactory EMF;
    private static MovieFacade FACADE;
    private Rating r1, r2, r3;
    private Review re1;
    private static User user1 ,user2, user3;
    private static Role userRole, criticRole;

    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = MovieFacade.getMovieFacade(EMF);
         EntityManager em = EMF.createEntityManager();
        userRole = new Role("user");
        criticRole = new Role("critic");
        user1 = new User("testuser", "123", "other", "05-05-2020");
        user1.addRole(userRole);
        user2 = new User("UserNameTestCritic", "UserPassword2", "female", "50-50-2020");
        user2.addRole(criticRole);
        user3 = new User("testuser1", "123", "other", "05-05-2020");
        user3.addRole(userRole);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(userRole);
            em.persist(criticRole);
            em.persist(user1);
            em.persist(user2);
            em.persist(user3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        r1 = new Rating("tt0076759", user1.getUserName(), 8);
        r2 = new Rating("tt0076759", user2.getUserName(), 3);
        r3 = new Rating("tt0076759", user3.getUserName(), 10);
        re1 = new Review("MovieID1", user1.getUserName(), "Very good movie");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Rating.deleteAllRows").executeUpdate();
            em.createNamedQuery("Review.deleteAllRows").executeUpdate();
            em.persist(r1);
            em.persist(r2);
            em.persist(r3);
            em.persist(re1);
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
    public void testGetMovieById_ReturnsMovie_EqualResults() throws IOException, MovieNotFoundException {
        System.out.println("testGetMovieById_ReturnsMovie_EqualResults");
        String movieID = r1.getMovieID();
        SpecificMovieDTO mdto = FACADE.getMovieById(movieID);
        double expectedRatingOnMovieDTO = ((double) r1.getRating() + (double) r3.getRating()) / 2;
        assertEquals(expectedRatingOnMovieDTO, mdto.getAvgRating());
    }

    @Test
    public void testGetMovieByID_NonExistentMovieID_ThrowMovieNotFoundException() {
        System.out.println("testGetMovieByID_NonExistentMovieID_ThrowMovieNotFoundException");
        Exception exception = assertThrows(MovieNotFoundException.class, () -> {
            FACADE.getMovieById("NonExistentID");
        });
        String expectedMessage = "No movie found with id: NonExistentID";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testGetMoviesByTitle_ReturnsListOf10Movies_EqualResults() throws IOException, MovieNotFoundException, InterruptedException  {
        System.out.println("testGetMoviesByTitle_ReturnsListOf10Movies_EqualResults");
        String searchString = "Star";
        int pageNumber = 1;
        MovieListDTO actualDto = FACADE.getMoviesByTitle(searchString, pageNumber);
        assertEquals(10, actualDto.getMovieDTOs().size());
    }

    @Test
    public void testGetMoviesByTitle_ChecksIfMoviesHaveRating_EqualResults() throws IOException, MovieNotFoundException, InterruptedException{
        System.out.println("testGetMoviesByTitle_ReturnsListOf10Movies_EqualResults");
        String searchString = "Planet";
        int pageNumber = 1;
        MovieListDTO actualDto = FACADE.getMoviesByTitle(searchString, pageNumber);
        assertEquals(-1.0, actualDto.getMovieDTOs().get(0).getAvgRating());
    }
    
    @Test
    public void testGetMoviesByTitle_SearchDoesNotExist_ThrowMovieNotFoundException() {
        System.out.println("testGetMoviesByTitle_SearchDoesNotExist_ThrowMovieNotFoundException");
        String searchString = "NonExistentMovie";
        int pageNumber = 1;
        Exception exception = assertThrows(MovieNotFoundException.class, () -> {
            FACADE.getMoviesByTitle(searchString, pageNumber);
        });
        String expectedMessage = "No movie found with the search result: NonExistentMovie";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    public void testGetMoviesByTitle_SearchTooUnspecific_ThrowIllegalArgumentException() {
        System.out.println("testGetMoviesByTitle_SearchTooUnspecific_ThrowIllegalArgumentException");
        String searchString = "a";
        int pageNumber = 1;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            FACADE.getMoviesByTitle(searchString, pageNumber);
        });
        String expectedMessage = "Too many search results, not specific enough search: a";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    
    @Test
    public void testGetTopTenMovies_ReturnsListNrOf10Movies_EqualResults() throws InterruptedException {
        System.out.println("testGetTopTenMovies_ReturnsListNrOf10Movies_EqualResults");
        TopTenMoviesDTO actualDto = FACADE.getTopTenMovies();
        assertEquals(1, actualDto.getMovieDTOs().size());
    }
    
    @Test
    public void testDeleteRating_ReturnsRatingDTO_EqualResults() throws NotFoundException, UserException {
        System.out.println("testDeleteRating_ReturnsRatingDTO_EqualResults");
        RatingDTO ratingDTO = new RatingDTO(r1);
        RatingDTO rdto = FACADE.deleteRating(ratingDTO);
        assertEquals(rdto, ratingDTO);
    }
    
    @Test
    public void testDeleteReview_ReturnsReviewDTO_EqualResults() throws NotFoundException, UserException {
        System.out.println("testDeleteReview_ReturnsReviewDTO_EqualResults");
        ReviewDTO reviewDTO = new ReviewDTO(re1);
        ReviewDTO rdto = FACADE.deleteReview(reviewDTO);
        assertEquals(rdto, reviewDTO);
    }
}
