package facades;

import dto.UserDTO;
import entities.CriticCode;
import entities.Rating;
import entities.Review;
import entities.User;
import errorhandling.AuthenticationException;
import errorhandling.NotFoundException;
import errorhandling.UserException;
import errorhandling.WrongCriticCodeException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
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
                throw new AuthenticationException("Invalid username or password");
            }
        } finally {
            em.close();
        }
        return user;
    }

    public String registerUser(UserDTO userDTO) throws UserException, WrongCriticCodeException {
        EntityManager em = getEntityManager();
        if(userDTO.getRole() == null || userDTO.getRole().isEmpty()){
            userDTO.setRole("user");
        }else{
            if(AdminFacade.getAdminFacade(emf).verifyCriticCode(new CriticCode(userDTO.getRole()))){
                userDTO.setRole("critic");
            }else{
                throw new WrongCriticCodeException("incorrect critic code");
            }
        }
        User userToAdd = new User(userDTO);
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

    public UserDTO getUser(String username) throws UserException {
        EntityManager em = getEntityManager();
        User user;
        try {
            user = em.find(User.class, username);
            UserDTO userdto = new UserDTO(user);
            List<Rating> ratings = ratingFacade.getRatings(user.getUserName());
            userdto.setRatings(ratings);
            List<Review> reviews = reviewFacade.getReviewsForUser(user.getUserName());
            userdto.setReviews(reviews);
            return userdto;
        } catch (RollbackException e) {
            //This error should not be able to be thrown.
            throw new UserException("Can't find user.");
        } finally {
            em.close();
        }
    }

    public UserDTO editUser(String username, UserDTO userDTO) throws UserException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User u = em.find(User.class, username);
            u.setGender(userDTO.getGender());
            u.setBirthday(userDTO.getBirthday());
            em.getTransaction().commit();
            return new UserDTO(u);
        } catch (RollbackException e) {
            throw new UserException("Can't find user to edit.");
        } finally {
            em.close();
        }
    }

    public String deleteUser(String username) throws NotFoundException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            User u = em.find(User.class, username);
            if (u == null) {
                throw new NotFoundException("Cannot find user");
            }
            Query qRating = em.createQuery("DELETE FROM Rating r WHERE r.user = :uName");
            int deletedRatingCount = qRating.setParameter("uName", username).executeUpdate();
            Query qReview = em.createQuery("DELETE FROM Review r WHERE r.user = :uName");
            int deletedReviewCount = qReview.setParameter("uName", username).executeUpdate();
            em.remove(u);
            em.getTransaction().commit();
            return "User: " + username + " Ratings: " + deletedRatingCount + " Reviews: " + deletedReviewCount + " were deleted";
        } finally {
            em.close();
        }
    }
}
