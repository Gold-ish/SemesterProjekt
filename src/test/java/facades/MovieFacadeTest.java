package facades;

import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author rando
 */
public class MovieFacadeTest {

    private static EntityManagerFactory EMF
            = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final MovieFacade FACADE = MovieFacade.getMovieFacade(EMF);

    public MovieFacadeTest() {
    }

    @Disabled("Not implemented yet")
    @Test
    public void testGetMovieById_ReturnsMovie_EqualResults() throws Exception {
        System.out.println("testGetMovieById_ReturnsMovie_EqualResults");
    }

    @Disabled("Not implemented yet")
    @Test
    public void testGetMovieByID_NonExistentMovieID_ThrowMovieNotFoundException() {
        System.out.println("testGetMovieByID_NonExistentMovieID_ThrowMovieNotFoundException");
    }

    @Disabled("Not implemented yet")
    @Test
    public void testGetMoviesByTitle_ReturnsListOf10Movies_EqualResults() throws Exception {
        System.out.println("testGetMoviesByTitle_ReturnsListOf10Movies_EqualResults");
    }

    @Disabled("Not implemented yet")
    @Test
    public void testGetMoviesByTitle_SearchDoesNotExist_ThrowMovieNotFoundException() {
        System.out.println("testGetMoviesByTitle_SearchDoesNotExist_ThrowMovieNotFoundException");
    }

    @Disabled("Not implemented yet")
    @Test
    public void testGetMoviesByTitle_SearchTooUnspecific_ThrowIllegalArgumentException() {
        System.out.println("testGetMoviesByTitle_SearchTooUnspecific_ThrowIllegalArgumentException");
    }

}
