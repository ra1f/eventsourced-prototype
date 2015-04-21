package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import zoo.dto.Events;
import zoo.events.Event;
import zoo.persistence.EventLogEntry;
import zoo.persistence.EventLogRepository;

import java.util.Collection;
import java.util.Date;

/**
 * Created by dueerkopra on 09.04.2015.
 */
@Component
public class EventStore {

  @Autowired
  private EventLogRepository eventLogRepository;

  private PublishSubject<Events> publishSubject = PublishSubject.create();
  // TODO: replace newThread by ThreadPool Executor
  private Observable<Events> observable = publishSubject.observeOn(Schedulers.newThread());

  /**
   *
   * @param events
   * @return sequenceId
   */
  public void save(Events<Event> events) {

    events.getEvents().stream().forEach(
        event -> eventLogRepository.save(new EventLogEntry(event.getAnimalId(),
            event.getClass().getSimpleName(), event.getSequenceId(),
            new Date())));

    if (!events.getEvents().isEmpty()) publishSubject.onNext(events);
  }

  public Collection<EventLogEntry> find(String animalId) {
    return eventLogRepository.findById(animalId, new Sort(Sort.Direction.ASC, "sequenceId"));
  }

  public Subscription subscribe(Action1<Events> onNext) {
    return observable.subscribe(onNext);
  }

}
