package zoo.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.Date;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public interface EventLogRepository extends CrudRepository<EventLog, Long> {
  Collection<EventLog> findByAnimalId(String animalId, Sort sort);
  Collection<EventLog> findByOccurenceAfter(Date occurence, Sort sort);
}
