package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoo.aggregates.Animal;
import zoo.events.Event;
import zoo.persistence.EventLog;
import zoo.persistence.EventLogRepository;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by dueerkopra on 08.04.2015.
 */
@Service
public class EventPublisher {

  @Autowired
  private EventLogRepository eventLogRepository;

  public void publish(Collection<Event> events, Animal animal) {

    eventLogRepository.save(events.stream().map(
        event -> new EventLog(event.getClass().getSimpleName(), event.getAnimalId(), event.getTimestamp())).
        collect(Collectors.toList()));


  }
}
