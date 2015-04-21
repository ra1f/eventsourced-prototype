package zoo.aggregates;

import zoo.commands.*;
import zoo.dto.Events;
import zoo.events.*;
import zoo.exceptions.AnimalAlreadyThereException;
import zoo.exceptions.CommandNotInSequenceException;
import zoo.exceptions.NoSuchAnimalException;
import zoo.exceptions.ZooException;
import zoo.states.FeelingOfSatiety;
import zoo.states.Hygiene;
import zoo.states.Mindstate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class AnimalAggregate extends Aggregate {

  private final Date timestamp;
  private final Boolean existing;
  private final FeelingOfSatiety feelingOfSatiety;
  private final Mindstate mindstate;
  private final Hygiene hygiene;

  public AnimalAggregate() {
    super();
    this.timestamp = null;
    this.existing = false;
    this.feelingOfSatiety = null;
    this.mindstate = null;
    this.hygiene = null;
  }

  private AnimalAggregate(String id,
                          Long sequenceId,
                          Date timestamp,
                          Boolean existing,
                          FeelingOfSatiety feelingOfSatiety,
                          Mindstate mindstate,
                          Hygiene hygiene) {
    super(id, sequenceId);
    this.timestamp = timestamp;
    this.existing = existing;
    this.feelingOfSatiety = feelingOfSatiety;
    this.mindstate = mindstate;
    this.hygiene = hygiene;
  }

  public Date getTimestamp() {
    return timestamp;
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

  // Command Handlers

  public CommandHandler<Buy, Event> asBuyCommandHandler() {
    return new CommandHandler<Buy, Event>() {
      @Override
      public Events<Event> handleCommand(Buy command) throws ZooException {
        if (existing) {
          throw new AnimalAlreadyThereException(command.toString());
        }
        return handleIdempotency(command,
            Arrays.asList(new Bought(command.getAnimalId(), command.getSequenceId())));
      }
    };
  }

  public CommandHandler<Sell, Event> asSellCommandHandler() {
    return new CommandHandler<Sell, Event>() {
      @Override
      public Events<Event> handleCommand(Sell command) throws ZooException {
        validateExistence(command);
        return handleIdempotency(command,
            Arrays.asList(new Sold(command.getAnimalId(), command.getSequenceId())));
      }
    };
  }

  public CommandHandler<Feed, Event> asFeedCommandHandler() {
    return new CommandHandler<Feed, Event>() {

      @Override
      public Events<Event> handleCommand(Feed command) throws ZooException {
        validateExistence(command);
        return handleIdempotency(
            command, Arrays.asList(new Fed(command.getAnimalId(), command.getSequenceId())));
      }
    };
  }

  public CommandHandler<Digest, Event> asDigestCommandHandler() {
    return new CommandHandler<Digest, Event>() {
      @Override
      public Events<Event> handleCommand(Digest command) throws ZooException {
        validateExistence(command);
        Event event = new Digested(command.getAnimalId(), command.getSequenceId());
        if (feelingOfSatiety.isWorst()) {
          event = new Died(command.getAnimalId(), command.getSequenceId());
        }
        return handleIdempotency(
            command, Arrays.asList(event));
      }
    };
  }

  public CommandHandler<Play, Event> asPlayCommandHandler() {
    return new CommandHandler<Play, Event>() {
      @Override
      public Events<Event> handleCommand(Play command) throws ZooException {
        validateExistence(command);
        return handleIdempotency(command,
            Arrays.asList(new Played(command.getAnimalId(), command.getSequenceId())));
      }
    };
  }

  public CommandHandler<Sadden, Event> asSaddenCommandHandler() {
    return new CommandHandler<Sadden, Event>() {
      @Override
      public Events<Event> handleCommand(Sadden command) throws ZooException {
        validateExistence(command);
        Event event = new Saddened(command.getAnimalId(), command.getSequenceId());
        if (mindstate.isWorst()) {
          event = new Died(command.getAnimalId(), command.getSequenceId());
        }
        return handleIdempotency(command, Arrays.asList(event));
      }
    };
  }

  // Event Appliers

  public EventApplier<Bought> asBoughtEventApplier() {
    return new EventApplier<Bought>() {
      @Override
      public AnimalAggregate applyEvent(Bought event) {
        return new AnimalAggregate(event.getAnimalId(),
            event.getSequenceId(),
            new Date(),
            true,
            FeelingOfSatiety.full,
            Mindstate.happy,
            Hygiene.tidy);
      }
    };
  }

  public EventApplier<Sold> asSoldEventApplier() {
    return new EventApplier<Sold>() {
      @Override
      public AnimalAggregate applyEvent(Sold event) {
        return new AnimalAggregate(event.getAnimalId(),
            event.getSequenceId(),
            new Date(),
            false,
            feelingOfSatiety,
            mindstate,
            hygiene);
      }
    };
  }

  public EventApplier<Fed> asFedEventApplier() {
    return new EventApplier<Fed>() {
      @Override
      public AnimalAggregate applyEvent(Fed event) {
        return new AnimalAggregate(event.getAnimalId(),
            event.getSequenceId(),
            new Date(),
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
            event.getSequenceId(),
            new Date(),
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
            event.getSequenceId(),
            new Date(),
            false,
            feelingOfSatiety,
            mindstate,
            hygiene);
      }
    };
  }

  public EventApplier<Played> asPlayedEventApplier() {
    return new EventApplier<Played>() {
      @Override
      public AnimalAggregate applyEvent(Played event) {
        return new AnimalAggregate(event.getAnimalId(),
            event.getSequenceId(),
            new Date(),
            existing,
            feelingOfSatiety,
            mindstate.better(),
            hygiene);
      }
    };
  }

  public EventApplier<Saddened> asSaddenedEventApplier() {
    return new EventApplier<Saddened>() {
      @Override
      public AnimalAggregate applyEvent(Saddened event) {
        return new AnimalAggregate(event.getAnimalId(),
            event.getSequenceId(),
            new Date(),
            existing,
            feelingOfSatiety,
            mindstate.worse(),
            hygiene);
      }
    };
  }

  private void validateExistence(Command command) throws NoSuchAnimalException {
    if (!existing) {
      throw new NoSuchAnimalException(command.toString());
    }
  }

  private Events<Event> handleIdempotency(Command command, Collection<Event> events) throws CommandNotInSequenceException {

    long nextSeqId = this.sequenceId + 1;

    // Check for a gap
    if (command.getSequenceId() > nextSeqId) {
      throw new CommandNotInSequenceException(command.toString());
    }

    // Only add those events which are not yet saved, the other ones are ignored => Idempotency
    Collection<Event> eventsToSave = new ArrayList<>();
    for (Event event: events) {
      if (event.getSequenceId() == nextSeqId) {
        eventsToSave.add(event);
        nextSeqId++;
      }
    }

    Events<Event> retVal = new Events<>(command.getAnimalId(), eventsToSave, this.sequenceId + events.size());
    return retVal;
  }

}
