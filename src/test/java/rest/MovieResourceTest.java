package rest;

import dto.MovieDTO;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 *
 * @author carol
 */
public class MovieResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;

    public MovieResourceTest() {
    }

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }
    
    @AfterAll
    public static void closeTestServer() {
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
                .statusCode(HttpStatus.OK_200.getStatusCode());
    }
    
//
//    /**
//     * Test of getByTitle method, of class MovieResource.
//     */
//    @Test
//    public void testGetByTitle() {
//    }
}
