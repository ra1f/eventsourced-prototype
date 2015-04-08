package zoo.services;

import rx.Subscriber;
import zoo.aggregates.Aggregate;
import zoo.persistence.EventLog;

/**
 * Created by rdu on 09.04.15.
 */
public class EventLogSubscriber extends Subscriber<EventLog> {

  private Aggregate aggregate;

  public EventLogSubscriber(Aggregate aggregate) {
    this.aggregate = aggregate;
  }

  @Override
  public void onCompleted() {

  }

  @Override
  public void onError(Throwable throwable) {

  }

  @Override
  public void onNext(EventLog eventLog) {

  }
}
