package zoo.persistence;

import org.springframework.data.repository.CrudRepository;

import java.util.Date;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public interface EventLogRepository extends CrudRepository<EventLog, Long> {
  EventLog findByOccurence(Date occurence);
}
