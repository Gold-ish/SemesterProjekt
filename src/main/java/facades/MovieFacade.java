package facades;

import com.google.gson.Gson;
import dto.MovieDTO;
import dto.MovieListDTO;
import errorhandling.MovieNotFoundException;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author carol
 */
public class MovieFacade {

    private static MovieFacade instance;
    private static EntityManagerFactory emf;
    private final Gson GSON = new Gson();
    private final FetchFacade fetchFacade = FetchFacade.getFetchFacade();
    private final RatingFacade ratingFacade = RatingFacade.getRatingFacade(emf);
    //Which url?????
    private final String URL = "";

    //Private Constructor to ensure Singleton
    private MovieFacade() {
    }

    public static MovieFacade getMovieFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new MovieFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public MovieDTO getMovieById(String id) throws IOException, MovieNotFoundException {
        MovieDTO mdto = fetchFacade.getMovieById(id);
        mdto.setAvgRating(ratingFacade.getRatingAvg(mdto.getImdbID()));
        return mdto;
    }

    public MovieListDTO getMoviesByTitle(String searchString, int page) throws IOException, MovieNotFoundException {
        MovieListDTO mdtoList = fetchFacade.getMoviesByTitle(searchString, page);
        for (MovieDTO movie : mdtoList.getMovieDTOs()) {
            movie.setAvgRating(ratingFacade.getRatingAvg(movie.getImdbID()));
        }
        return mdtoList;
    }
}
