package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoo.aggregates.Animal;
import zoo.commands.Buy;
import zoo.commands.Feed;
import zoo.events.Event;
import zoo.exceptions.AggregateLoadException;
import zoo.exceptions.ZooException;

import java.util.Collection;

/**
 * Created by dueerkopra on 09.04.2015.
 */
@Service
public class AnimalService {

  @Autowired
  private AggregateLoader aggregateLoader;

  @Autowired
  private EventPublisher publisher;

  public void buy(Buy buy) throws AggregateLoadException, ZooException {
    Animal animal = aggregateLoader.replayAnimalAggregate(buy.getAnimalId());
    Collection<Event> events = animal.asBuyCommandHandler().handleCommand(buy);
    publisher.publish(events, animal);
  }

  public void feed(Feed feed) throws AggregateLoadException, ZooException {
    Animal animal = aggregateLoader.replayAnimalAggregate(feed.getAnimalId());
    Collection<Event> events = animal.asFeedCommandHandler().handleCommand(feed);
    publisher.publish(events, animal);
  }
}
