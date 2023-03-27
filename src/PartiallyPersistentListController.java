import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/list")
public class PartiallyPersistentListController {

  private final Map<Integer, PartiallyPersistentList<Integer>> versions = new HashMap<>();
  private int currentVersion = 0;

  @PostMapping
  public int addToList(@RequestBody int value) {
    PartiallyPersistentList<Integer> list = versions.get(currentVersion);
    if (list == null) {
      list = new PartiallyPersistentList<>(new ArrayList<>());
      versions.put(currentVersion, list);
    }
    list.add(value);
    currentVersion++;
    return currentVersion;
  }

  @PutMapping("/{version}/{index}")
  public int updateListElement(@PathVariable int version, @PathVariable int index, @RequestBody int value) {
    PartiallyPersistentList<Integer> list = versions.get(version);
    if (list == null) {
      throw new IllegalArgumentException("Invalid version");
    }
    list.set(index, value);
    currentVersion++;
    return currentVersion;
  }

  @DeleteMapping("/{version}")
  public int removeFromList(@PathVariable int version, @RequestBody int value) {
    PartiallyPersistentList<Integer> list = versions.get(version);
    if (list == null) {
      throw new IllegalArgumentException("Invalid version");
    }
    int index = list.indexOf(value);
    if (index == -1) {
      throw new IllegalArgumentException("Value not found in list");
    }
    list.remove(index);
    currentVersion++;
    return currentVersion;
  }

  @GetMapping("/versions")
  public List<Integer> getListVersions() {
    return new ArrayList<>(versions.keySet());
  }

  @GetMapping("/{version}")
  public List<Integer> getListElements(@PathVariable int version) {
    PartiallyPersistentList<Integer> list = versions.get(version);
    if (list == null) {
      throw new IllegalArgumentException("Invalid version");
    }
    return list;
  }

  public static void main(String[] args) {
    SpringApplication.run(PartiallyPersistentListController.class, args);
  }
}

