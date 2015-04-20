package zoo.services;

import zoo.aggregates.AnimalAggregate;
import zoo.events.*;
import zoo.exceptions.AggregateLoadException;
import zoo.persistence.EventLogEntry;

import java.util.Collection;
import java.util.function.BiFunction;

/**
 * Created by dueerkopra on 08.04.2015.
 */
public class AggregateLoader {

  private static BiFunction<AnimalAggregate, ? super EventLogEntry, AnimalAggregate> reduceByEventLogEntry = (aggregate, eventLogEntry) -> {
    if (eventLogEntry.getEvent().equals(Bought.class.getSimpleName())) {
      return aggregate.asBoughtEventApplier().applyEvent(new Bought(eventLogEntry.getId(),
          eventLogEntry.getSequenceId(),
          eventLogEntry.getTransactionId()));
    } if (eventLogEntry.getEvent().equals(Died.class.getSimpleName())) {
      return aggregate.asDiedEventApplier().applyEvent(new Died(eventLogEntry.getId(),
          eventLogEntry.getSequenceId(),
          eventLogEntry.getTransactionId()));
    } else if (eventLogEntry.getEvent().equals(Fed.class.getSimpleName())) {
      return aggregate.asFedEventApplier().applyEvent(new Fed(eventLogEntry.getId(),
          eventLogEntry.getSequenceId(),
          eventLogEntry.getTransactionId()));
    } else if (eventLogEntry.getEvent().equals(Digested.class.getSimpleName())) {
      return aggregate.asDigestedEventApplier().applyEvent(new Digested(eventLogEntry.getId(),
          eventLogEntry.getSequenceId(),
          eventLogEntry.getTransactionId()));
    } else {
      throw new RuntimeException(String.format("Event %s not handled", eventLogEntry));
    }
  };

  private static BiFunction<AnimalAggregate, ? super Event, AnimalAggregate> reduceByEvent = (aggregate, event) -> {
    if (event instanceof Bought) {
      return aggregate.asBoughtEventApplier().applyEvent((Bought) event);
    }
    else if (event instanceof Died) {
      return aggregate.asDiedEventApplier().applyEvent((Died) event);
    }
    else if (event instanceof Fed) {
      return aggregate.asFedEventApplier().applyEvent((Fed) event);
    } else if (event instanceof Digested) {
      return aggregate.asDigestedEventApplier().applyEvent((Digested) event);
    } else {
      throw new RuntimeException(String.format("Event %s not handled", event));
    }
  };

  public static AnimalAggregate replayFromOrigin(String animalId, EventStore eventStore) throws AggregateLoadException {

    // Are there entries within the eventlog?
    Collection<EventLogEntry> eventLogs = eventStore.find(animalId);
    if (eventLogs.isEmpty()) {
      // If not emit his as a starting point
      return new AnimalAggregate();
    }

    // If so replay state;
    try {
      return eventLogs.stream().reduce(new AnimalAggregate(), reduceByEventLogEntry, (z1, z2) -> z2);//TODO: try to parallelise here
    } catch (Exception e) {
      throw new AggregateLoadException(e);
    }
  }

  public static AnimalAggregate replayFromSnapshot(AnimalAggregate snapshot, Collection<Event> events) throws AggregateLoadException {

    try {
      return events.stream().reduce(snapshot, reduceByEvent, (z1, z2) -> z2);
    } catch (Exception e) {
      throw new AggregateLoadException(e);
    }
  }
}
