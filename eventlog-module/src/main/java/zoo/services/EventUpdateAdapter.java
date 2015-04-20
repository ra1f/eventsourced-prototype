package zoo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.functions.Action1;
import zoo.aggregates.AnimalAggregate;
import zoo.dto.Events;
import zoo.exceptions.AggregateLoadException;
import zoo.persistence.Animal;
import zoo.persistence.AnimalRepository;
import static zoo.services.AggregateLoader.*;

/**
 * Created by dueerkopra on 14.04.2015.
 */
public class EventUpdateAdapter implements Action1<Events> {

  private String id;
  private AnimalRepository animalRepository;
  private AnimalAggregate animalAggregate;

  private static final Logger logger = LoggerFactory.getLogger(EventUpdateAdapter.class);

  public EventUpdateAdapter(String id, EventStore eventStore, AnimalRepository animalRepository) throws AggregateLoadException {
    this.id = id;
    this.animalRepository = animalRepository;
    this.animalAggregate = replayFromOrigin(id, eventStore);
  }

  @Override
  public void call(Events events) {

    try {
      if (id.equals(events.getId())) {

        animalAggregate = replayFromSnapshot(animalAggregate, events.getEvents());

        Animal animal = map(animalAggregate);
        if (animalAggregate.isExisting()) {
          animalRepository.save(animal);
        } else {
          animalRepository.delete(animal);
        }
      }
    } catch (Exception e) {
      logger.error("Error updating materialized view", e);
    }
  }

  private Animal map(AnimalAggregate aggregate) {
    return new Animal(aggregate.getId(),
        aggregate.getSequenceId(),
        aggregate.getTimestamp(),
        aggregate.getFeelingOfSatiety(),
        aggregate.getMindstate(),
        aggregate.getHygiene()
    );
  }
}
