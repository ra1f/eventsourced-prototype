package zoo.aggregates;

import zoo.commands.Buy;
import zoo.commands.Command;
import zoo.commands.Digest;
import zoo.commands.Feed;
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

    Events<Event> retVal = new Events<>(id, eventsToSave, this.sequenceId + events.size());
    return retVal;
  }

}
