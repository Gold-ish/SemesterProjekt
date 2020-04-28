/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dto.MovieDTO;
import static org.junit.Assert.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author allan
 */
public class fetchFacadeTest {

    public fetchFacadeTest() {
    }

    /**
     * Test of getMovieWithID method, of class FetchFacade.
     */
    @Test
    public void testGetMovieWithID() throws Exception {
        System.out.println("getMovieWithID");
        FetchFacade facade = FetchFacade.getFetchFacade();
        MovieDTO expResult = new MovieDTO("Star Wars: Episode IV - A New Hope", "1977", "https://m.media-amazon.com/images/M/MV5BNzVlY2MwMjktM2E4OS00Y2Y3LWE3ZjctYzhkZGM3YzA1ZWM2XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg", "tt0076759");
        MovieDTO result = facade.getMovieWithID("tt0076759");
        assertEquals(expResult, result);
    }
}
