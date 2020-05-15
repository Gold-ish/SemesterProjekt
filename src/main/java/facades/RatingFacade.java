package facades;

import dto.RatingDTO;
import entities.Rating;
import errorhandling.NotFoundException;
import errorhandling.UserException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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

    public List<Rating> getRatings(String user) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            TypedQuery<Rating> q = em.createQuery("SELECT r FROM Rating r "
                    + "WHERE r.user = :user", Rating.class);
            q.setParameter("user", user);
            em.getTransaction().commit();
            return q.getResultList();
        } finally {
            em.close();
        }
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
    
    public double getRatingAvgUser(String movieID) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNativeQuery("select avg(RATING) from RATING r LEFT JOIN user_roles userRoles ON r.USER = userRoles.user_name WHERE role_name = 'user' && MOVIEID = '" + movieID + "';");
            double avgRating = -1.0;
            System.out.println(q.getSingleResult());
            if (q.getSingleResult() != null) {
                avgRating = ((BigDecimal)q.getSingleResult()).doubleValue();
            }
            return avgRating;
        } finally {
            em.close();
        }
    }
    
    public double getRatingAvgCritic(String movieID) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createNativeQuery("select avg(RATING) from RATING r LEFT JOIN user_roles userRoles ON r.USER = userRoles.user_name WHERE role_name = 'critic' && MOVIEID = '" + movieID + "';");
            double avgRating = -1.0;
            System.out.println(q.getSingleResult());
            if (q.getSingleResult() != null) {
                avgRating = ((BigDecimal)q.getSingleResult()).doubleValue();
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
            if (r == null || !r.getMovieID().equals(rdto.getMovieID())) {
                throw new NotFoundException("Movie or Review ID is wrong");
            }
            r.setRating(rdto.getRating());
            em.getTransaction().commit();
            return new RatingDTO(r);
        } finally {
            em.close();
        }
    }

    public RatingDTO deleteRating(RatingDTO rdto) throws NotFoundException, UserException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Rating r = em.find(Rating.class, rdto.getId());
            if (r == null) {
                throw new NotFoundException("");
            } else if (!"adminRole".equals(rdto.getUserName())) {
                if (!r.getUser().equals(rdto.getUserName())) {
                    throw new UserException("User doesn't match the ratingID");
                }
            }
            em.remove(r);
            em.getTransaction().commit();
            return new RatingDTO(r);
        } finally {
            em.close();
        }
    }

    public List<RatingDTO> getRatingsWithMovieID(String movieID) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Rating> tq = em.createQuery("SELECT r FROM Rating r "
                    + "WHERE r.movieID = :id", Rating.class);
            tq.setParameter("id", movieID);
            List<RatingDTO> qList = new ArrayList<>();
            for (Rating r : tq.getResultList()) {
                qList.add(new RatingDTO(r));
            }
            return qList;
        } finally {
            em.close();
        }

    }

    public List<String> getTopTenMovies() {
        EntityManager em = getEntityManager();
        try {
            Query tq = em.createNativeQuery("SELECT MOVIEID FROM RATING WHERE MOVIEID IN (SELECT MOVIEID FROM RATING GROUP BY MOVIEID HAVING COUNT(*) > 1) GROUP BY MOVIEID ORDER BY AVG(rating)desc LIMIT 10;");
            List<String> list = (List<String>) (List<?>) tq.getResultList();
            return list;
        } finally {
            em.close();
        }
    }

}
