package zoo.observables;

/**
 * Created by dueerkopra on 13.04.2015.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import rx.Observable;
import rx.schedulers.Schedulers;

import java.util.concurrent.TimeUnit;

/**
 * Experimental stuff
 */
@RunWith(JUnit4.class)
public class ObservableTest {

  @Test
  public void blah() throws Exception {

    Observable
        .interval(2, TimeUnit.SECONDS)
        .map(x -> {return "Hello World";})
        .observeOn(Schedulers.newThread())
        .subscribe(n -> System.out.println(n));

    Thread.sleep(5000);

  }
}
