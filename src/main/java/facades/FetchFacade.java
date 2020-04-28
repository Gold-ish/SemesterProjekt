package facades;

import com.google.gson.Gson;
import dto.MovieDTO;
import dto.MovieListDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import utils.HttpUtils;

/**
 *
 * @author rando
 */
public class FetchFacade {

    private static FetchFacade instance;
    private static final Gson gson = new Gson();

    //Private Constructor to ensure Singleton
    private FetchFacade() {
    }

    public static FetchFacade getFetchFacade() {
        if (instance == null) {
            instance = new FetchFacade();
        }
        return instance;
    }
    
    public MovieDTO getMovieWithID(String id) throws IOException {
        String movieAPI = HttpUtils.fetchData("http://www.omdbapi.com/?i=" + id + "&apikey=6b10a5de");
        MovieDTO fetchedmovie = gson.fromJson(movieAPI, MovieDTO.class);
        return fetchedmovie;
    }
    
    public MovieListDTO fetchSearch(String searchString, int page) throws IOException{
        String searchAPIdata = HttpUtils.fetchData("http://www.omdbapi.com/?s="+searchString+"&page="+page+"&apikey=6b10a5de");
        
        //We remove all the formalities from the Json by substringing it into a format that we can use.
        String movies = searchAPIdata.substring(11);
        String[] moviesExtra = movies.split("\\]");
        //We extract alle the individual 10 movies from our search.
        List<String> movieStrings = new ArrayList();
        int nextMovieIndexInString = 0;
        int lastMovieIndexInString = 0;
        for (int i = 1; i <= 10; i++) {
            if (i != 10) {
                nextMovieIndexInString = moviesExtra[0].indexOf("},{", nextMovieIndexInString) + 1;
                movieStrings.add(moviesExtra[0].substring(lastMovieIndexInString, nextMovieIndexInString));
                lastMovieIndexInString = nextMovieIndexInString;
                if (lastMovieIndexInString != 0) {
                    lastMovieIndexInString += 1;
                }
            }else {
                movieStrings.add(moviesExtra[0].substring(lastMovieIndexInString, moviesExtra[0].length()));
            }
        }
        List<MovieDTO> movieDtos = new ArrayList();
        for (String movie : movieStrings) {
            movieDtos.add(gson.fromJson(movie, MovieDTO.class));
        }
        
        return new MovieListDTO(movieDtos);
    }
    
}
