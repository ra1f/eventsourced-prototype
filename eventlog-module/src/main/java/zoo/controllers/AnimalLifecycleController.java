package zoo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zoo.commands.*;
import zoo.exceptions.AggregateLoadException;
import zoo.exceptions.NotIdempotentException;
import zoo.exceptions.ZooException;
import zoo.services.AnimalService;

/**
 * Created by dueerkopra on 30.03.2015.
 */
@RestController
public class AnimalLifecycleController {

  @Autowired
  private AnimalService animalService;

  private static final Logger logger = LoggerFactory.getLogger(AnimalLifecycleController.class);

  @RequestMapping(value="/buy/{animalId}/{sequenceId:[\\d]+}", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult buy(@PathVariable("animalId") String animalId,
                    @PathVariable("sequenceId") Long sequenceId)
      throws ZooException, AggregateLoadException, NotIdempotentException {
    Buy buy = new Buy(animalId, sequenceId);
    logger.info(buy.toString());
    return new CommandResult(animalService.buy(buy));
  }

  @RequestMapping(value="/buy/{animalId}", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult buy(@PathVariable("animalId") String animalId)
      throws ZooException, AggregateLoadException, NotIdempotentException {
    Buy buy = new Buy(animalId);
    logger.info(buy.toString());
    return new CommandResult(animalService.buy(buy));
  }

  @RequestMapping(value="/sell/{animalId}/{sequenceId:[\\d]+}", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult sell(@PathVariable("animalId") String animalId,
                     @PathVariable("sequenceId") Long sequenceId)
      throws ZooException, AggregateLoadException, NotIdempotentException {
    Sell sell = new Sell(animalId, sequenceId);
    logger.info(sell.toString());
    return new CommandResult(animalService.sell(sell));
  }

  @RequestMapping(value="/feed/{animalId}/{sequenceId:[\\d]+}", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult feed(@PathVariable("animalId") String animalId,
                     @PathVariable("sequenceId") Long sequenceId)
      throws ZooException, AggregateLoadException, NotIdempotentException {
    Feed feed = new Feed(animalId, sequenceId);
    logger.info(feed.toString());
    return new CommandResult(animalService.feed(feed));
  }

  @RequestMapping(value="/digest/{animalId}/{sequenceId:[\\d]+}", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult digest(@PathVariable("animalId") String animalId,
                       @PathVariable("sequenceId") Long sequenceId)
      throws ZooException, AggregateLoadException, NotIdempotentException {
    Digest digest = new Digest(animalId, sequenceId);
    logger.info(digest.toString());
    return new CommandResult(animalService.digest(digest));
  }

  @RequestMapping(value="/play/{animalId}/{sequenceId:[\\d]+}", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult play(@PathVariable("animalId") String animalId,
                     @PathVariable("sequenceId") Long sequenceId)
      throws ZooException, AggregateLoadException, NotIdempotentException {
    Play play = new Play(animalId, sequenceId);
    logger.info(play.toString());
    return new CommandResult(animalService.play(play));
  }

  @RequestMapping(value="/sadden/{animalId}/{sequenceId:[\\d]+}", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult sadden(@PathVariable("animalId") String animalId,
                       @PathVariable("sequenceId") Long sequenceId)
      throws ZooException, AggregateLoadException, NotIdempotentException {
    Sadden sadden = new Sadden(animalId, sequenceId);
    logger.info(sadden.toString());
    return new CommandResult(animalService.sadden(sadden));
  }

  @RequestMapping(value="/cleanup/{animalId}/{sequenceId:[\\d]+}", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult cleanUp(@PathVariable("animalId") String animalId,
                        @PathVariable("sequenceId") Long sequenceId)
      throws ZooException, AggregateLoadException, NotIdempotentException {
    CleanUp cleanUp = new CleanUp(animalId, sequenceId);
    logger.info(cleanUp.toString());
    return new CommandResult(animalService.cleanUp(cleanUp));
  }

  @RequestMapping(value="/messup/{animalId}/{sequenceId:[\\d]+}", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult messup(@PathVariable("animalId") String animalId,
                       @PathVariable("sequenceId") Long sequenceId)
      throws ZooException, AggregateLoadException, NotIdempotentException {
    MessUp messup = new MessUp(animalId, sequenceId);
    logger.info(messup.toString());
    return new CommandResult(animalService.messUp(messup));
  }
}
