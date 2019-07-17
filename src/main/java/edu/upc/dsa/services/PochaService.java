package edu.upc.dsa.services;

import edu.upc.dsa.PochaManager;
import edu.upc.dsa.PochaManagerImpl;
import edu.upc.dsa.TracksManager;
import edu.upc.dsa.TracksManagerImpl;
import edu.upc.dsa.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/pocha", description = "Endpoint to Track Service")
@Path("/pocha")
public class PochaService {
    private PochaManager pm;

    public PochaService() {
        this.pm = PochaManagerImpl.getInstance();
        if (pm.size()==0) {
            try {
                pm.getUsuarios();
            }catch (Exception e)
            {}
        }
    }

    @GET
    @ApiOperation(value = "get one User", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response = Usuario.class),
            @ApiResponse(code = 404, message = "User not found")

    })
    @Path("/getUsuario/{Username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsuario(@PathParam("Username") String username) {

        try {
            Usuario usuario = this.pm.getUsuario(username);
            GenericEntity<Usuario> entity = new GenericEntity<Usuario>(usuario) {
            };
            return Response.status(201).entity(entity).build();
        } catch (Exception e) {
            return Response.status(404).build();
        }
    }

    @GET
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
    }
}
