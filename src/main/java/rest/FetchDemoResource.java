package rest;

import com.google.gson.Gson;
import facades.FetchFacade;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;

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
    
}
