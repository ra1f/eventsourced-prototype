package zoo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zoo.commands.*;
import zoo.services.AnimalService;

/**
 * Created by dueerkopra on 30.03.2015.
 */
@RestController
public class AnimalLifecycleController {

  @Autowired
  private AnimalService animalService;

  private static final Logger logger = LoggerFactory.getLogger(AnimalLifecycleController.class);

  @RequestMapping(value = "/buy", method=RequestMethod.PUT)
  public @ResponseBody CommandResult buy(@RequestBody Buy buy) {

    logger.info(buy.toString());
    CommandResult result = new CommandResult();
    try {
      result.setSequenceId(animalService.buy(buy));
      result.setSuccess(true);
    } catch (Exception e) {
      logger.error("Error in /buy", e);
    }
    return result;
  }

  @RequestMapping(value = "/sell", method=RequestMethod.PUT)
  public @ResponseBody CommandResult sell(@RequestBody Sell sell) {

    logger.info(sell.toString());
    CommandResult result = new CommandResult();
    try {
      result.setSequenceId(animalService.sell(sell));
      result.setSuccess(true);
    } catch (Exception e) {
      logger.error("Error in /sell", e);
    }
    return result;
  }

  @RequestMapping(value = "/feed", method=RequestMethod.PUT)
  public @ResponseBody CommandResult feed(@RequestBody Feed feed) {

    logger.info(feed.toString());
    CommandResult result = new CommandResult();
    try {
      result.setSequenceId(animalService.feed(feed));
      result.setSuccess(true);
    } catch (Exception e) {
      logger.error("Error in /feed", e);
    }
    return result;
  }

  @RequestMapping(value = "/digest", method=RequestMethod.PUT)
  public @ResponseBody CommandResult digest(@RequestBody Digest digest) {

    logger.info(digest.toString());
    CommandResult result = new CommandResult();
    try {
      result.setSequenceId(animalService.digest(digest));
      result.setSuccess(true);
    } catch (Exception e) {
      logger.error("Error in /digest", e);
    }
    return result;
  }

  @RequestMapping(value = "/play", method=RequestMethod.PUT)
  public @ResponseBody CommandResult play(@RequestBody Play play) {

    logger.info(play.toString());
    CommandResult result = new CommandResult();
    try {
      result.setSequenceId(animalService.play(play));
      result.setSuccess(true);
    } catch (Exception e) {
      logger.error("Error in /play", e);
    }
    return result;
  }

  @RequestMapping(value = "/sadden", method=RequestMethod.PUT)
  public @ResponseBody CommandResult sadden(@RequestBody Sadden sadden) {

    logger.info(sadden.toString());
    CommandResult result = new CommandResult();
    try {
      result.setSequenceId(animalService.sadden(sadden));
      result.setSuccess(true);
    } catch (Exception e) {
      logger.error("Error in /sadden", e);
    }
    return result;
  }

}
