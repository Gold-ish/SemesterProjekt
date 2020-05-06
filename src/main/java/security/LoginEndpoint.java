package security;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import dto.UserDTO;
import entities.User;
import errorhandling.AuthenticationException;
import errorhandling.GenericExceptionMapper;
import errorhandling.UserException;
import facades.UserFacade;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import utils.EMF_Creator;

@Path("login")
public class LoginEndpoint {

    public static final int TOKEN_EXPIRE_TIME = 1000 * 60 * 30; //30 min
    private static final EntityManagerFactory EMF
            = EMF_Creator.createEntityManagerFactory(EMF_Creator.DbSelector.DEV, EMF_Creator.Strategy.CREATE);
    private static final UserFacade USER_FACADE = UserFacade.getUserFacade(EMF);//This was public.. This was causing all my problems..
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final GenericExceptionMapper GENERIC_EXCEPTION_MAPPER
            = new GenericExceptionMapper();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String jsonString) {
        try {
            JsonObject json = new JsonParser().parse(jsonString).getAsJsonObject();
            String username = json.get("username").getAsString();
            String password = json.get("password").getAsString();
            return verifyAndGrantToken(username, password, "LoggedIn");
        } catch (AuthenticationException e) {
            return GENERIC_EXCEPTION_MAPPER.toResponse(e);
        }
    }

    @POST
    @Path("register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registrUser(String jsonString) {
        try {
            UserDTO userDTO = GSON.fromJson(jsonString, UserDTO.class);
            String result = USER_FACADE.registerUser(userDTO);
            return verifyAndGrantToken(userDTO.getUsername(), userDTO.getPassword(), result);
        } catch (UserException | AuthenticationException e) {
            return GENERIC_EXCEPTION_MAPPER.toResponse(e);
        }
    }

    private Response verifyAndGrantToken(String username, String password, String result) throws AuthenticationException {
        try {
            User user = USER_FACADE.getVeryfiedUser(username, password);
            String token = createToken(username, user.getRolesAsStrings());
            JsonObject responseJson = new JsonObject();
            responseJson.addProperty("username", username);
            responseJson.addProperty("role", user.getRolesAsString());
            responseJson.addProperty("token", token);
            responseJson.addProperty("creation", result);
            return Response.ok(new Gson().toJson(responseJson)).build();
        } catch (JOSEException | AuthenticationException ex) {
            if (ex instanceof AuthenticationException) {
                throw (AuthenticationException) ex;
            }
            Logger.getLogger(GenericExceptionMapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new AuthenticationException("Invalid username or password! Please try again");
    }

    private String createToken(String userName, List<String> roles) throws JOSEException {

        StringBuilder res = new StringBuilder();
        for (String string : roles) {
            res.append(string);
            res.append(",");
        }
        String rolesAsString = res.length() > 0 ? res.substring(0, res.length() - 1) : "";
        String issuer = "semesterstartcode-dat3";

        JWSSigner signer = new MACSigner(SharedSecret.getSharedKey());
        Date date = new Date();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userName)
                .claim("username", userName)
                .claim("roles", rolesAsString)
                .claim("issuer", issuer)
                .issueTime(date)
                .expirationTime(new Date(date.getTime() + TOKEN_EXPIRE_TIME))
                .build();
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);
        return signedJWT.serialize();

    }
}
