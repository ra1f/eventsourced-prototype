package zoo.aggregates;

import zoo.commands.Buy;
import zoo.commands.Digest;
import zoo.commands.Feed;
import zoo.events.Bought;
import zoo.events.Digested;
import zoo.events.Event;
import zoo.events.Fed;
import zoo.exceptions.AnimalAlreadyThereException;
import zoo.exceptions.AnimalNotThereException;
import zoo.exceptions.ZooException;
import zoo.states.FeelingOfSatiety;

import java.util.Arrays;
import java.util.Collection;
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

  public Animal() {
  }

  public FeelingOfSatiety getFeelingOfSatiety() {
    return feelingOfSatiety;
  }

  public CommandHandler<Buy, Event> asBuyCommandHandler() {
    return new CommandHandler<Buy, Event>() {
      @Override
      public Collection<Event> handleCommand(Buy command) throws ZooException {
        if (id != null) {
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
        if (id == null) {
          throw new AnimalNotThereException(command.getAnimalId());
        }
        return Arrays.asList(new Fed(command.getAnimalId(), command.getTimestamp()));
      }
    };
  }

  public CommandHandler<Digest, Event> asDigestCommandhandler() {
    return new CommandHandler<Digest, Event>() {
      @Override
      public Collection<Event> handleCommand(Digest command) throws ZooException {
        if (id == null) {
          throw new AnimalNotThereException(command.getAnimalId());
        }
        return Arrays.asList(new Digested(command.getAnimalId(), command.getTimestamp()));
      }
    };
  }

  public EventApplier<Bought> asBoughtEventApplier() {
    return new EventApplier<Bought>() {
      @Override
      public Animal applyEvent(Bought event) {
        return new Animal(event.getAnimalId(), event.getTimestamp(), FeelingOfSatiety.full);
      }
    };
  }

  public EventApplier<Fed> asFedEventApplier() {
    return new EventApplier<Fed>() {
      @Override
      public Animal applyEvent(Fed event) {
        return new Animal(event.getAnimalId(), event.getTimestamp(), feelingOfSatiety.better());
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
