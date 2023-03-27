import com.google.gson.Gson;
import com.sun.jndi.url.rmi.rmiURLContext.Parser;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;

public class PartiallyPersistentListAPI {

  private static final Gson gson = new Gson();
  private static final List<PartiallyPersistentList<Integer>> versions = new ArrayList<>();

  public static void main(String[] args) {
    Spark.port(8080);

    // Initialize the initial version of the list
    List<Integer> initialList = new ArrayList<>();
    PartiallyPersistentList<Integer> initialVersion = new PartiallyPersistentList<>(initialList);
    versions.add(initialVersion);

    // Handle POST requests to add a new element to the end of the list
    Spark.post("/list", (req, res) -> {
      int element = Integer.parseInt(req.body());
      PartiallyPersistentList<Integer> latestVersion = versions.get(versions.size() - 1);
      PartiallyPersistentList<Integer> newVersion = new PartiallyPersistentList<>(new ArrayList<>(latestVersion.getElements()));
      newVersion.add(element);
      versions.add(newVersion);
      return newVersion.getVersions().size();
    });

    // Handle PUT requests to update an element's value
    Spark.put("/list/:index", (req, res) -> {
      int index = Integer.parseInt(req.params(":index"));
      int element = Integer.parseInt(req.body());
      PartiallyPersistentList<Integer> latestVersion = versions.get(versions.size() - 1);
      PartiallyPersistentList<Integer> newVersion = new PartiallyPersistentList<>(new ArrayList<>(latestVersion.getElements()));
      newVersion.set(index, element);
      versions.add(newVersion);
      return newVersion.getVersions().size();
    });

    // Handle DELETE requests to remove an element with a given value
    Spark.delete("/list/:value", (req, res) -> {
      int value = Integer.parseInt(req.params(":value"));
      PartiallyPersistentList<Integer> latestVersion = versions.get(versions.size() - 1);
      int index = latestVersion.indexOf(value);
      if (index == -1) {
        throw new IllegalArgumentException("Value not found");
      }
      PartiallyPersistentList<Integer> newVersion = new PartiallyPersistentList<>(new ArrayList<>(latestVersion.getElements()));
      newVersion.remove(index);
      versions.add(newVersion);
      return newVersion.getVersions().size();
    });

    // Handle GET requests to return a list of available versions
    Spark.get("/list/versions", (req, res) -> {
      List<Integer> versionNumbers = new ArrayList<>();
      for (int i = 0; i < versions.size(); i++) {
        versionNumbers.add(i);
      }
      return gson.toJson(versionNumbers);
    });

    // Handle GET requests to return all elements of the specific version of the list
    Spark.get("/list/:version", (req, res) -> {
      int version = Integer.parseInt(req.params(":version"));
      PartiallyPersistentList<Integer> versionedInstance = versions.get(version);
      List<Integer> elements = versionedInstance.getElements();
      return gson.toJson(elements);
    });
  }
}
