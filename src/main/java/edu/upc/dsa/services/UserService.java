package edu.upc.dsa.services;

import edu.upc.dsa.PochaManager;
import edu.upc.dsa.PochaManagerImpl;
import edu.upc.dsa.exceptions.PasswordNotMatchException;
import edu.upc.dsa.exceptions.UserAlreadyExistsException;
import edu.upc.dsa.exceptions.UserNotFoundException;
import edu.upc.dsa.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    /*@GET
    @ApiOperation(value = "get one User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Usuario.class),
            @ApiResponse(code = 404, message = "User not found")

    })
    @Path("/getUsuarios")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuarios() {

        try {
            List<Usuario> usuarios = this.pm.getUsuarios();
            GenericEntity<List<Usuario>> entity = new GenericEntity<List<Usuario>>(usuarios) {
            };
            return Response.status(201).entity(entity).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    @POST
    @ApiOperation(value = "create a new User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=Usuario.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/nuevoUsuario")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newUsuario(Usuario usuario) {

        try {
            this.pm.añadirUsuarios(usuario.getNombre());
            return Response.status(201).entity(usuario).build();
        }catch (Exception e)
        {return Response.status(500).build();}
    }


    @POST
    @ApiOperation(value = "create a new Partida", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= PartidaTO.class),
            @ApiResponse(code = 500, message = "Validation Error")

    })
    @Path("/nuevaPartida")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newPartida(PartidaTO partidaTO) {

        try {
            this.pm.añadirPartida(partidaTO.getNombre(),partidaTO.getNumJugadores());
            for (UsuarioTO usuario : partidaTO.getJugadores())
            {
                Usuario u = this.pm.getUsuario(usuario.getNombre());
                this.pm.añadirPartidaUsuario(partidaTO.getNombre(), u.getId(),usuario.getPuntuacion());
            }
            return Response.status(201).entity(partidaTO).build();
        }catch (Exception e)
        {return Response.status(500).build();}
    }*/
}
