package facades;

import dto.MovieListDTO;
import dto.SpecificMovieDTO;
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
    private final FetchFacade fetchFacade = FetchFacade.getFetchFacade();
    private final RatingFacade ratingFacade = RatingFacade.getRatingFacade(emf);

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

    public SpecificMovieDTO getMovieById(String id) throws IOException, MovieNotFoundException {
        SpecificMovieDTO mdto = fetchFacade.getMovieById(id);
        mdto.setAvgRating(ratingFacade.getRatingAvg(mdto.getImdbID()));
        return mdto;
    }

    public MovieListDTO getMoviesByTitle(String searchString, int page) throws IOException, MovieNotFoundException {
        MovieListDTO mdtoList = fetchFacade.getMoviesByTitle(searchString, page);
        mdtoList.getMovieDTOs().forEach((movie) -> {
            movie.setAvgRating(ratingFacade.getRatingAvg(movie.getImdbID()));
        });
        return mdtoList;
    }
    
    public double addRating(String movieID, int rating){
        return ratingFacade.addRating(movieID, rating);
    }
}
