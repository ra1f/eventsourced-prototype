package zoo.aggregates;

import org.springframework.data.domain.Sort;
import rx.Observable;
import zoo.events.Bought;
import zoo.persistence.EventLog;
import zoo.persistence.EventLogRepository;

import java.util.Collection;
import java.util.Date;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public abstract class Aggregate {

  protected String id;

  protected Date timestamp;

  public Aggregate(String id, Date timestamp) {
    this.id = id;
    this.timestamp = timestamp;
  }

  public Aggregate() {
  }

  public String getId() {
    return id;
  }

  public Observable<EventLog> publish(EventLogRepository eventLogRepository) {

    return Observable.create(observer -> {
      Collection<EventLog> events =
          eventLogRepository.findByOccurenceAfter(timestamp, new Sort(Sort.Direction.ASC, "occurence"));
      events.stream().forEach(event -> observer.onNext(event));
      observer.onCompleted();
    });
  }

}
