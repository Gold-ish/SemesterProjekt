package facades;

import dto.MovieListDTO;
import dto.RatingDTO;
import dto.ReviewDTO;
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

    public MovieListDTO getMoviesByTitle(String title, int page) throws IOException, 
            MovieNotFoundException {
        String searchString = title;
        if (title.contains("%20")) {
            searchString = searchString.replaceAll("%20", " ");
        }
        
        MovieListDTO mdtoList = fetchFacade.getMoviesByTitle(searchString, page);
        mdtoList.getMovieDTOs().forEach((movie) -> {
            movie.setAvgRating(ratingFacade.getRatingAvg(movie.getImdbID()));
        });
        return mdtoList;
    }
    
    public RatingDTO addRating(RatingDTO ratingDTO){
        return ratingFacade.addRating(ratingDTO);
    }
    
    public RatingDTO editRating(RatingDTO ratingDTO) throws NotFoundException{
        return ratingFacade.editRating(ratingDTO);
    }
    
    public String deleteRating(RatingDTO ratingDTO) throws NotFoundException{
        return ratingFacade.deleteRating(ratingDTO);
    }
    
    public ReviewDTO addReview(ReviewDTO reviewDTO) {
        return reviewFacade.addReview(reviewDTO);
    }
    
    public ReviewDTO editReview(ReviewDTO reviewDTO) throws NotFoundException{
        return reviewFacade.editReview(reviewDTO);
    }
    
    public String deleteReview(ReviewDTO review) throws NotFoundException{
        return reviewFacade.deleteReview(review);
    }
    
    
}
