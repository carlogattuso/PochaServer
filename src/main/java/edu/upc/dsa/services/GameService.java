package edu.upc.dsa.services;

import edu.upc.dsa.PochaManager;
import edu.upc.dsa.PochaManagerImpl;
import edu.upc.dsa.exceptions.EmptyGameListException;
import edu.upc.dsa.exceptions.UserNotFoundException;
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

@Api(value = "/game", description = "Endpoint to Game Service")
@Path("/game")
public class GameService {
    private PochaManager pm;

    public GameService() {
        this.pm = PochaManagerImpl.getInstance();
    }

    @POST
    @ApiOperation(value = "create a new Game", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response=GameJava.class),
            @ApiResponse(code = 500, message = "Impossible to create game")

    })
    @Path("/new")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response newGame(GameJava gameJava) {
        try {
            pm.newGame(gameJava.getGameType(), gameJava.getDate(), gameJava.getWinner(), gameJava.getPlayers());
        } catch (Exception e){
            return Response.status(500).build();
        }
        return Response.status(201).entity(gameJava).build();
    }

    @GET
    @ApiOperation(value = "get all games of any user", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= GameTO.class),
            @ApiResponse(code = 204, message = "Empty game list"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 500, message = "Impossible to create game")
    })
    @Path("/games/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGamesByUser(@PathParam("username") String username) {
        List<GameTO> gamesToSend = new LinkedList<>();
        try {
            gamesToSend = pm.getGamesByUser(username);
        } catch (EmptyGameListException e){
            return Response.status(204).build();
        } catch (UserNotFoundException e){
            return Response.status(404).build();
        } catch (Exception e){
            return Response.status(500).build();
        }
        GenericEntity<List<GameTO>> entity = new GenericEntity<List<GameTO>>(gamesToSend) {
        };
        return Response.status(201).entity(entity).build();
    }

    @GET
    @ApiOperation(value = "get all games of any user by Game", notes = "asdasd")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successful", response= GameTO.class),
            @ApiResponse(code = 204, message = "Empty game list"),
            @ApiResponse(code = 404, message = "User not found"),
            @ApiResponse(code = 500, message = "Impossible to create game")
    })
    @Path("/games/{username}/{gameType}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getGamesByUserAndGame(@PathParam("username") String username, @PathParam("gameType") String gameType) {
        List<GameTO> gamesToSend = new LinkedList<>();
        try {
            gamesToSend = pm.getGamesByUserAndGame(username,gameType);
        } catch (EmptyGameListException e){
            return Response.status(204).build();
        } catch (UserNotFoundException e){
            return Response.status(404).build();
        } catch (Exception e){
            return Response.status(500).build();
        }
        GenericEntity<List<GameTO>> entity = new GenericEntity<List<GameTO>>(gamesToSend) {
        };
        return Response.status(201).entity(entity).build();
    }
}
