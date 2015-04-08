package zoo.aggregates;

import zoo.commands.Buy;
import zoo.commands.Digest;
import zoo.events.Bought;
import zoo.events.Digested;
import zoo.events.Event;
import zoo.exceptions.AnimalAlreadyBoughtException;
import zoo.exceptions.AnimalNotExistingException;
import zoo.exceptions.ZooException;
import zoo.states.FeelingOfSatiety;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Animal extends Aggregate {

  private FeelingOfSatiety feelingOfSatiety;

  public Animal(String id, Date timestamp, FeelingOfSatiety feelingOfSatiety) {
    super(id, timestamp);
    this.feelingOfSatiety = feelingOfSatiety;
  }

  public Animal(String id, Date timestamp) {
    super(id, timestamp);
  }

  public Animal() {
  }

  public CommandHandler<Buy, Event> asBuyCommandHandler() {
    return new CommandHandler<Buy, Event>() {
      @Override
      public Iterable<Event> handleCommand(Buy command) throws ZooException {
        if (id != null) {
          throw new AnimalAlreadyBoughtException(command.getAnimalId());
        }
        return Arrays.asList(new Bought(command.getAnimalId(), command.getTimestamp()));
      }
    };
  }

  public CommandHandler<Digest, Event> asDigestCommandhandler() {
    return new CommandHandler<Digest, Event>() {
      @Override
      public Iterable<Event> handleCommand(Digest command) throws ZooException {
        if (id == null) {
          throw new AnimalNotExistingException(command.getAnimalId());
        }
        return Arrays.asList(new Digested(command.getAnimalId(), command.getTimestamp()));
      }
    };
  }

  public EventApplier<Bought> asBoughtEventApplier() {
    return new EventApplier<Bought>() {
      @Override
      public Animal applyEvent(Bought event) {
        return new Animal(event.getAnimalId(), event.getTimestamp());
      }
    };
  }

  public EventApplier<Digested> asDigestedEventApplier() {
    return new EventApplier<Digested>() {
      @Override
      public Animal applyEvent(Digested event) {
        return new Animal(event.getAnimalId(), event.getTimestamp(), feelingOfSatiety.worse());
      }
    };
  }

}
