package facades;

import com.google.gson.Gson;
import dto.MovieDTO;
import java.io.IOException;
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
    
    public MovieDTO getMovieById(String id) throws IOException {
        String movieAPI = HttpUtils.fetchData("http://www.omdbapi.com/?i=" + id + "&apikey=6b10a5de");
        MovieDTO fetchedmovie = gson.fromJson(movieAPI, MovieDTO.class);
        return fetchedmovie;
    }
    
}
