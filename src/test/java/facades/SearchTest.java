package facades;

import dto.MovieListDTO;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

/**
 *
 * @author rando
 */
public class SearchTest {
    private static EntityManagerFactory emf;
    private static FetchFacade facade;

    public SearchTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
       emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,EMF_Creator.Strategy.DROP_AND_CREATE);
       facade = FetchFacade.getFetchFacade();
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        //r1 = new RenameMe("Some txt", "More text");
        //r2 = new RenameMe("aaa", "bbb");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("RenameMe.deleteAllRows").executeUpdate();
            //em.persist(r1);
            //em.persist(r2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }
    
    @Test
    public void testAddPerson() throws IOException {    
        String searchString = "star";
        int pageNumber = 1;
        MovieListDTO actualDto = facade.getMoviesByTitle(searchString, pageNumber);
        assertEquals(actualDto.getMovieDTOs().size(), 10);
    }

}
