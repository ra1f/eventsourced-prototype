package zoo.aggregates;

import zoo.exceptions.ZooException;

import java.util.Collection;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public interface CommandHandler<C, E> {

  Collection<E> handleCommand(C command) throws ZooException;
}
