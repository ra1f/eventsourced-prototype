package zoo.aggregates;

import zoo.commands.Buy;
import zoo.commands.Digest;
import zoo.commands.Feed;
import zoo.events.*;
import zoo.exceptions.AnimalAlreadyThereException;
import zoo.exceptions.NoSuchAnimalException;
import zoo.exceptions.ZooException;
import zoo.states.FeelingOfSatiety;
import zoo.states.Hygiene;
import zoo.states.Mindstate;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class AnimalAggregate extends Aggregate {

  private Boolean existing = false;
  private FeelingOfSatiety feelingOfSatiety;
  private Mindstate mindstate;
  private Hygiene hygiene;

  public AnimalAggregate() {
  }

  private AnimalAggregate(String id,
                          Date timestamp,
                          Boolean existing,
                          FeelingOfSatiety feelingOfSatiety,
                          Mindstate mindstate,
                          Hygiene hygiene) {
    super(id, timestamp);
    this.existing = existing;
    this.feelingOfSatiety = feelingOfSatiety;
    this.mindstate = mindstate;
    this.hygiene = hygiene;
  }

  public FeelingOfSatiety getFeelingOfSatiety() {
    return feelingOfSatiety;
  }

  public Mindstate getMindstate() {
    return mindstate;
  }

  public Hygiene getHygiene() {
    return hygiene;
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
      public AnimalAggregate applyEvent(Bought event) {
        return new AnimalAggregate(event.getAnimalId(),
            event.getTimestamp(),
            true,
            FeelingOfSatiety.full,
            Mindstate.happy,
            Hygiene.tidy);
      }
    };
  }

  public EventApplier<Fed> asFedEventApplier() {
    return new EventApplier<Fed>() {
      @Override
      public AnimalAggregate applyEvent(Fed event) {
        return new AnimalAggregate(event.getAnimalId(),
            event.getTimestamp(),
            existing,
            feelingOfSatiety.better(),
            mindstate,
            hygiene);
      }
    };
  }

  public EventApplier<Digested> asDigestedEventApplier() {
    return new EventApplier<Digested>() {
      @Override
      public AnimalAggregate applyEvent(Digested event) {
        return new AnimalAggregate(event.getAnimalId(),
            event.getTimestamp(),
            existing,
            feelingOfSatiety.worse(),
            mindstate,
            hygiene);
      }
    };
  }

  public EventApplier<Died> asDiedEventApplier() {
    return new EventApplier<Died>() {
      @Override
      public AnimalAggregate applyEvent(Died event) {
        return new AnimalAggregate(event.getAnimalId(),
            event.getTimestamp(),
            false,
            feelingOfSatiety,
            mindstate,
            hygiene);
      }
    };
  }

}
