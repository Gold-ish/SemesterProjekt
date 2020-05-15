package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.UserDTO;
import entities.CriticCode;
import entities.User;
import errorhandling.GenericExceptionMapper;
import errorhandling.NotFoundException;
import errorhandling.UserException;
import errorhandling.UserExceptionMapper;
import facades.AdminFacade;
import facades.UserFacade;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import utils.EMF_Creator;

/**
 * @author allan
 */
@Path("info")
public class RoleResource {

    private static EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private final UserFacade FACADE_USER = UserFacade.getUserFacade(EMF);
    private final AdminFacade FACADE_ADMIN = AdminFacade.getAdminFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final UserExceptionMapper USER_EXCEPTION_MAPPER
            = new UserExceptionMapper();
    private static final GenericExceptionMapper GENERIC_EXCEPTION_MAPPER
            = new GenericExceptionMapper();

    @Context
    private UriInfo context;

    @Context
    SecurityContext securityContext;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getInfoForAll() {
        return "{\"msg\":\"Hello anonymous\"}";
    }

    //Just to verify if the database is setup
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("all")
    public String allUsers() {

        EntityManager em = EMF.createEntityManager();
        try {
            List<User> users = em.createQuery("select user from User user").getResultList();
            return "[" + users.size() + "]";
        } finally {
            em.close();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("user")
    @RolesAllowed({"user", "critic"})
    public Response getFromUser() {
        try {
            String username = securityContext.getUserPrincipal().getName();
            UserDTO user = FACADE_USER.getUser(username);
            return Response.ok(GSON.toJson(user)).build();
        } catch (UserException ex) {
            return USER_EXCEPTION_MAPPER.toResponse((UserException) ex);
        }
    }

    @PUT
    @Path("user/edit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "critic"})
    public Response editUser(String json) {
        try {
            String username = securityContext.getUserPrincipal().getName();
            UserDTO userDTO = GSON.fromJson(json, UserDTO.class);
            String returnedUser = GSON.toJson(FACADE_USER.editUser(username, userDTO));
            return Response.ok(returnedUser).build();
        } catch (UserException ex) {
            return USER_EXCEPTION_MAPPER.toResponse(ex);
        }
    }

    @DELETE
    @Path("user/delete")
    @RolesAllowed({"user", "critic"})
    public Response deleteUser() {
        try {
            String username = securityContext.getUserPrincipal().getName();
            String deletedUser = GSON.toJson(FACADE_USER.deleteUser(username));
            return Response.ok(deletedUser).build();
        } catch (NotFoundException ex) {
            return GENERIC_EXCEPTION_MAPPER.toResponse(ex);
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("admin")
    @RolesAllowed("admin")
    public String getFromAdmin() {
        String thisuser = securityContext.getUserPrincipal().getName();
        return "{\"msg\": \"Hello admin: " + thisuser + "\"}";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("critic/code")
    @RolesAllowed("admin")
    public Response getCriticCode() {
        CriticCode generatedCriticCode = FACADE_ADMIN.generateUniqueCriticCode(0);
        return Response.ok(generatedCriticCode).build();
    }
}
