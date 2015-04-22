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

  @RequestMapping(value = "/buy", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult buy(@RequestBody Buy buy) throws ZooException, AggregateLoadException, NotIdempotentException {

    logger.info(buy.toString());
    return new CommandResult(animalService.buy(buy));
  }

  @RequestMapping(value = "/sell", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult sell(@RequestBody Sell sell) throws ZooException, AggregateLoadException, NotIdempotentException {

    logger.info(sell.toString());
    return new CommandResult((animalService.sell(sell)));
  }

  @RequestMapping(value = "/feed", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult feed(@RequestBody Feed feed) throws ZooException, AggregateLoadException, NotIdempotentException {

    logger.info(feed.toString());
    return new CommandResult(animalService.feed(feed));
  }

  @RequestMapping(value = "/digest", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult digest(@RequestBody Digest digest) throws ZooException, AggregateLoadException, NotIdempotentException {

    logger.info(digest.toString());
    return new CommandResult(animalService.digest(digest));
  }

  @RequestMapping(value = "/play", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult play(@RequestBody Play play) throws ZooException, AggregateLoadException, NotIdempotentException {

    logger.info(play.toString());
    return new CommandResult(animalService.play(play));
  }

  @RequestMapping(value = "/sadden", method = RequestMethod.PUT)
  public
  @ResponseBody
  CommandResult sadden(@RequestBody Sadden sadden) throws ZooException, AggregateLoadException, NotIdempotentException {

    logger.info(sadden.toString());
    return new CommandResult(animalService.sadden(sadden));
  }

}
