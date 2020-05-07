package facades;

import dto.MovieListDTO;
import dto.SpecificMovieDTO;
import entities.Rating;
import entities.User;
import errorhandling.MovieNotFoundException;
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
    private Rating r1, r2;
    private static User user1 = new User("testuser", "123", "other", "05-05-2020");

    @BeforeAll
    public static void setUpClass() {
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,
                EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = MovieFacade.getMovieFacade(EMF);
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        r1 = new Rating("tt0076759", "user1", 8);
        r2 = new Rating("tt0076759", "user1", 3);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Rating.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(r1);
            em.persist(r2);
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
        double expectedRatingOnMovieDTO = ((double) r1.getRating() + (double) r2.getRating()) / 2;
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
    public void testGetMoviesByTitle_ReturnsListOf10Movies_EqualResults() throws IOException, MovieNotFoundException  {
        System.out.println("testGetMoviesByTitle_ReturnsListOf10Movies_EqualResults");
        String searchString = "Star";
        int pageNumber = 1;
        MovieListDTO actualDto = FACADE.getMoviesByTitle(searchString, pageNumber);
        assertEquals(10, actualDto.getMovieDTOs().size());
    }

    @Test
    public void testGetMoviesByTitle_ChecksIfMoviesHaveRating_EqualResults() throws IOException, MovieNotFoundException{
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

}
