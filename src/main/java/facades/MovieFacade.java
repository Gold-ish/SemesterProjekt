package facades;

import com.google.gson.Gson;
import dto.MovieDTO;
import java.io.IOException;
import utils.HttpUtils;

/**
 *
 * @author carol
 */
public class MovieFacade {
    
    private static MovieFacade instance;
    private final Gson GSON = new Gson();
    //Which url?????
    private final String URL = "";

    //Private Constructor to ensure Singleton
    private MovieFacade() {
    }

    public static MovieFacade getFacade() {
        if (instance == null) {
            instance = new MovieFacade();
        }
        return instance;
    }
    
    public MovieDTO getMovieById(String id) throws IOException {
        String json = HttpUtils.fetchData(URL);
        MovieDTO movie = GSON.fromJson(json, MovieDTO.class);
        return movie;
    }
    
    public MovieDTO getMovieByTitle(String title) throws IOException {
        String json = HttpUtils.fetchData(URL);
        MovieDTO movie = GSON.fromJson(json, MovieDTO.class);
        return movie;
    }
}
