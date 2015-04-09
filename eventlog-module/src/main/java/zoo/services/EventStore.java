package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import zoo.persistence.EventLogEntry;
import zoo.persistence.EventLogRepository;

import java.util.Collection;

/**
 * Created by dueerkopra on 09.04.2015.
 */
@Component
public class EventStore {

  @Autowired
  private EventLogRepository eventLogRepository;

  public void save(Iterable<EventLogEntry> events) {
    eventLogRepository.save(events);
  }

  public Collection<EventLogEntry> find(String animalId) {
    return eventLogRepository.findByAnimalId(animalId, new Sort(Sort.Direction.ASC, "occurence"));
  }


}
