/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.MovieDTO;
import errorhandling.MovieNotFoundException;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author allan
 */
public class fetchFacadeTest {

    private static final FetchFacade FACADE = FetchFacade.getFetchFacade();

    public fetchFacadeTest() {
    }

    /**
     * Test of getMovieById method, of class FetchFacade.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetMovieByID() throws Exception {
        System.out.println("getMovieWithID");
        MovieDTO expResult = new MovieDTO("Star Wars: Episode IV - A New Hope", "1977", "https://m.media-amazon.com/images/M/MV5BNzVlY2MwMjktM2E4OS00Y2Y3LWE3ZjctYzhkZGM3YzA1ZWM2XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg", "tt0076759");
        MovieDTO result = FACADE.getMovieById("tt0076759");
        assertEquals(expResult, result);
    }

    @Test
    public void testGetMovieByIDFail() {
        System.out.println("getMovieWithIDFail");
        Exception exception = assertThrows(MovieNotFoundException.class, () -> {
            FACADE.getMovieById("tt00767555555559");
        });

        String expectedMessage = "No movie found with id: tt00767555555559";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
