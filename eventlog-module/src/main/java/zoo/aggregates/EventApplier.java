package zoo.aggregates;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public interface EventApplier<E> {

  <T extends Aggregate> T applyEvent(E event);
}
