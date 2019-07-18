package edu.upc.dsa.services;

import edu.upc.dsa.PochaManager;
import edu.upc.dsa.PochaManagerImpl;
import edu.upc.dsa.models.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
}
