package rest;

import dto.MovieDTO;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;


/**
 *
 * @author carol
 */
public class MovieResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
        private static EntityManagerFactory emf;


    public MovieResourceTest() {
    }

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
        
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.CREATE);
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

//    @Test
//    public void testTest() {
//        assertTrue(true);
//    }
    /**
     * Test of demo method, of class MovieResource.
     */
    @Test
    public void testServerConnection() {
        given().when()
                .get("/movies").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", is("Hello World"));
    }

    /**
     * Test of getById method, of class MovieResource.
     */
    @Test
    public void testGetById() {
        MovieDTO movie = new MovieDTO("Star Wars: Episode V - The Empire Strikes Back", "1980",
                "https://m.media-amazon.com/images/M/MV5BYmU1NDRjNDgtMzhiMi00NjZmLTg5NGItZDNiZjU5NTU4OTE0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg",
                "tt0080684");
        given().when()
                .get("/movies/{id}", movie.getImdbID()).
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("Title", is(movie.getTitle()))
                .body("Year", is(movie.getYear()))
                .body("Poster", is(movie.getPoster()))
                .body("imdbID", is(movie.getImdbID()));
    }

    /**
     * Negative test of getById method, of class MovieResource with wrong id
     */
    @Test
    public void testGetById_wrongId() {
        MovieDTO movie = new MovieDTO("", "", "",
                "tt1");
        given().when()
                .get("/movies/{id}", movie.getImdbID()).
                then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", is(404))
                .body("message", is("No movie found with id: " + movie.getImdbID()));
    }

    /**
     * Test of getByTitle method, of class MovieResource.
     */
    @Test
    public void testGetByTitle() {
        String title = "star";
        int page = 1;
        given().when()
                .get("/movies/search/{title}/{page}", title, page).
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("movieDTOs", hasSize(10))
                .body("movieDTOs.Title", containsInAnyOrder("Star Trek",
                        "Star Wars: Episode I - The Phantom Menace",
                        "Star Wars: Episode II - Attack of the Clones",
                        "Star Wars: Episode III - Revenge of the Sith",
                        "Star Wars: Episode IV - A New Hope",
                        "Star Wars: Episode V - The Empire Strikes Back",
                        "Star Wars: Episode VI - Return of the Jedi",
                        "Star Wars: Episode VII - The Force Awakens",
                        "Star Wars: Episode VIII - The Last Jedi",
                        "Rogue One: A Star Wars Story"))
                .body("movieDTOs.imdbID", containsInAnyOrder("tt0076759", "tt3748528",
                        "tt0080684", "tt0086190", "tt2488496", "tt0120915",
                        "tt0121766", "tt0121765", "tt0796366", "tt2527336"));
    }

    /**
     * Negative test of getByTitle method, of class MovieResource with wrong
     * title
     */
    @Test
    public void testGetByTitle_wrongTitle() {
        String title = "x";
        int page = 1;
        given().when()
                .get("/movies/search/{title}/{page}", title, page).
                then()
                .assertThat()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR_500.getStatusCode())
                .body("code", is(500))
                .body("message", is("Internal Server Error"));
    }

    /**
     * Negative test of getByTitle method, of class MovieResource with wrong
     * pageNumber
     */
    @Test
    public void testGetByTitle_wrongPageNumber() {
        String title = "star";
        int page = 1000;
        given().when()
                .get("/movies/search/{title}/{page}", title, page).
                then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", is(404))
                .body("message", is("No movie found with the search result: " + title));
    }
}
