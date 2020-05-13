package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entities.User;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import utils.EMF_Creator;

/**
 *
 * @author carol
 */
public class RoleDemoResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory EMF;
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private User u1, u2;

    public RoleDemoResourceTest() {
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
        try {
            em.getTransaction().begin();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.persist(u1);
            em.persist(u2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
    /**
     * Test of getInfoForAll method, of class RoleDemoResource.
     */
    @Test
    public void testServerConnection_EnsuresThatTheServerIsUp_200() {
        System.out.println("testServerConnection_EnsuresThatTheServerIsUp_200");
        given().when()
                .get("/info").
                then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("msg", is("Hello anonymous"));
    }

    /**
     * Test of allUsers method, of class RoleDemoResource.
     */
    @Test
    public void testAllUsers() {
    }

    /**
     * Test of getFromUser method, of class RoleDemoResource.
     */
    @Test
    public void testGetFromUser() {
    }

    /**
     * Test of editUser method, of class RoleDemoResource.
     */
    @Test
    public void testEditUser() {
    }

    /**
     * Test of deleteRating method, of class RoleDemoResource.
     */
    @Test
    public void testDeleteRating() {
    }

    /**
     * Test of getFromAdmin method, of class RoleDemoResource.
     */
    @Test
    public void testGetFromAdmin() {
    }

}
