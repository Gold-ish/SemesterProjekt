package facades;

import entities.RenameMe;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;


//Uncomment the line below, to temporarily disable this test
//@Disabled
public class FacadeExampleTest {

    private static EntityManagerFactory emf;
    private static FacadeExample facade;
    private static RenameMe r1, r2;

    public FacadeExampleTest() {
    }

    //@BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactory(
                "pu",
                "jdbc:mysql://localhost:3307/startcode_test",
                "dev",
                "ax2",
                EMF_Creator.Strategy.CREATE);
        facade = FacadeExample.getFacadeExample(emf);
    }

    /*   **** HINT **** 
        A better way to handle configuration values, compared to the UNUSED example above, is to store those values
        ONE COMMON place accessible from anywhere.
        The file config.properties and the corresponding helper class utils.Settings is added just to do that. 
        See below for how to use these files. This is our RECOMENDED strategy
     */
    @BeforeAll
    public static void setUpClassV2() {
       emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST,Strategy.DROP_AND_CREATE);
       facade = FacadeExample.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        r1 = new RenameMe("Some txt", "More text");
        r2 = new RenameMe("aaa", "bbb");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("RenameMe.deleteAllRows").executeUpdate();
            em.persist(r1);
            em.persist(r2);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change these method 
    @Test
    public void testAFacadeMethod() {
        assertEquals(2, facade.getRenameMeCount(), "Expects two rows in the database");
    }
    
     @Test
    public void testAddPerson() {
        
        RenameMe rm = new RenameMe("hello", "hello");
        RenameMe postedresult = facade.addRenameMe("hello");
        assertTrue(rm.equals(postedresult));
    }

    @Test
    public void testEditPerson() throws SQLException {
        RenameMe rm = new RenameMe("hello from the other side", "hello from the other side");
        RenameMe putresult = facade.editRenameMe("hello from the other side", r1.getId());
        assertTrue(rm.equals(putresult));
    }
    
    @Test
    public void testDelete() throws SQLException{
        assertEquals(2, facade.getRenameMeCount(), "Expects two rows in the database");
        facade.delete(r2.getId());
        assertEquals(1, facade.getRenameMeCount(), "Expects one rows in the database");

    }

}
