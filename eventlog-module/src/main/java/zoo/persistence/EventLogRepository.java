package zoo.persistence;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Date;

/**
 * Created by dueerkopra on 30.03.2015.
 */
public interface EventLogRepository extends JpaRepository<EventLogEntry, EventLogEntry.PrimaryKey> {
  Collection<EventLogEntry> findByIdAndSequenceIdLessThan(String id, Long sequenceId, Sort sort);

  @Modifying
  @Transactional
  @Query(value = "insert into eventlog (agg_id, seq_id, event, occurence) values (?1, ?2, ?3, ?4)", nativeQuery = true)
  void insert(String id, Long sequenceId, String event, Date occurence);
}
