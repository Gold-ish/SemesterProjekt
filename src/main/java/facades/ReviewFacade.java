package facades;

import dto.ReviewDTO;
import entities.Review;
import errorhandling.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

    public List<ReviewDTO> getReviews(String movieID) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Review> tq = em.createQuery("SELECT r FROM Review r "
                    + "WHERE r.movieID = :id", Review.class);
            tq.setParameter("id", movieID);
            List<ReviewDTO> qList = new ArrayList<>();
            for (Review r : tq.getResultList()) {
                qList.add(new ReviewDTO(r));
            }
            return qList;
        } finally {
            em.close();
        }

    }
    
    public ReviewDTO editReview(ReviewDTO rdto) throws NotFoundException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Review r = em.find(Review.class, rdto.getId());
            if(r == null || !r.getMovieID().equals(rdto.getMovieID())){
                throw new NotFoundException();
            }
            r.setReview(rdto.getReview());
            em.getTransaction().commit();
            return new ReviewDTO(r);
        } finally {
            em.close();
        }
    }
    
    public ReviewDTO deleteReview(ReviewDTO review) throws NotFoundException{
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Review r = em.find(Review.class, review.getId());
            if(r == null){
                throw new NotFoundException();
            }
            em.remove(r);
            em.getTransaction().commit();
            return new ReviewDTO(r);
        } finally {
            em.close();
        }
    }

}
