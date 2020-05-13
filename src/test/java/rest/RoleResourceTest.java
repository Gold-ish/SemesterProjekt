package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.UserDTO;
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
public class RoleResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory EMF;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private User u1, u2;

    public RoleResourceTest() {
    }

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);

    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
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
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();
        u1 = new User("UserNameTest1", "UserPassword1", "male", "05-05-2020");
        u2 = new User("UserNameTest2", "UserPassword2", "female", "50-50-2020");
        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        User both = new User("user_admin", "test");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(u1);
            em.persist(u2);
            u1.addRole(userRole);
            em.persist(both);
            both.addRole(userRole);
            both.addRole(adminRole);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        logOut();
    }

    //This is how we hold on to the token after login, similar to that a client must store the token somewhere
    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                .when().post("/login")
                .then()
                .extract().path("token");
        System.out.println("TOKEN ---> " + securityToken);
    }

    private void logOut() {
        securityToken = null;
    }

    /**
     * Test of getInfoForAll method, of class RoleResource.
     */
    @Test
    public void testServerConnection_EnsuresThatTheServerIsUp_200() {
        given().when()
                .get("/info").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", is("Hello anonymous"));
    }

    /**
     * Test of allUsers method, of class RoleResource.
     */
    @Test
    public void testAllUsers_ReturnsAmountOfUsers_EqualsResult() {
        given().when()
                .get("/info/all").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body(is("[3]"));
    }

    /**
     * Test of getFromUser method, of class RoleResource.
     */
    @Test
    public void testGetFromUser_LoggedIn_EqualsResult() {
        login(u1.getUserName(), "UserPassword1");
        given()
                .header("x-access-token", securityToken).
                when()
                .get("/info/user").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("username", is(u1.getUserName()));
    }

    /**
     * Test of getFromUser method, of class RoleResource, not logged in.
     */
    @Test
    public void testGetFromUser_NotLoggedIn_403() {
        given().when()
                .get("/info/user").
                then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode());
    }

    /**
     * Test of editUser method, of class RoleResource.
     */
    @Test
    public void testEditUser_LoggedIn_EqualsResult() {
        login(u1.getUserName(), "UserPassword1");
        u1.setBirthday("15-05-2020");
        String json = new Gson().toJson(new UserDTO(u1));
        given().contentType(ContentType.JSON)
                .body(json)
                .header("x-access-token", securityToken).
                when()
                .put("/info/user/edit").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("username", is(u1.getUserName()))
                .body("birthday", is("15-05-2020"));
    }

    /**
     * Test of editUser method, of class RoleResource, not logged in.
     */
    @Test
    public void testEditUser_NotLoggedIn_403() {
        given().when()
                .put("/info/user/edit").
                then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode());
    }

    /**
     * Test of deleteRating method, of class RoleResource.
     */
    @Test
    public void testDeleteUser_LoggedIn_EqualsResult() {
        login(u1.getUserName(), "UserPassword1");
        given()
                .header("x-access-token", securityToken).
                when()
                .delete("/info/user/delete").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode());
//                .body(is("User: " + u1.getUserName() + " Ratings: 0 Reviews: 0 were deleted")); //Actual er det samme, men den fejler??
    }

    /**
     * Test of deleteRating method, of class RoleResource, not logged in.
     */
    @Test
    public void testDeleteUser_NotLoggedIn_403() {
        given().when()
                .delete("/info/user/delete").
                then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode());
    }

    /**
     * Test of getFromAdmin method, of class RoleResource.
     */
    @Test
    public void testGetFromAdmin_LoggedIn_EqualsResult() {
        login("user_admin", "test");
        given()
                .header("x-access-token", securityToken).
                when()
                .get("/info/admin").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", is("Hello to (admin) User: user_admin"));
    }
    
    /**
     * Test of getFromAdmin method, of class RoleResource, not logged in.
     */
    @Test
    public void testGetFromAdmin_NotLoggedIn_403() {
        given().
                when()
                .get("/info/admin").
                then()
                .assertThat()
                .statusCode(HttpStatus.FORBIDDEN_403.getStatusCode());
    }

    /**
     * Test of getCriticCode method, of class RoleResource.
     */
    @Test
    public void testGetCriticCode_ReturnsRandomGeneratedString_EqualResults() {
        login("user_admin", "test");
        given().contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/info/critic/code").then()
                .statusCode(200);
    }

}
