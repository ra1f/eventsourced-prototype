package zoo.observables;

import rx.Observable;
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

    Observable<String> observable = publishSubject.observeOn(Schedulers.newThread());

    observable.subscribe(m -> {
      Long id = Thread.currentThread().getId();
      if (m.equals("Yo")) {
        System.out.println(String.format("%d-yo: I know you, %s.", id, m));
      } else {
        System.out.println(String.format("%d-yo: I don't know you, %s. Go away.", id, m));
      }
    });

    observable.subscribe(m -> {
      Long id = Thread.currentThread().getId();
      if (m.equals("Man")) {
        System.out.println(String.format("%d-man: I know you, %s.", id, m));
      } else {
        System.out.println(String.format("%d-man: I don't know you, %s. Go away.", id, m));
      }
    });

    observable.subscribe(m -> {
      Long id = Thread.currentThread().getId();
      if (m.equals("Yep")) {
        System.out.println(String.format("%d-yep: I know you, %s.", id, m));
      } else {
        System.out.println(String.format("%d-yep: I don't know you, %s. Go away.", id, m));
      }
    });

    publishSubject.onNext("Yo");
    publishSubject.onNext("Man");
    publishSubject.onNext("Yep");

    Thread.sleep(30000);

  }
}
