package facades;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.MovieDTO;
import dto.MovieListDTO;
import dto.SpecificMovieDTO;
import errorhandling.MovieNotFoundException;
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
        String movieAPI = HttpUtils.fetchData("http://www.omdbapi.com/?i=" + id + "&plot=full&apikey=6b10a5de");
        if (movieAPI.contains("Error")) {
            throw new MovieNotFoundException("No movie found with id: " + id);
        }
        SpecificMovieDTO fetchedmovie = GSON.fromJson(movieAPI, SpecificMovieDTO.class);
        return fetchedmovie;
    }

    public MovieListDTO getMoviesByTitle(String searchString, int page) throws IOException, MovieNotFoundException {
        String searchAPIdata = HttpUtils.fetchData("http://www.omdbapi.com/?s='"
                + searchString + "'&page=" + page + "&apikey=6b10a5de");
        if (searchAPIdata.contains("Movie not found!")) {
            throw new MovieNotFoundException("No movie found with the search result: " + searchString);
        } else if (searchAPIdata.contains("Too many results.")) {
            throw new IllegalArgumentException("Too many search results, not specific enough search: " + searchString);
        } else {
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
                } else {
                    movieStrings.add(moviesExtra[0].substring(lastMovieIndexInString, moviesExtra[0].length()));
                }
            }
            List<MovieDTO> movieDtos = new ArrayList();
            for (String movie : movieStrings) {
                movieDtos.add(GSON.fromJson(movie, MovieDTO.class));
            }
            
            String searchResultsJSONString = "{" + moviesExtra[1].substring(1);
            JsonObject jobj = new Gson().fromJson(searchResultsJSONString, JsonObject.class);
            int searchResults = jobj.get("totalResults").getAsInt();
            return new MovieListDTO(movieDtos , searchResults);
        }
    }
}
