package facades;

import entities.Review;
import errorhandling.NotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
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

    public String addReview(String movieID, String review) {
        EntityManager em = getEntityManager();
        Review r = new Review(movieID, review);
        try {
            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
            return r.getReview();
        } finally {
            em.close();
        }
    }
    
    public String getReview(String movieID) throws NoResultException {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT r FROM Review r WHERE r.movieID = :id");
            q.setParameter("id", movieID);
            String review = null;
            try {
                if (q.getSingleResult() != null) {
                review = (String) q.getSingleResult();
                return review;
            } else {
                return null;
            }
            } catch (NoResultException nre){
                return null;
            }
        } finally {
            em.close();
        }
    }

    public List<Review> getReviews(String movieID) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Review> tq = em.createQuery("SELECT r FROM Review r WHERE r.movieID = :id", Review.class);
            tq.setParameter("id", movieID);
            return tq.getResultList();
        } finally {
            em.close();
        }

    }
    
    public String editReview(int id, String movieID, String review) throws NotFoundException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Review r = em.find(Review.class, id);
            if(r == null || !r.getMovieID().equals(movieID)){
                throw new NotFoundException();
            }
            r.setReview(review);
            em.getTransaction().commit();
            return r.getReview();
        } finally {
            em.close();
        }
    }

}
