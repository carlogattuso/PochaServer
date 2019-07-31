package edu.upc.dsa.services;

import edu.upc.dsa.PochaManager;
import edu.upc.dsa.PochaManagerImpl;
import edu.upc.dsa.exceptions.*;
import edu.upc.dsa.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Api(value = "/user", description = "Endpoint to User Service")
@Path("/user")
public class UserService {
    private PochaManager pm;

    public UserService() {
        this.pm = PochaManagerImpl.getInstance();
    }

    @POST
    @ApiOperation(value = "register a new User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=User.class),
            @ApiResponse(code = 409, message = "User Already Exists"),
            @ApiResponse(code = 500, message = "Impossible to register")

    })
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response register(UserTO user) {
        try {
            pm.newUser(user.getUsername(), user.getPassword(), user.getName(), user.getSurname(), user.getMail(), user.getAge());
        } catch (UserAlreadyExistsException e){
            return Response.status(409).build();
        } catch (Exception e){
            return Response.status(500).build();
        }
        return Response.status(201).entity(user).build();
    }

    @POST
    @ApiOperation(value = "login any User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = UserTO.class),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 401, message = "Password Not Match"),
            @ApiResponse(code = 500, message = "Impossible to login")
    })
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserLogin u) {
        UserTO user = null;
        try {
            UserJava userJava = pm.getUser(u.getUsername(), u.getPassword());
            user = new UserTO(userJava.getUsername(),userJava.getPassword(),userJava.getName(),userJava.getSurname(),userJava.getMail(),userJava.getAge());
        } catch (UserNotFoundException e) {
            return Response.status(404).build();
        } catch (PasswordNotMatchException e) {
            return Response.status(401).build();
        } catch (Exception e) {
            return Response.status(500).build();
        }
        return Response.status(201).entity(user).build();
    }

    @GET
    @ApiOperation(value = "get users by piece of username", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= UsernameTO.class),
            @ApiResponse(code = 204, message = "Empty user list"),
            @ApiResponse(code = 500, message = "Impossible to search users")
    })
    @Path("/search/{piece}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response searchUser(@PathParam("piece") String piece) {
        List<UsernameTO> usersToSend = new LinkedList<>();
        try {
            usersToSend = pm.searchUser(piece);
        } catch (EmptyUserListException e) {
            return Response.status(204).build();
        } catch (Exception e){
            return Response.status(500).build();
        }
        GenericEntity<List<UsernameTO>> entity = new GenericEntity<List<UsernameTO>>(usersToSend) {
        };
        return Response.status(201).entity(entity).build();
    }
}
