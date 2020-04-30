package facades;

import entities.Rating;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 *
 */
public class RatingFacade {

    private static RatingFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private RatingFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static RatingFacade getRatingFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new RatingFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public double addRating(String movieID, int rating) {
        EntityManager em = getEntityManager();
        Rating r = new Rating(movieID, rating);
        try {
            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
            return r.getRating();
        } finally {
            em.close();
        }
    }

    public double getRatingAvg(String movieID) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT AVG(r.rating) FROM Rating r WHERE r.movieID = :id");
            q.setParameter("id", movieID);
            double avgRating = -1.0;
            if (q.getSingleResult() != null) {
                /*
                We return the avg rating for the movie if we allready have 
                ratings for the movie in the DB.
                 */
                avgRating = (double) q.getSingleResult();
                return avgRating;
            } else {
                /*
                We return a rating of 0.0 if we havent created any 
                rating for the specific movie yet.
                 */
                return avgRating;
            }
        } finally {
            em.close();
        }
    }

}
