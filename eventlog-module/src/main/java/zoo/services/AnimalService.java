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
    AnimalAggregate animalAggregate = replay(buy.getAnimalId());
    Events<Event> events = animalAggregate.asBuyCommandHandler().handleCommand(buy);
    if (!events.getEvents().isEmpty()) {
      eventStore.save(events);
    }
    return events.getSequenceId();
  }

  public Long sell(Sell sell) throws AggregateLoadException, ZooException {
    AnimalAggregate animalAggregate = replay(sell.getAnimalId());
    Events<Event> events = animalAggregate.asSellCommandHandler().handleCommand(sell);
    if (!events.getEvents().isEmpty()) {
      eventStore.save(events);
    }
    return events.getSequenceId();
  }

  public Long feed(Feed feed) throws AggregateLoadException, ZooException {
    AnimalAggregate animalAggregate = replay(feed.getAnimalId());
    Events<Event> events = animalAggregate.asFeedCommandHandler().handleCommand(feed);
    if (!events.getEvents().isEmpty()) {
      eventStore.save(events);
    }
    return events.getSequenceId();
  }

  public Long digest(Digest digest) throws AggregateLoadException, ZooException {
    AnimalAggregate animalAggregate = replay(digest.getAnimalId());
    Events<Event> events = animalAggregate.asDigestCommandHandler().handleCommand(digest);
    if (!events.getEvents().isEmpty()) {
      eventStore.save(events);
    }
    return events.getSequenceId();
  }

  public Long play(Play play) throws AggregateLoadException, ZooException {
    AnimalAggregate animalAggregate = replay(play.getAnimalId());
    Events<Event> events = animalAggregate.asPlayCommandHandler().handleCommand(play);
    if (!events.getEvents().isEmpty()) {
      eventStore.save(events);
    }
    return events.getSequenceId();
  }

  public Long sadden(Sadden sadden) throws AggregateLoadException, ZooException {
    AnimalAggregate animalAggregate = replay(sadden.getAnimalId());
    Events<Event> events = animalAggregate.asSaddenCommandHandler().handleCommand(sadden);
    if (!events.getEvents().isEmpty()) {
      eventStore.save(events);
    }
    return events.getSequenceId();
  }

  public void cleanUp(CleanUp cleanUp) throws AggregateLoadException, ZooException {
    throw new ZooException("Not implemented");
  }

  public void messUp(MessUp messUp) throws AggregateLoadException, ZooException {
    throw new ZooException("Not implemented");
  }

  private AnimalAggregate replay(String id) throws AggregateLoadException {
    return replayFromOrigin(id, eventStore);
  }


}
