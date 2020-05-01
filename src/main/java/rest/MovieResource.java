package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import errorhandling.GenericExceptionMapper;
import errorhandling.MovieNotFoundException;
import errorhandling.MovieNotFoundExceptionMapper;
import errorhandling.NotFoundException;
import facades.MovieFacade;
import java.io.IOException;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import utils.EMF_Creator;

/**
 *
 * @author carol
 */
@Path("movies")
public class MovieResource {

    private static EntityManagerFactory EMF = 
            EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final MovieFacade FACADE = MovieFacade.getMovieFacade(EMF);
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
    
    @POST
    @Path("add/rating/{movieid}/{rating}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRating(@PathParam("movieid") String movieID,
            @PathParam("rating") int rating) {
        double addedRating = FACADE.addRating(movieID, rating);
        return Response.ok(addedRating).build();
    }
    
    @POST
    @Path("add/review/{movieid}/{review}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addReview(@PathParam("movieid") String movieID,
            @PathParam("review") String review) {
        String addedReview = FACADE.addReview(movieID, review);
        return Response.ok(addedReview).build();
    }
    
    @PUT
    @Path("edit/review/{id}/{movieid}/{review}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editReview(@PathParam("id") int id, @PathParam("movieid") String movieID,
            @PathParam("review") String review) throws NotFoundException {
        String addedReview = FACADE.editReview(id, movieID, review);
        return Response.ok(addedReview).build();
    }
    
    @DELETE
    @Path("delete/review/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response editReview(@PathParam("id") int id) throws NotFoundException {
        String addedReview = FACADE.deleteReview(id);
        return Response.ok(addedReview).build();
    }
    
}
