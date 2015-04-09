package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zoo.aggregates.Animal;
import zoo.events.Bought;
import zoo.events.Digested;
import zoo.events.Event;
import zoo.events.Fed;
import zoo.persistence.EventLogEntry;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by dueerkopra on 08.04.2015.
 */
@Component
public class EventPublisher {

  @Autowired
  private EventStore eventStore;

  @Autowired
  private AggregateRegistry aggregateRegistry;

  private BiFunction<Animal, ? super Event, Animal> reduceNewEvents = (animal, event) -> {
    if (event instanceof Bought) {
      return animal.asBoughtEventApplier().applyEvent((Bought)event);
    } else if (event instanceof Fed) {
      return animal.asFedEventApplier().applyEvent((Fed)event);
    } else if (event instanceof Digested) {
      return animal.asDigestedEventApplier().applyEvent((Digested)event);
    } else {
      throw new RuntimeException(String.format("Event %s not handled", event));
    }};

  public void publish(Collection<Event> events, Animal animal) {

    eventStore.save(events.stream().map(
        event -> new EventLogEntry(event.getClass().getSimpleName(), event.getAnimalId(), event.getTimestamp())).
          collect(Collectors.toList()));

    aggregateRegistry.register(events.stream().reduce(animal, reduceNewEvents, (z1, z2) -> z2));
  }

}
