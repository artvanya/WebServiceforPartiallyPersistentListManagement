import java.util.ArrayList;
import java.util.List;

public class PartiallyPersistentList<E> {

  private final List<VersionedElement<E>> elements;

  public PartiallyPersistentList(List<E> initialList) {
    this.elements = new ArrayList<>();
    for (E element : initialList) {
      this.elements.add(new VersionedElement<>(element, 0));
    }
  }

  public void add(E element) {
    VersionedElement<E> versionedElement = new VersionedElement<>(element, elements.size());
    elements.add(versionedElement);
  }

  public void set(int index, E element) {
    VersionedElement<E> oldVersionedElement = elements.get(index);
    VersionedElement<E> newVersionedElement = new VersionedElement<>(element, oldVersionedElement.getVersion() + 1);
    elements.set(index, newVersionedElement);
  }

  public void remove(int index) {
    VersionedElement<E> oldVersionedElement = elements.get(index);
    VersionedElement<E> newVersionedElement = new VersionedElement<>(null, oldVersionedElement.getVersion() + 1);
    elements.set(index, newVersionedElement);
  }

  public E get(int index, int version) {
    VersionedElement<E> versionedElement = elements.get(index);
    while (versionedElement.getVersion() > version) {
      index = versionedElement.getPreviousIndex();
      versionedElement = elements.get(index);
    }
    return versionedElement.getElement();
  }

  public int indexOf(E element) {
    for (int i = elements.size() - 1; i >= 0; i--) {
      VersionedElement<E> versionedElement = elements.get(i);
      if (versionedElement.getElement() == element) {
        return i;
      }
    }
    return -1;
  }
}

