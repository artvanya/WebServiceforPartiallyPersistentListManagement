import java.util.ArrayList;
import java.util.List;


@Path("/list")
public class ListResource {

  private static PersistentList<Integer> list = new PersistentLinkedList<>();

  // POST /list/{value}
  @POST
  @Path("/{value}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response add(@PathParam("value") int value) {
    int version = list.add(value);
    return Response.ok(version).build();
  }

  // PUT /list/{id}/{value}
  @PUT
  @Path("/{id}/{value}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response update(@PathParam("id") int id, @PathParam("value") int value) {
    try {
      list = list.set(id, value);
      return Response.ok(id).build();
    } catch (IndexOutOfBoundsException e) {
      return Response.status(Response.Status.NOT_FOUND).entity("Index not found").build();
    }
  }

  // DELETE /list/{id}/{value}
  @DELETE
  @Path("/{id}/{value}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response delete(@PathParam("id") int id, @PathParam("value") int value) {
    try {
      list = list.remove(id, value);
      return Response.ok(id).build();
    } catch (IllegalArgumentException e) {
      return Response.status(Response.Status.NOT_FOUND).entity("Value not found").build();
    }
  }

  // GET /list
  @GET
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllVersions() {
    List<Integer> versionList = new ArrayList<>(list.getAllVersions());
    return Response.ok(versionList).build();
  }

  // GET /list/{id}
  @GET
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getVersion(@PathParam("id") int id) {
    try {
      List<Integer> version = list.getVersion(id);
      return Response.ok(version).build();
    } catch (IndexOutOfBoundsException e) {
      return Response.status(Response.Status.NOT_FOUND).entity("Version not found").build();
    }
  }
}
