public class VersionedElement<E> {

  private final E element;
  private final int version;
  private final int previousIndex;

  public VersionedElement(E element, int version) {
    this.element = element;
    this.version = version;
    this.previousIndex = version > 0 ? version - 1 : -1;
  }

  public E getElement() {
    return element;
  }

  public int getVersion() {
    return version;
  }

  public int getPreviousIndex() {
    return previousIndex;
  }
}

