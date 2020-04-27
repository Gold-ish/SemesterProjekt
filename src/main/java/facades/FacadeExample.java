package facades;

import entities.RenameMe;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class FacadeExample {

    private static FacadeExample instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private FacadeExample() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static FacadeExample getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new FacadeExample();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //TODO Remove/Change this before use
    public long getRenameMeCount() {
        EntityManager em = getEntityManager();
        try {
            long renameMeCount = (long) em.createQuery("SELECT COUNT(r) FROM RenameMe r").getSingleResult();
            return renameMeCount;
        } finally {
            em.close();
        }
    }
    //IMPORTANT DO NOT RETURN ENTITY CLASS! MAKE A DTO
    public RenameMe addRenameMe(String str) {
        EntityManager em = getEntityManager();
        RenameMe rm = new RenameMe(str, str);
        try {
            em.getTransaction().begin();
            em.persist(rm);
            em.getTransaction().commit();
            return rm;
        } finally {
            em.close();
        }
    }

    public RenameMe editRenameMe(String str, Long id) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            RenameMe rmDB = em.find(RenameMe.class, id);
            if (rmDB == null) {
                throw new SQLException("Nothing found with id.");
            }
            rmDB.setDummyStr1(str);
            rmDB.setDummyStr2(str);
            em.getTransaction().commit();
            return rmDB;
        } finally {
            em.close();
        }
    }

    public void delete(long id) throws SQLException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            RenameMe rmDB = em.find(RenameMe.class, id);
            if (rmDB == null) {
                throw new SQLException("Nothing found with id.");
            }
            em.remove(rmDB);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
