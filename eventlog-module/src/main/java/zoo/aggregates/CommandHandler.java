package zoo.aggregates;

import zoo.commands.Command;
import zoo.dto.Events;
import zoo.events.Event;
import zoo.exceptions.ZooException;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public interface CommandHandler<C extends Command, E extends Event> {

  Events<E> handleCommand(C command) throws ZooException;
}
