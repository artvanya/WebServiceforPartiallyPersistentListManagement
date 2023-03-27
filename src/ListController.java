import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ListController {

  @Autowired
  private ListService listService;

  @RequestMapping(value = "/list", method = RequestMethod.POST)
  public ResponseEntity<Integer> addElement(@RequestBody int value) {
    int version = listService.addElement(value);
    return new ResponseEntity<>(version, HttpStatus.OK);
  }

  @RequestMapping(value = "/list/{version}/{index}", method = RequestMethod.PUT)
  public ResponseEntity<String> updateElement(@PathVariable("version") int version,
      @PathVariable("index") int index, @RequestBody int value) {
    try {
      listService.updateElement(version, index, value);
      return new ResponseEntity<>("Element updated successfully", HttpStatus.OK);
    } catch (IndexOutOfBoundsException e) {
      return new ResponseEntity<>("Index out of range", HttpStatus.BAD_REQUEST);
    }
  }

  @RequestMapping(value = "/list/{version}/{value}", method = RequestMethod.DELETE)
  public ResponseEntity<String> removeElement(@PathVariable("version") int version,
      @PathVariable("value") int value) {
    boolean result = listService.removeElement(version, value);
    if (result) {
      return new ResponseEntity<>("Element removed successfully", HttpStatus.OK);
    } else {
      return new ResponseEntity<>("Element not found", HttpStatus.NOT_FOUND);
    }
  }

  @RequestMapping(value = "/list/versions", method = RequestMethod.GET)
  public ResponseEntity<List<Integer>> getVersions() {
    List<Integer> versions = listService.getVersions();
    return new ResponseEntity<>(versions, HttpStatus.OK);
  }

  @RequestMapping(value = "/list/{version}", method = RequestMethod.GET)
  public ResponseEntity<List<Integer>> getList(@PathVariable("version") int version) {
    List<Integer> list = listService.getList(version);
    if (list != null) {
      return new ResponseEntity<>(list, HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

