package zoo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import zoo.dto.Events;
import zoo.persistence.AnimalRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * Created by dueerkopra on 14.04.2015.
 */
@Component
public class ViewUpdaterService implements Action1<Events> {

  @Autowired
  private EventStore eventStore;

  @Autowired
  private AnimalRepository animalRepository;

  private PublishSubject<Events> publishSubject = PublishSubject.create();
  // TODO: replace newThread by ThreadPool Executor
  private Observable<Events> observable = publishSubject.observeOn(Schedulers.newThread());

  private HashMap<String, EventUpdateAdapter> adapterRegistry = new HashMap(11);

  private static final Logger logger = LoggerFactory.getLogger(ViewUpdaterService.class);

  @PostConstruct
  public void init() {
    eventStore.subscribe(this);
  }

  @Override
  public void call(Events events) {

    EventUpdateAdapter adapter = adapterRegistry.get(events.getId());
    try {
      if (adapter == null) {
        adapter = new EventUpdateAdapter(events.getId(),
            eventStore,
            animalRepository,
            observable);
        adapterRegistry.put(events.getId(), adapter);
      }
    } catch (Exception e) {
      logger.error("Error creating EventViewAdapter", e);
    }
    publishSubject.onNext(events);
  }
}
