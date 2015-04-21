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

import static zoo.services.AggregateLoader.replayFromOrigin;
import static zoo.services.AggregateLoader.replayFromSnapshot;

/**
 * Created by dueerkopra on 14.04.2015.
 */
public class EventUpdateAdapter implements Action1<Events> {

  private String id;
  private AnimalRepository animalRepository;
  private AnimalAggregate animalAggregate;
  boolean justMaterialized = true;
  private Subscription subscription;

  private static final Logger logger = LoggerFactory.getLogger(EventUpdateAdapter.class);

  public EventUpdateAdapter(String id, EventStore eventStore, AnimalRepository animalRepository, Observable<Events> observable) throws AggregateLoadException {
    this.id = id;
    this.animalRepository = animalRepository;
    subscription = observable.subscribe(this);
    animalAggregate = replayFromOrigin(id, eventStore);
  }

  @Override
  public void call(Events events) {

    try {
      if (id.equals(events.getId())) {

        //If this is the first invocation we must not sync here since the state is already up to date because we just
        // materialised from event store milli seconds ago.
        Animal animal;
        if (!justMaterialized) {
          animalAggregate = replayFromSnapshot(animalAggregate, events.getEvents());
          animal = animalRepository.findOne(events.getId());
        } else {
          justMaterialized = false;
          animal = new Animal();
        }

        animal = merge(animalAggregate, animal);
        if (animalAggregate.isExisting()) {
          animalRepository.save(animal);
        } else {
          animalRepository.delete(animal);
          subscription.unsubscribe();
        }
        animalRepository.flush();
      }
    } catch (Exception e) {
      logger.error("Error updating materialized view", e);
    }
  }

  /*private Animal map(AnimalAggregate aggregate) {
    return new Animal(aggregate.getId(),
        aggregate.getSequenceId(),
        aggregate.getTimestamp(),
        aggregate.getFeelingOfSatiety(),
        aggregate.getMindstate(),
        aggregate.getHygiene()
    );
  }*/

  private Animal merge(AnimalAggregate aggregate, Animal animal) {
    animal.setAnimalId(aggregate.getId());
    animal.setSequenceId(aggregate.getSequenceId());
    animal.setLastOccurence(aggregate.getTimestamp());
    animal.setFeelingOfSatiety(aggregate.getFeelingOfSatiety());
    animal.setMindstate(aggregate.getMindstate());
    animal.setHygiene(aggregate.getHygiene());
    return animal;
  }
}
