package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zoo.aggregates.Animal;
import zoo.events.Bought;
import zoo.events.Died;
import zoo.events.Digested;
import zoo.events.Fed;
import zoo.exceptions.AggregateLoadException;
import zoo.persistence.EventLogEntry;

import java.util.Collection;
import java.util.function.BiFunction;

/**
 * Created by dueerkopra on 08.04.2015.
 */
@Component
public class AggregateLoader {

  @Autowired
  private EventStore eventStore;
  @Autowired
  private AggregateRegistry aggregateRegistry;

  private BiFunction<Animal, ? super EventLogEntry, Animal> reduceFromHistory = (animal, eventLogEntry) -> {
    if (eventLogEntry.getEvent().equals(Bought.class.getSimpleName())) {
      return animal.asBoughtEventApplier().applyEvent(new Bought(eventLogEntry.getAnimalId(), eventLogEntry.getOccurence()));
    } if (eventLogEntry.getEvent().equals(Died.class.getSimpleName())) {
      return animal.asDiedEventApplier().applyEvent(new Died(eventLogEntry.getAnimalId(), eventLogEntry.getOccurence()));
    } else if (eventLogEntry.getEvent().equals(Fed.class.getSimpleName())) {
      return animal.asFedEventApplier().applyEvent(new Fed(eventLogEntry.getAnimalId(), eventLogEntry.getOccurence()));
    } else if (eventLogEntry.getEvent().equals(Digested.class.getSimpleName())) {
      return animal.asDigestedEventApplier().applyEvent(new Digested(eventLogEntry.getAnimalId(), eventLogEntry.getOccurence()));
    } else {
      throw new RuntimeException(String.format("Event %s not handled", eventLogEntry));
    }
  };

  public Animal replayAnimalAggregate(String animalId) throws AggregateLoadException {

    // Is there already a snapshot in memory?
    Animal animal = (Animal) aggregateRegistry.findSnapshot(animalId);
    if (animal != null) {
      return animal;
    }

    // Are there entries within the eventlog?
    Collection<EventLogEntry> eventLogs = eventStore.find(animalId);
    if (eventLogs.isEmpty()) {
      return new Animal();
    }

    // If so replay state;
    try {
      return eventLogs.stream().reduce(new Animal(), reduceFromHistory, (z1, z2) -> z2);//TODO: try to parallelise here
    } catch (Exception e) {
      throw new AggregateLoadException(e);
    }
  }
}
