package facades;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.MovieDTO;
import dto.MovieListDTO;
import dto.SpecificMovieDTO;
import errorhandling.MovieNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import utils.HttpUtils;

/**
 *
 * @author rando
 */
public class FetchFacade {

    private static FetchFacade instance;
    private static final Gson GSON = new Gson();

    //Private Constructor to ensure Singleton
    private FetchFacade() {
    }

    public static FetchFacade getFetchFacade() {
        if (instance == null) {
            instance = new FetchFacade();
        }
        return instance;
    }

    public SpecificMovieDTO getMovieById(String id) throws IOException, MovieNotFoundException {
        String fetchedJSONString = HttpUtils.fetchData("http://www.omdbapi.com/?i=" + id + "&plot=full&apikey=6b10a5de");
        if (fetchedJSONString.contains("Error")) {
            throw new MovieNotFoundException("No movie found with id: " + id);
        }
        SpecificMovieDTO fetchedmovie = GSON.fromJson(fetchedJSONString, SpecificMovieDTO.class);
        return fetchedmovie;
    }

    public MovieListDTO getMoviesByTitle(String searchString, int page) throws IOException, MovieNotFoundException {
        String fetchedJSONString = HttpUtils.fetchData("http://www.omdbapi.com/?s='"
                + searchString + "'&page=" + page + "&apikey=6b10a5de");
        if (fetchedJSONString.contains("Movie not found!")) {
            throw new MovieNotFoundException("No movie found with the search result: " + searchString);
        } else if (fetchedJSONString.contains("Too many results.")) {
            throw new IllegalArgumentException("Too many search results, not specific enough search: " + searchString);
        }
        
        String[] jsonStringSplit = fetchedJSONString.substring(11).split("\\]");
        MovieDTO[] movieDTOs = GSON.fromJson("[" + jsonStringSplit[0] + "]", MovieDTO[].class);
        JsonObject jobj = new Gson().fromJson("{" + jsonStringSplit[1].substring(1), JsonObject.class);

        return new MovieListDTO(Arrays.asList(movieDTOs), jobj.get("totalResults").getAsInt());

    }
}
