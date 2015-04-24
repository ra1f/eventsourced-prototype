package zoo.observables;

import rx.Observable;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by dueerkopra on 13.04.2015.
 *
 *
 * Experimental stuff
 */
public class MoreObservableTests {

  public static void main(String[] args) throws Exception {

    PublishSubject<String> publishSubject = PublishSubject.create();

    Observable<String> observable = Observable.just("Yo1", "Yo2", "Yo3", "Yo4", "Yo5", "Yo6")
                                          .mergeWith(Observable.just("Man1", "Man2", "Man3", "Man4", "Man5", "Man6"))
                                          .observeOn(Schedulers.newThread())
                                          .mergeWith(publishSubject);

    observable.subscribe(onNext("Yo"));
    observable.subscribe(onNext("Man"));
    observable.subscribe(onNext("Yep"));
    observable.subscribe(onNext("Foo"));
    observable.subscribe(onNext("Bar"));

    Thread.sleep(120000);

  }

  private static Action1<String> onNext(String name) {
    return m -> {
      Long id = Thread.currentThread().getId();
      if (m.startsWith(name)) {
        System.out.println(String.format("%d-%s: I know you, %s.", id, name.toLowerCase(), m));
      } else {
        System.out.println(String.format("%d-%s: I don't know you, %s. Go away.", id, name.toLowerCase(), m));
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    };
  }

}

