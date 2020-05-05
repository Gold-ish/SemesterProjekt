//package facades;
//
//import dto.UserDTO;
//import entities.User;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import utils.EMF_Creator;
//
///**
// *
// * @author rando
// */
//public class UserFacadeTest {
//    private static EntityManagerFactory EMF;
//    private static UserFacade FACADE;
//    private User u1, u2;
//    private UserDTO ud1, ud2;
//
//    @BeforeAll
//    public static void setUpClass() {
//        EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.TEST,
//                EMF_Creator.Strategy.DROP_AND_CREATE);
//        FACADE = UserFacade.getUserFacade(EMF);
////        EntityManager em = EMF.createEntityManager();
////        try {
////            em.getTransaction().begin();
////            em.persist(new Role("user"));
////            em.getTransaction().commit();
////        } finally {
////            em.close();
////        }
//    }
//
//    @BeforeEach
//    public void setUp() {
//        EntityManager em = EMF.createEntityManager();
//        ud1 = new UserDTO("UserNameTest1", "UserPassword1", "male", "05-05-2020");
//        ud2 = new UserDTO("UserNameTest2", "UserPassword2", "female", "50-50-2020");
//        u1 = new User(ud1);
//        u2 = new User(ud2);
//        //u1.addRole(new Role("user"));
//        //u2.addRole(new Role("user"));
//        try {
//            em.getTransaction().begin();
//            //em.createQuery("DELETE FROM user_roles").executeUpdate();
//            em.createNamedQuery("User.deleteAllRows").executeUpdate();
//            //em.createQuery("DELETE FROM users").executeUpdate();
//            //em.persist(new Role("user"));
//            em.persist(u1);
//            em.persist(u2);
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//    }
//
//    @Test
//    public void testRegisterUser_ReturnsConfirmationString_EqualResults() {
//        System.out.println("testRegisterUser_ReturnsConfirmationString_EqualResults");
//        UserDTO udto = new UserDTO("registerUser", "registerPassword", "female", "08-08-2020");
//        String actualResult = FACADE.registerUser(udto);
//        String expectedResult = "User was created";
//        assertEquals(expectedResult, actualResult);
//    }
//    
//    
//    @Test
//    public void testReg1isterUser_ReturnsConfirmationString_EqualResults() {
//        System.out.println("testRegisterUser_ReturnsConfirmationString_EqualResults");
//        UserDTO udto = new UserDTO("registerUser1", "registerPassword", "female", "08-08-2020");
//        String actualResult = FACADE.registerUser(udto);
//        String expectedResult = "User was created";
//        assertEquals(expectedResult, actualResult);
//    }
//    
//}
