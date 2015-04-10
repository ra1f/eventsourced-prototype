package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import zoo.events.Event;
import zoo.persistence.EventLogEntry;
import zoo.persistence.EventLogRepository;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by dueerkopra on 09.04.2015.
 */
@Component
public class EventStore {

  @Autowired
  private EventLogRepository eventLogRepository;

  public void save(Iterable<EventLogEntry> eventLogs) {
    eventLogRepository.save(eventLogs);
  }

  public void saveEvents(Collection<Event> events) {
    save(events.stream().map(
        event -> new EventLogEntry(event.getClass().getSimpleName(), event.getAnimalId(), event.getTimestamp())).
        collect(Collectors.toList()));
  }

  public Collection<EventLogEntry> find(String animalId) {
    return eventLogRepository.findByAnimalId(animalId, new Sort(Sort.Direction.ASC, "occurence"));
  }


}
