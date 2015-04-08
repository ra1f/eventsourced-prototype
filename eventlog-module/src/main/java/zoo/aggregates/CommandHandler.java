package zoo.aggregates;

import zoo.exceptions.ZooException;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public interface CommandHandler<C, E> {

  Iterable<E> handleCommand(C command) throws ZooException;
}
