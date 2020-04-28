package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import errorhandling.GenericExceptionMapper;
import errorhandling.MovieNotFoundException;
import errorhandling.MovieNotFoundExceptionMapper;
import facades.FetchFacade;
import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author carol
 */
@Path("movies")
public class MovieResource {

    private static final FetchFacade FACADE = FetchFacade.getFetchFacade();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final GenericExceptionMapper GENERIC_EXCEPTION_MAPPER
            = new GenericExceptionMapper();
    private static final MovieNotFoundExceptionMapper MOVIE_EXCEPTION_MAPPER 
            = new MovieNotFoundExceptionMapper();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        try {
            String movie = GSON.toJson(FACADE.getMovieById(id));
            return Response.ok(movie).build();
        } catch (IOException ex) {
            return GENERIC_EXCEPTION_MAPPER.toResponse(ex);
        } catch (MovieNotFoundException ex) {
            return MOVIE_EXCEPTION_MAPPER.toResponse(ex);
        }
    }

    @GET
    @Path("search/{title}/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByTitle(@PathParam("title") String title,
            @PathParam("page") int page) {
        try {
            String movie = GSON.toJson(FACADE.getMoviesByTitle(title, page));
            return Response.ok(movie).build();
        } catch (IOException ex) {
             return GENERIC_EXCEPTION_MAPPER.toResponse(ex);
        } catch (MovieNotFoundException ex) {
            return MOVIE_EXCEPTION_MAPPER.toResponse(ex);
        }
    }
}
