package facades;

import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

/**
 *
 * @author rando
 */
public class RatingFacadeTest {

    private static EntityManagerFactory EMF
            = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final RatingFacade FACADE = RatingFacade.getRatingFacade(EMF);

    //@Disabled("Not implemented yet")
    //@Test
    public void testAddRating_ReturnsTheRating_EqualResults() throws Exception {
        System.out.println("testAddRating_ReturnsTheRating_EqualResults");
    }

    //@Disabled("Not implemented yet")
    //@Test
    public void testAddRating_CantThinkOfNegativeTest_ThrowSomeException() throws Exception {
        System.out.println("test-negative");
    }

    //@Disabled("Not implemented yet")
    //@Test
    public void testGetRatingAvg_ReturnsTheAvgRating_EqualResults() throws Exception {
        System.out.println("testGetRatingAvg_ReturnsTheAvgRating_EqualResults");
    }

    //@Disabled("Not implemented yet")
    //@Test
    public void testGetRatingAvg_ReturnsTheAvgRatingOfNonExistingRating_EqualResults() throws Exception {
        System.out.println("testGetRatingAvg_ReturnsTheAvgRatingOfNonExistingRating_EqualResults");
    }
}
