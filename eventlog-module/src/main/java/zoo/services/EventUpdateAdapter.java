package zoo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import zoo.aggregates.AnimalAggregate;
import zoo.dto.Events;
import zoo.exceptions.AggregateLoadException;
import zoo.persistence.Animal;
import zoo.persistence.AnimalRepository;

import java.util.Map;

import static zoo.services.AggregateLoader.replayFromOrigin;
import static zoo.services.AggregateLoader.replayFromSnapshot;

/**
 * Created by dueerkopra on 14.04.2015.
 */
public class EventUpdateAdapter implements Action1<Events> {

  private String id;
  private AnimalRepository animalRepository;
  private AnimalAggregate animalAggregate;
  private Subscription subscription;
  private Map<String, EventUpdateAdapter> adapterRegistry;

  private static final Logger logger = LoggerFactory.getLogger(EventUpdateAdapter.class);

  public EventUpdateAdapter(String id,
                            Long sequenceId,
                            EventStore eventStore,
                            AnimalRepository animalRepository,
                            Observable<Events> observable,
                            Map<String, EventUpdateAdapter> adapterRegistry) throws AggregateLoadException {
    this.id = id;
    this.animalRepository = animalRepository;
    subscription = observable.subscribe(this);
    animalAggregate = replayFromOrigin(id, sequenceId, eventStore);
    adapterRegistry.put(id, this);
    this.adapterRegistry = adapterRegistry;
  }

  @Override
  public void call(Events events) {

    try {
      if (id.equals(events.getId())) {
        animalAggregate = replayFromSnapshot(animalAggregate, events.getEvents());
        Animal animal = merge(animalAggregate, createAnimal(events.getId()));
        if (animalAggregate.isExisting()) {
          animalRepository.save(animal);
        } else {
          animalRepository.delete(animal);
          unsubscribe();
        }
        animalRepository.flush();
      }
    } catch (Exception e) {
      logger.error("Error updating materialized view", e);
      unsubscribe();
    }
  }

  private Animal merge(AnimalAggregate aggregate, Animal animal) {
    animal.setAnimalId(aggregate.getId());
    animal.setSequenceId(aggregate.getSequenceId());
    animal.setLastOccurence(aggregate.getTimestamp());
    animal.setFeelingOfSatiety(aggregate.getFeelingOfSatiety());
    animal.setMindstate(aggregate.getMindstate());
    animal.setHygiene(aggregate.getHygiene());
    return animal;
  }

  private void unsubscribe() {
    subscription.unsubscribe();
    this.adapterRegistry.remove(this.id);
  }

  private Animal createAnimal(String id) {
    Animal animal = animalRepository.findOne(id);
    if (animal == null) {
      animal = new Animal();
    }
    return animal;
  }
}
