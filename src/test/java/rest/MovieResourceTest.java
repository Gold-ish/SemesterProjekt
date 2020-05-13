package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.MovieDTO;
import dto.RatingDTO;
import dto.ReviewDTO;
import dto.SpecificMovieDTO;
import entities.Rating;
import entities.Review;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
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
import org.junit.jupiter.api.BeforeEach;
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
    private static EntityManagerFactory EMF;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private Review re1, re2;
    private Rating r1, r2;
    private static User user1 = new User("testuser", "123", "other", "05-05-2020");

    public MovieResourceTest() {
    }

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);

    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.CREATE);
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
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Review.deleteAllRows").executeUpdate();
            em.createNamedQuery("Rating.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        re1 = new Review("tt0076759", "user1", "Good movie!");
        re2 = new Review("tt0076759", "user1", "Best movie ever");
        r1 = new Rating("tt0076759", "user1", 8);
        r2 = new Rating("tt0076759", "user1", 3);
        Role userRole = new Role("user");
        Role criticRole = new Role("critic");
        User user = new User("user", "test");
        user.addRole(userRole);
        User critic = new User("critic", "test");
        critic.addRole(criticRole);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Review.deleteAllRows").executeUpdate();
            em.createNamedQuery("Rating.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(userRole);
            em.persist(criticRole);
            em.persist(user);
            em.persist(critic);
            em.persist(r1);
            em.persist(r2);
            em.persist(re1);
            em.persist(re2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    /**
     * Test of demo method, of class MovieResource.
     */
    @Test
    public void testServerConnection_EnsuresThatTheServerIsUp_200() {
        System.out.println("testServerConnection_EnsuresThatTheServerIsUp_200");
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
    public void testGetMovieById_ReturnsMovie_EqualResults() {
        System.out.println("testGetMovieById_ReturnsMovie_EqualResults");
        SpecificMovieDTO movie = new SpecificMovieDTO("Star Wars: Episode V - The Empire Strikes Back", "1980", "PG", "20 Jun 1980", "124 min", "Action, Adventure, Fantasy, Sci-Fi", "Irvin Kershner", "Mark Hamill, Harrison Ford, Carrie Fisher, Billy Dee Williams",
                "Luke Skywalker, Han Solo, Princess Leia and Chewbacca face attack by the Imperial forces and its AT-AT walkers on the ice planet Hoth. While Han and Leia escape in the Millennium Falcon, Luke travels to Dagobah in search of Yoda. Only with the Jedi master's help will Luke survive when the dark side of the Force beckons him into the ultimate duel with Darth Vader.",
                "English", "Won 1 Oscar. Another 24 wins & 20 nominations.", "https://m.media-amazon.com/images/M/MV5BYmU1NDRjNDgtMzhiMi00NjZmLTg5NGItZDNiZjU5NTU4OTE0XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg",
                "tt0080684", "movie", "21 Sep 2004", "Twentieth Century Fox");
        given().when()
                .get("/movies/{id}", movie.getImdbID()).
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("Title", is(movie.getTitle()))
                .body("Year", is(movie.getYear()))
                .body("Poster", is(movie.getPoster()))
                .body("imdbID", is(movie.getImdbID()))
                .body("Rated", is(movie.getRated()))
                .body("Released", is(movie.getReleased()))
                .body("Runtime", is(movie.getRuntime()))
                .body("Genre", is(movie.getGenre()))
                .body("Director", is(movie.getDirector()))
                .body("Actors", is(movie.getActors()))
                .body("Plot", is(movie.getPlot()))
                .body("Language", is(movie.getLanguage()))
                .body("Awards", is(movie.getAwards()))
                .body("Type", is(movie.getType()))
                .body("DVD", is(movie.getDVD()))
                .body("Production", is(movie.getProduction()));
    }

    /**
     * Negative test of getById method, of class MovieResource with wrong id
     */
    @Test
    public void testGetMovieByID_NonExistentMovieID_404MovieNotFoundException() {
        System.out.println("testGetMovieByID_NonExistentMovieID_404MovieNotFoundException");
        MovieDTO movie = new MovieDTO("", "0", "", "tt1", "", "", "", "", "");
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
    public void testGetMoviesByTitle_ReturnsListOf10Movies_EqualResults() {
        System.out.println("testGetMoviesByTitle_ReturnsListOf10Movies_EqualResults");
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
    public void testGetMoviesByTitle_SearchTooUnspecific_404IllegalArgumentException() {
        System.out.println("testGetMoviesByTitle_SearchTooUnspecific_404IllegalArgumentException");
        String title = "x";
        int page = 1;
        given().when()
                .get("/movies/search/{title}/{page}", title, page).
                then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", is(404))
                .body("message", is("Too many search results, not specific enough search: x"));
    }

    /**
     * Negative test of getByTitle method, of class MovieResource with wrong
     * pageNumber
     */
    @Test
    public void testGetMoviesByTitle_PageNumberDoesNotExist_404MovieNotFoundException() {
        System.out.println("testGetMoviesByTitle_PageNumberDoesNotExist_404MovieNotFoundException");
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

    @Test
    public void testGetMoviesByTitle_SearchDoesNotExist_404MovieNotFoundException() {
        System.out.println("testGetMoviesByTitle_SearchDoesNotExist_404MovieNotFoundException");
        String title = "qwertyuiop";
        int page = 1;
        given().when()
                .get("/movies/search/{title}/{page}", title, page).
                then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND_404.getStatusCode())
                .body("code", is(404))
                .body("message", is("No movie found with the search result: " + title));
    }

    @Test
    public void testAddRating_ReturnsRating_EqualResults() {
        System.out.println("testAddRating_ReturnsRating_EqualResults");
        User user = new User("testuser1", "123", "other", "05-05-2020");
        RatingDTO rating = new RatingDTO("tt0080684", user.getUserName(), 8);
        String json = GSON.toJson(rating);
        login("user", "test");
        given().contentType(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(json)
                .when()
                .post("/movies/add/rating")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("rating", is(8));
    }

    @Test
    public void testEditRating_ReturnsRating_EqualResults() {
        System.out.println("testEditRating_ReturnsRating_EqualResults");
        RatingDTO rating = new RatingDTO(r1.getId(), "tt0076759", user1.getUserName(), 10);
        String json = GSON.toJson(rating);
        login("user", "test");
        given().contentType(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(json)
                .when()
                .put("/movies/edit/rating")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("rating", is(10))
                .body("user", is("user1"));

    }

    //@Test
    public void testDeleteRating_ReturnsRating_EqualResults() {
        System.out.println("testDeleteRating_ReturnsRating_EqualResults");
        String json = GSON.toJson(new RatingDTO(r2));
        given().contentType(ContentType.JSON)
                .body(json)
                .delete("/movies/delete/rating")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", is(r2.getId()))
                .body("movieID", is("tt0076759"))
                .body("rating", is(3));
    }

    @Test
    public void testAddReview_ReturnsReview_EqualResults() {
        System.out.println("testAddReview_ReturnsReview_EqualResults");
        User user = new User("testuser3", "123", "other", "05-05-2020");
        ReviewDTO review = new ReviewDTO("tt0080684", user.getUserName(), "Very good movie");
        String json = GSON.toJson(review);
        login("user", "test");
        given().contentType(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(json)
                .when()
                .post("/movies/add/review")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("review", is("Very good movie"));
    }

    @Test
    public void testEditReview_ReturnsReview_EqualResults() {
        System.out.println("testEditReview_ReturnsReview_EqualResults");
        ReviewDTO review = new ReviewDTO(re1.getId(), "tt0076759", "user1",
                "Very good movie");
        String json = GSON.toJson(review);
        login("user", "test");
        given().contentType(ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(json)
                .when()
                .put("/movies/edit/review")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("review", is("Very good movie"))
                .body("user", is("user1"));
    }

    //@Test
    public void testDeleteReview_ReturnsReview_EqualResults() {
        System.out.println("testDeleteReview_ReturnsReview_EqualResults");
        ReviewDTO review = new ReviewDTO(re1);
        String json = GSON.toJson(review);
        given().contentType(ContentType.JSON)
                .body(json)
                .delete("/movies/delete/review").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", is(re1.getId()))
                .body("movieID", is("tt0076759"))
                .body("review", is("Good movie!"));
    }
}
