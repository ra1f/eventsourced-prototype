package zoo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zoo.aggregates.Animal;
import zoo.commands.Buy;
import zoo.events.Event;
import zoo.persistence.EventLogRepository;
import zoo.services.AggregateLoader;

/**
 * Created by dueerkopra on 30.03.2015.
 */
@RestController
public class AnimalLifecycleController {

  @Autowired
  private AggregateLoader aggregateLoader;

  @Autowired
  private EventLogRepository eventLogRepository;

  private static final Logger logger = LoggerFactory.getLogger(AnimalLifecycleController.class);

  @RequestMapping(value = "/buy", method=RequestMethod.POST)
  public @ResponseBody CommandResult create(@RequestBody Buy command) {

    logger.info(command.toString());
    CommandResult result = new CommandResult();
    try {
      Animal animal = aggregateLoader.replayAnimalAggregate(command.getAnimalId());
      Iterable<Event> event = animal.asBuyCommandHandler().handleCommand(command);

      result.setSuccess(true);
    } catch (Exception e) {
      logger.error("Error in /buy", e);
    } finally {
      return result;
    }
  }
}
