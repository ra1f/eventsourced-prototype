package zoo.services;

import org.springframework.stereotype.Component;
import zoo.aggregates.Aggregate;

import java.util.HashMap;

/**
 * Created by dueerkopra on 09.04.2015.
 */
@Component
public class AggregateRegistry {

  private HashMap<String, Aggregate> aggregateMap = new HashMap<>(23);

  public void register(Aggregate aggregate) {
    aggregateMap.put(aggregate.getId(), aggregate);
  }

  public Aggregate find(String id) {
    return aggregateMap.get(id);
  }

}
