package zoo.aggregates;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public interface CommandHandler<C, E> {

  Iterable<E> handleCommand(C command);
}
