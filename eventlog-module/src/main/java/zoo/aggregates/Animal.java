package zoo.aggregates;

import zoo.commands.Buy;
import zoo.commands.Digest;
import zoo.commands.Feed;
import zoo.events.*;
import zoo.exceptions.AnimalAlreadyThereException;
import zoo.exceptions.NoSuchAnimalException;
import zoo.exceptions.ZooException;
import zoo.states.FeelingOfSatiety;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Animal extends Aggregate {

  private Boolean existing = false;
  private FeelingOfSatiety feelingOfSatiety;

  public Animal() {
  }

  private Animal(String id, Date timestamp, Boolean existing, FeelingOfSatiety feelingOfSatiety) {
    super(id, timestamp);
    this.existing = existing;
    this.feelingOfSatiety = feelingOfSatiety;
  }

  public FeelingOfSatiety getFeelingOfSatiety() {
    return feelingOfSatiety;
  }

  public Boolean isExisting() {
    return existing;
  }

  public CommandHandler<Buy, Event> asBuyCommandHandler() {
    return new CommandHandler<Buy, Event>() {
      @Override
      public Collection<Event> handleCommand(Buy command) throws ZooException {
        if (existing) {
          throw new AnimalAlreadyThereException(command.getAnimalId());
        }
        return Arrays.asList(new Bought(command.getAnimalId(), command.getTimestamp()));
      }
    };
  }

  public CommandHandler<Feed, Event> asFeedCommandHandler() {
    return new CommandHandler<Feed, Event>() {

      @Override
      public Collection<Event> handleCommand(Feed command) throws ZooException {
        if (!existing) {
          throw new NoSuchAnimalException(command.getAnimalId());
        }
        return Arrays.asList(new Fed(command.getAnimalId(), command.getTimestamp()));
      }
    };
  }

  public CommandHandler<Digest, Event> asDigestCommandHandler() {
    return new CommandHandler<Digest, Event>() {
      @Override
      public Collection<Event> handleCommand(Digest command) throws ZooException {
        if (!existing) {
          throw new NoSuchAnimalException(command.getAnimalId());
        }
        Event event = new Digested(command.getAnimalId(), command.getTimestamp());
        if (feelingOfSatiety.isWorst()) {
          event = new Died(command.getAnimalId(), command.getTimestamp());
        }
        return Arrays.asList(event);
      }
    };
  }

  public EventApplier<Bought> asBoughtEventApplier() {
    return new EventApplier<Bought>() {
      @Override
      public Animal applyEvent(Bought event) {
        return new Animal(event.getAnimalId(), event.getTimestamp(), true, FeelingOfSatiety.full);
      }
    };
  }

  public EventApplier<Fed> asFedEventApplier() {
    return new EventApplier<Fed>() {
      @Override
      public Animal applyEvent(Fed event) {
        return new Animal(event.getAnimalId(), event.getTimestamp(), existing, feelingOfSatiety.better());
      }
    };
  }

  public EventApplier<Digested> asDigestedEventApplier() {
    return new EventApplier<Digested>() {
      @Override
      public Animal applyEvent(Digested event) {
        return new Animal(event.getAnimalId(), event.getTimestamp(), existing, feelingOfSatiety.worse());
      }
    };
  }

  public EventApplier<Died> asDiedEventApplier() {
    return new EventApplier<Died>() {
      @Override
      public Animal applyEvent(Died event) {
        return new Animal(event.getAnimalId(), event.getTimestamp(), false, feelingOfSatiety);
      }
    };
  }

}
