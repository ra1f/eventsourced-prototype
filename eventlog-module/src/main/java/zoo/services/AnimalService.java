package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoo.aggregates.Animal;
import zoo.commands.*;
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
  private EventStore eventStore;

  public void buy(Buy buy) throws AggregateLoadException, ZooException {
    Animal animal = aggregateLoader.replayAnimalAggregate(buy.getAnimalId());
    Collection<Event> events = animal.asBuyCommandHandler().handleCommand(buy);
    eventStore.saveEvents(events);
  }

  public void sell(Sell sell) throws AggregateLoadException, ZooException {
    throw new ZooException("Not implemented");
  }

  public void feed(Feed feed) throws AggregateLoadException, ZooException {
    Animal animal = aggregateLoader.replayAnimalAggregate(feed.getAnimalId());
    Collection<Event> events = animal.asFeedCommandHandler().handleCommand(feed);
    eventStore.saveEvents(events);
  }

  public void digest(Digest digest) throws AggregateLoadException, ZooException {
    Animal animal = aggregateLoader.replayAnimalAggregate(digest.getAnimalId());
    Collection<Event> events = animal.asDigestCommandHandler().handleCommand(digest);
    eventStore.saveEvents(events);
  }

  public void play(Play play) throws AggregateLoadException, ZooException {
    throw new ZooException("Not implemented");
  }

  public void sadden(Sadden sadden) throws AggregateLoadException, ZooException {
    throw new ZooException("Not implemented");
  }

  public void cleanUp(CleanUp cleanUp) throws AggregateLoadException, ZooException {
    throw new ZooException("Not implemented");
  }

  public void messUp(MessUp messUp) throws AggregateLoadException, ZooException {
    throw new ZooException("Not implemented");
  }




}
