package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.UserDTO;
import entities.Role;
import entities.User;
import facades.UserFacade;
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
 * @author rando
 */
public class UserResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory EMF;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static UserFacade FACADE;
    private User u1, u2, u3;
    private static Role userRole, criticRole;

    public UserResourceTest() {
    }

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);

    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST, EMF_Creator.Strategy.DROP_AND_CREATE);
        FACADE = UserFacade.getUserFacade(EMF);
        EntityManager em = EMF.createEntityManager();
        userRole = new Role("user");
        criticRole = new Role("critic");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.persist(userRole);
            em.persist(criticRole);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
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

    @BeforeEach
    public void setUp() {
        EntityManager em = EMF.createEntityManager();

        u1 = new User("UserNameTest1", "UserPassword1", "male", "05-05-2020");
        u1.addRole(userRole);
        u2 = new User("UserNameTest2", "UserPassword2", "female", "50-50-2020");
        u2.addRole(userRole);
        u3 = new User("UserNameTest3", "UserPassword3", "other", "04-04-2020");
        u3.addRole(criticRole);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(u1);
            em.persist(u2);
            em.persist(u3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testRegisterUser_ReturnsConfirmationString_EqualResults() {
        System.out.println("testRegisterUser_ReturnsConfirmationString_EqualResults");
        UserDTO udto = new UserDTO("registerUser", "registerPassword", "female", "08-08-2020", "user");
        String json = GSON.toJson(udto);
        given().contentType(ContentType.JSON)
                .body(json)
                .post("/login/register").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("creation", is("User was created"));
    }
    
    @Test
    public void testRegisterCritic_ReturnsConfirmationString_EqualResults() {
        System.out.println("testRegisterCritic_ReturnsConfirmationString_EqualResults");
        UserDTO udto = new UserDTO("registerCritic", "registerPassword", "female", "08-08-2020", "critic");
        String json = GSON.toJson(udto);
        given().contentType(ContentType.JSON)
                .body(json)
                .post("/login/register").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("creation", is("User was created"));
    }

    @Test
    public void testRegisterUser_RegisteringExistingUser_ThrowsUserException() {
        System.out.println("testRegisterUser_RegisteringExistingUser_ThrowsUserException");
        UserDTO udto = new UserDTO(u1.getUserName(), "registerPassword", "female", "08-08-2020", "user");
        String json = GSON.toJson(udto);
        given().contentType(ContentType.JSON)
                .body(json)
                .post("/login/register").
                then()
                .assertThat()
                .statusCode(HttpStatus.CONFLICT_409.getStatusCode())
                .body("code", is(409))
                .body("message", is("Username already taken."));
    }
}
