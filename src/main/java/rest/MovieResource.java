package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import errorhandling.GenericExceptionMapper;
import errorhandling.MovieNotFoundException;
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
    private static final GenericExceptionMapper EXCEPTION_MAPPER
            = new GenericExceptionMapper();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) throws MovieNotFoundException {
        try {
            String movie = GSON.toJson(FACADE.getMovieById(id));
            return Response.ok(movie).build();
        } catch (IOException ex) {
            return EXCEPTION_MAPPER.toResponse(ex);
        }
    }

//    @GET
//    @Path("search/{title}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getByTitle(@PathParam("title") String title) {
//        try {
//            String movie = GSON.toJson(FACADE.getMovieByTitle(title));
//            return Response.ok(movie).build();
//        } catch (IOException ex) {
//             return EXCEPTION_MAPPER.toResponse(ex);
//        }
//    }
}
