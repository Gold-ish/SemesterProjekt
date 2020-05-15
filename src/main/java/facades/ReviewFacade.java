package facades;

import dto.ReviewDTO;
import entities.Review;
import errorhandling.NotFoundException;
import errorhandling.UserException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/*
 * @author Nina
 */
public class ReviewFacade {

    private static ReviewFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private ReviewFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static ReviewFacade getReviewFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ReviewFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ReviewDTO addReview(ReviewDTO rdto) {
        EntityManager em = getEntityManager();
        Review r = new Review(rdto.getMovieID(), rdto.getUser(),
                rdto.getReview());
        try {
            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
            return new ReviewDTO(r);
        } finally {
            em.close();
        }
    }

    public List<Review> getReviewsForUser(String user) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Review> tq = em.createQuery("SELECT r FROM Review r "
                    + "WHERE r.user = :user", Review.class);
            tq.setParameter("user", user);
            return tq.getResultList();
        } finally {
            em.close();
        }
    }

    public List<ReviewDTO> getReviews(String movieID) {
        EntityManager em = getEntityManager();
        try {
            Query tq = em.createNativeQuery("select ID, MOVIEID, REVIEW, USER, role_name AS role from users join REVIEW on user_name = USER join user_roles on users.user_name = user_roles.user_name where MOVIEID = '" + movieID + "'");
            List<ReviewDTO> returnList = new ArrayList<>();
            for (Object[] o : (List<Object[]>) tq.getResultList()) {
                returnList.add(new ReviewDTO((Number) o[0], (String) o[1], (String) o[3], (String) o[2], (String) o[4]));

            }
            return returnList;
        } finally {
            em.close();
        }

    }

    public ReviewDTO editReview(ReviewDTO rdto) throws NotFoundException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Review r = em.find(Review.class, rdto.getId());
            if (r == null || !r.getMovieID().equals(rdto.getMovieID())) {
                throw new NotFoundException();
            }
            r.setReview(rdto.getReview());
            em.getTransaction().commit();
            return new ReviewDTO(r);
        } finally {
            em.close();
        }
    }

    public ReviewDTO deleteReview(ReviewDTO review) throws NotFoundException, UserException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Review r = em.find(Review.class, review.getId());
            if (r == null) {
                throw new NotFoundException("");
            } else if (!"adminRole".equals((review.getUser()))) {
                if (!r.getUser().equals(review.getUser())) {
                    throw new UserException("User doesn't match the reviewID");
                }
            }
            em.remove(r);
            em.getTransaction().commit();
            return new ReviewDTO(r);
        } finally {
            em.close();
        }
    }

}
