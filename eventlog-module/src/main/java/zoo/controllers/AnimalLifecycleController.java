package zoo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import zoo.commands.Buy;
import zoo.commands.Feed;
import zoo.services.AnimalService;

/**
 * Created by dueerkopra on 30.03.2015.
 */
@RestController
public class AnimalLifecycleController {

  @Autowired
  private AnimalService animalService;

  private static final Logger logger = LoggerFactory.getLogger(AnimalLifecycleController.class);

  @RequestMapping(value = "/buy", method=RequestMethod.POST)
  public @ResponseBody CommandResult create(@RequestBody Buy buy) {

    logger.info(buy.toString());
    CommandResult result = new CommandResult();
    try {
      animalService.buy(buy);
      result.setSuccess(true);
    } catch (Exception e) {
      logger.error("Error in /buy", e);
    }
    return result;
  }

  @RequestMapping(value = "/feed", method=RequestMethod.POST)
  public @ResponseBody CommandResult create(@RequestBody Feed feed) {

    logger.info(feed.toString());
    CommandResult result = new CommandResult();
    try {
      animalService.feed(feed);
      result.setSuccess(true);
    } catch (Exception e) {
      logger.error("Error in /feed", e);
    }
    return result;
  }
}
