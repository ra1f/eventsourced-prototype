package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoo.aggregates.AnimalAggregate;
import zoo.commands.*;
import zoo.dto.Events;
import zoo.events.Event;
import zoo.exceptions.AggregateLoadException;
import zoo.exceptions.ZooException;

import static zoo.services.AggregateLoader.replayFromOrigin;

/**
 * Created by dueerkopra on 09.04.2015.
 */
@Service
public class AnimalService {

  @Autowired
  private EventStore eventStore;

  public Long buy(Buy buy) throws AggregateLoadException, ZooException {
    AnimalAggregate animalAggregate = replayFromOrigin(buy.getAnimalId(), eventStore);
    Events<Event> events = animalAggregate.asBuyCommandHandler().handleCommand(buy);
    if (!events.getEvents().isEmpty()) {
      eventStore.save(events);
    }
    return events.getSequenceId();
  }

  public void sell(Sell sell) throws AggregateLoadException, ZooException {
    throw new ZooException("Not implemented");
  }

  public Long feed(Feed feed) throws AggregateLoadException, ZooException {
    AnimalAggregate animalAggregate = replayFromOrigin(feed.getAnimalId(), eventStore);
    Events<Event> events = animalAggregate.asFeedCommandHandler().handleCommand(feed);
    if (!events.getEvents().isEmpty()) {
      eventStore.save(events);
    }
    return events.getSequenceId();
  }

  public Long digest(Digest digest) throws AggregateLoadException, ZooException {
    AnimalAggregate animalAggregate = replayFromOrigin(digest.getAnimalId(), eventStore);
    Events<Event> events = animalAggregate.asDigestCommandHandler().handleCommand(digest);
    if (!events.getEvents().isEmpty()) {
      eventStore.save(events);
    }
    return events.getSequenceId();
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
