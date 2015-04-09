package zoo.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public interface EventLogRepository extends CrudRepository<EventLogEntry, Long> {
  Collection<EventLogEntry> findByAnimalId(String animalId, Sort sort);
}
