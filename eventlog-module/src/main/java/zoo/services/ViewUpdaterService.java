package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import zoo.persistence.AnimalRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * Created by dueerkopra on 14.04.2015.
 */
@Component
public class ViewUpdaterService implements Action1<String> {

  @Autowired
  private AggregateLoader aggregateLoader;

  @Autowired
  private EventStore eventStore;

  @Autowired
  private AnimalRepository animalRepository;

  private Subscription subscription;

  private PublishSubject<String> publishSubject = PublishSubject.create();
  // TODO: replace newThread by ThreadPool Executor
  private Observable<String> observable = publishSubject.observeOn(Schedulers.newThread());

  private HashMap<String, EventUpdateAdapter> adapterRegistry = new HashMap(11);

  @PostConstruct
  public void init() {
    subscription = eventStore.subscribe(this);
  }

  @Override
  public void call(String animalId) {
    EventUpdateAdapter adapter = adapterRegistry.get(animalId);
    if (adapter == null) {
      adapter = new EventUpdateAdapter(animalId, aggregateLoader, animalRepository);
      adapterRegistry.put(animalId, adapter);
      observable.subscribe(adapter);
    }
    publishSubject.onNext(animalId);
  }
}
