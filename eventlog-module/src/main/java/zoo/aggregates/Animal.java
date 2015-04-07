package zoo.aggregates;

import zoo.commands.Buy;
import zoo.events.Bought;

/**
 * Created by dueerkopra on 07.04.2015.
 */
public class Animal extends Aggregate implements CommandHandler<Buy, Bought> {

  public Animal(String id) {
    super(id);
  }

  public Animal() {
  }

  @Override
  public Iterable<Bought> handleCommand(Buy command) {
    if (id != null) {
      throw new Exception();
    }
    return new Arrays.;
  }
}
