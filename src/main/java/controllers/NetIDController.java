package controllers;

import javax.ws.rs.*;

@Path("/netid")
public class NetIDController {

    public NetIDController() {
    }

    @GET
    public String getNetID() {
        return "np423";
    }
}
