package facades;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.MovieDTO;
import dto.MovieListDTO;
import dto.SpecificMovieDTO;
import errorhandling.MovieNotFoundException;
import fetch.MovieFetchCall;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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

    public SpecificMovieDTO getMovieByIdSpecific(String id) throws IOException, MovieNotFoundException {
        String fetchedJSONString = HttpUtils.fetchData("http://www.omdbapi.com/?i=" + id + "&plot=full&apikey=492c3335");
        if (fetchedJSONString.contains("Error")) {
            throw new MovieNotFoundException("No movie found with id: " + id);
        }
        SpecificMovieDTO fetchedmovie = GSON.fromJson(fetchedJSONString, SpecificMovieDTO.class);
        return fetchedmovie;
    }
    
    public MovieDTO getMovieByIdSimple(String id) throws IOException, MovieNotFoundException {
        String fetchedJSONString = HttpUtils.fetchData("http://www.omdbapi.com/?i=" + id + "&plot=short&apikey=492c3335");
        if (fetchedJSONString.contains("Error")) {
            throw new MovieNotFoundException("No movie found with id: " + id);
        }
        MovieDTO fetchedmovie = GSON.fromJson(fetchedJSONString, MovieDTO.class);
        return fetchedmovie;
    }

    public MovieListDTO getMoviesByTitle(String searchString, int page) throws IOException, MovieNotFoundException, InterruptedException {
        String fetchedJSONString = HttpUtils.fetchData("http://www.omdbapi.com/?s='"
                + searchString + "'&page=" + page + "&apikey=492c3335");
        if (fetchedJSONString.contains("Movie not found!")) {
            throw new MovieNotFoundException("No movie found with the search result: " + searchString);
        } else if (fetchedJSONString.contains("Too many results.")) {
            throw new IllegalArgumentException("Too many search results, not specific enough search: " + searchString);
        }
        
        String[] jsonStringSplit = fetchedJSONString.substring(11).split("\\]");
        MovieDTO[] movieDTOs = GSON.fromJson("[" + jsonStringSplit[0] + "]", MovieDTO[].class);
        JsonObject jobj = new Gson().fromJson("{" + jsonStringSplit[1].substring(1), JsonObject.class);

        return new MovieListDTO(fetchParralel(movieDTOs), jobj.get("totalResults").getAsInt());

    }
    
    
    private List<MovieDTO> fetchParralel(MovieDTO[] searchList) throws InterruptedException{
        List<MovieFetchCall> fetchCalls = new ArrayList<>();
        for(MovieDTO mdto : searchList){
            fetchCalls.add(new MovieFetchCall(mdto.getImdbID()));
        }
        ExecutorService workingJack = Executors.newFixedThreadPool(10);
        for (MovieFetchCall fc : fetchCalls) {
            Runnable task = () -> {
                try {
                    fc.doWork();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            workingJack.submit(task);
        }
        workingJack.shutdown();
        workingJack.awaitTermination(15, TimeUnit.SECONDS);
        List<MovieDTO> returnList = new ArrayList<>();
        for(MovieFetchCall fc : fetchCalls){
            returnList.add(fc.getMdto());
        }
        return returnList;
        }
}
