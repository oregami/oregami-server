package org.oregami.resources;

import com.google.inject.Inject;
import io.dropwizard.auth.Auth;
import org.oregami.data.GameDao;
import org.oregami.data.GameDao;
import org.oregami.entities.Game;
import org.oregami.entities.user.User;
import org.oregami.service.GameService;
import org.oregami.service.GameService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GamesResource {

	@Inject
	private GameDao dao = null;

    @Inject
    private GameService service = null;

	public GamesResource() {
	}
	
	  
	@GET
	public List<Game> list() {
		return dao.findAll();
	}

    @GET
    @Path("/{id}")
    public Response get(@PathParam("id") String id) {
        return ResourceHelper.get(id, dao);
    }

    @GET
    @Path("/{id}/revisions")
    public Response getRevisions(@PathParam("id") String id) {
        return ResourceHelper.getRevisions(id, dao);
    }

    @GET
    @Path("/{id}/revisions/{revision}")
    public Response getRevision(@PathParam("id") String id, @PathParam("revision") String revision) {
        return ResourceHelper.getRevision(id, revision, dao);
    }

    @POST
    public Response create(@Auth User user, Game entity) {
        return ResourceHelper.create(user, entity, service);
    }

    @PUT
    @Path("{id}")
    public Response update(@Auth User user, @PathParam("id") String id, Game entity) {
        return ResourceHelper.update(user, id, entity, service);
    }

    @DELETE
    @Path("{id}")
    public Response delete(@Auth User user, @PathParam("id") String id) {
        return ResourceHelper.delete(user, id, service);
    }

}
