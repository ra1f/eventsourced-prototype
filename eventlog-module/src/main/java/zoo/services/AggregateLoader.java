package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import zoo.aggregates.Animal;
import zoo.events.Bought;
import zoo.events.Digested;
import zoo.exceptions.AggregateLoadException;
import zoo.persistence.EventLog;
import zoo.persistence.EventLogRepository;

import java.util.Collection;
import java.util.function.BiFunction;

/**
 * Created by dueerkopra on 08.04.2015.
 */
@Service
public class AggregateLoader {

  @Autowired
  private EventLogRepository eventLogRepository;

  public Animal replayAnimalAggregate(String animalId) throws AggregateLoadException {

    Collection<EventLog> events = eventLogRepository.findByAnimalId(animalId, new Sort(Sort.Direction.ASC, "occurence"));
    if (events.isEmpty()) {
      return new Animal();
    }

    BiFunction<Animal, ? super EventLog, Animal> reduceFunction = (animal, event) -> {
      if (event.getEvent().equals(Bought.class.getSimpleName())) {
        return animal.asBoughtEventApplier().applyEvent(new Bought(event.getAnimalId(), event.getOccurence()));
      } else if (event.getEvent().equals(Digested.class.getSimpleName())) {
        return animal.asDigestedEventApplier().applyEvent(new Digested(event.getAnimalId(), event.getOccurence()));
      } else {
        throw new RuntimeException(String.format("Event %s not handled", event));
      }};

    try {
      return events.stream().reduce(new Animal(), reduceFunction, (z1, z2) -> z2);
    } catch (Exception e) {
      throw new AggregateLoadException(e);
    }
  }
}
