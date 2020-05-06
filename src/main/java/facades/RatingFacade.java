package facades;

import dto.RatingDTO;
import entities.Rating;
import errorhandling.NotFoundException;
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

    public RatingDTO addRating(RatingDTO rdto) {
        EntityManager em = getEntityManager();
        Rating r = new Rating(rdto.getMovieID(), rdto.getUserName(), 
                rdto.getRating());
        try {
            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
            return new RatingDTO(r);
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
                avgRating = (double) q.getSingleResult();
            }
            return avgRating;
        } finally {
            em.close();
        }
    }
    
        public RatingDTO editRating(RatingDTO rdto) throws NotFoundException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Rating r = em.find(Rating.class, rdto.getId());
            if(r == null || !r.getMovieID().equals(rdto.getMovieID())){
                throw new NotFoundException("Movie or Review ID is wrong");
            }
            r.setRating(rdto.getRating());
            em.getTransaction().commit();
            return new RatingDTO(r);
        } finally {
            em.close();
        }
    }
    
    public RatingDTO deleteRating(RatingDTO rdto) throws NotFoundException{
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Rating r = em.find(Rating.class, rdto.getId());
            if(r == null){
                throw new NotFoundException("");
            }
            em.remove(r);
            em.getTransaction().commit();
            return new RatingDTO(r);
        } finally {
            em.close();
        }
    }

}
