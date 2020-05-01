package facades;

import dto.MovieListDTO;
import dto.SpecificMovieDTO;
import errorhandling.MovieNotFoundException;
import errorhandling.NotFoundException;
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
    private final ReviewFacade reviewFacade = ReviewFacade.getReviewFacade(emf);

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
        mdto.setReviews(reviewFacade.getReviews(mdto.getImdbID()));
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
    
    public String addReview(String movieID, String review) {
        return reviewFacade.addReview(movieID, review);
    }
    
    public String editReview(int id, String movieID, String review) throws NotFoundException{
        return reviewFacade.editReview(id, movieID, review);
    }
    
    public String deleteReview(int id) throws NotFoundException{
        return reviewFacade.deleteReview(id);
    }
    
    
}
