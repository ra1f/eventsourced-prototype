package zoo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.functions.Action1;
import zoo.aggregates.AnimalAggregate;
import zoo.persistence.Animal;
import zoo.persistence.AnimalRepository;

/**
 * Created by dueerkopra on 14.04.2015.
 */
public class EventUpdateAdapter implements Action1<String> {

  private String id;
  private AggregateLoader aggregateLoader;
  private AnimalRepository animalRepository;

  private static final Logger logger = LoggerFactory.getLogger(EventUpdateAdapter.class);

  public EventUpdateAdapter(String id, AggregateLoader aggregateLoader, AnimalRepository animalRepository) {
    this.id = id;
    this.aggregateLoader = aggregateLoader;
    this.animalRepository = animalRepository;
  }

  @Override
  public void call(String animalId) {

    try {
      if (id.equals(animalId)) {
        AnimalAggregate animalAggregate = aggregateLoader.replayAnimalAggregate(animalId);
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
        aggregate.getFeelingOfSatiety(),
        aggregate.getMindstate(),
        aggregate.getHygiene()
    );
  }
}
