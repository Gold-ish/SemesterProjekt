package facades;

import entities.CriticCode;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rando
 */
public class AdminFacade {

    private static AdminFacade instance;
    private static EntityManagerFactory emf;

    private AdminFacade() {
    }

    public static AdminFacade getAdminFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AdminFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public CriticCode generateUniqueCriticCode(double random) {
        EntityManager em = getEntityManager();
        CriticCode cc = new CriticCode();
        cc.setCode(getAlphaNumericString(random));
        try {
            em.getTransaction().begin();
            em.persist(cc);
            em.getTransaction().commit();
            return cc;
        } finally {
            em.close();
        }
    }

    private String getAlphaNumericString(double random) {
        //Choose a Character random from this String
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder(50);
        for (int i = 0; i < 50; i++) {
            //Generate a random number between
            //0 to AlphaNumericString variable length
            int index = (int) (AlphaNumericString.length() * (random == 0 ? Math.random(): random));
            sb.append(AlphaNumericString.charAt(index));
        }
        return sb.toString();
    }
}
