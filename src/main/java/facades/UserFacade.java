package facades;

import dto.UserDTO;
import entities.Rating;
import entities.Review;
import entities.Role;
import entities.User;
import errorhandling.AuthenticationException;
import errorhandling.UserException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.RollbackException;

/**
 * @author lam@cphbusiness.dk
 */
public class UserFacade {

    private static UserFacade instance;
    private static EntityManagerFactory emf;
    private final RatingFacade ratingFacade = RatingFacade.getRatingFacade(emf);
    private final ReviewFacade reviewFacade = ReviewFacade.getReviewFacade(emf);

    private UserFacade() {
    }

    /**
     *
     * @param _emf
     * @return the instance of this facade.
     */
    public static UserFacade getUserFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public User getVeryfiedUser(String username, String password) throws AuthenticationException {
        EntityManager em = getEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user == null || !user.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public String registerUser(UserDTO userDTO) throws UserException {
        EntityManager em = getEntityManager();
        User userToAdd = new User(userDTO);
        userToAdd.addRole(new Role("user"));
        try {
            em.getTransaction().begin();
            em.persist(userToAdd);
            em.getTransaction().commit();
            return "User was created";
        } catch (RollbackException e) {
            throw new UserException("Username already taken.");
        } finally {
            em.close();
        }
    }

    public User getUser(String username) {
        EntityManager em = getEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            if (user != null) {
                List<Rating> ratings = ratingFacade.getRatings(user);
                user.setRatings(ratings);
                List<Review> reviews = reviewFacade.getReviews(user);
                user.setReviews(reviews);
            }
            return user;
        } finally {
            em.close();
        }
    }

}
