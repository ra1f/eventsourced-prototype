package zoo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import zoo.dto.Events;
import zoo.events.Event;
import zoo.exceptions.NotIdempotentException;
import zoo.persistence.EventLogEntry;
import zoo.persistence.EventLogRepository;

import java.util.Collection;
import java.util.Date;

/**
 * Created by dueerkopra on 09.04.2015.
 */
@Component
public class EventStore {

  private static final Logger logger = LoggerFactory.getLogger(EventStore.class);

  @Autowired
  private EventLogRepository eventLogRepository;

  private PublishSubject<Events> publishSubject = PublishSubject.create();
  // TODO: replace newThread by ThreadPool Executor
  private Observable<Events> observable = publishSubject.observeOn(Schedulers.newThread());

  /**
   * @param events
   * @return sequenceId
   */
  public void save(Events<Event> events) throws NotIdempotentException {

    for (Event event : events.getEvents()) {
      try {
        eventLogRepository.insert(event.getAnimalId(),
            event.getSequenceId(),
            event.getClass().getSimpleName(),
            new Date());
      } catch (DataIntegrityViolationException e) {
        logger.info(String.format("%s already persisted in eventstore", event));
        checkIdempotency(event);
      }
    }

    Events remainder = new Events(events.getId(), events.getSequenceId(), events.getEvents());
    publishSubject.onNext(remainder);
  }

  public Collection<EventLogEntry> find(String animalId, Long sequenceId) {
    return eventLogRepository.findByIdAndSequenceIdLessThan(
        animalId,
        sequenceId,
        new Sort(Sort.Direction.ASC, "sequenceId"));
  }

  public Subscription subscribe(Action1<Events> onNext) {
    return observable.subscribe(onNext);
  }

  private void checkIdempotency(Event event) throws NotIdempotentException {
    EventLogEntry eventLogEntry = eventLogRepository.findOne(
        new EventLogEntry.PrimaryKey(
            event.getAnimalId(),
            event.getSequenceId()));

    if (eventLogEntry != null && !event.getClass().getSimpleName().equals(eventLogEntry.getEvent())) {
      throw new NotIdempotentException("There is already such an event but with different payload");
    }
  }

}
