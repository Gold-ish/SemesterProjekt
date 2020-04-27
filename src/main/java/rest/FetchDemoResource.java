package rest;

import com.google.gson.Gson;
import dto.CompleteDTO;
import dto.RandomCatDTO;
import dto.RandomDogDTO;
import facades.FetchFacade;
import java.io.IOException;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author rando
 */
@Path("fetch")
@RolesAllowed("admin")
public class FetchDemoResource {
    private Gson gson = new Gson();
    private FetchFacade facade = FetchFacade.getFetchFacade();
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJokeMovieKanye() throws IOException, InterruptedException {
        CompleteDTO cDTO = facade.runParalel();
        return gson.toJson(cDTO);
    }
    
    @GET
    @Path("catpic")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public String getCatPic() throws IOException {
        RandomCatDTO randomCatDTO = facade.getCatPic();
        return gson.toJson(randomCatDTO);
    }
    
    @GET
    @Path("dogpic")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDogPic() throws IOException {
        RandomDogDTO randomDogDTO = facade.getDogPic();
        return gson.toJson(randomDogDTO);
    }
    
}
