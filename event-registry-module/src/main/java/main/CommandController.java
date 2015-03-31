package main;

import common.Event;
import models.CommandDto;
import models.CommandResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dueerkopra on 30.03.2015.
 */
@RestController
@RequestMapping("/commands")
public class CommandController {

  private static final Logger logger = LoggerFactory.getLogger(CommandController.class);

  @Autowired
  private CommandHandler commandHandler;

  @Autowired
  private EventLogRepository eventLogRepository;

  @RequestMapping(method=RequestMethod.POST)
  public @ResponseBody CommandResultDto create(@RequestBody List<CommandDto> commands) {

    commands.stream().forEach(c ->
        logger.info(String.format("CommandDto[command='%s', animalId='%s', timestamp='%s']",
                                     c.getCommand(),
                                     c.getAnimalId(),
                                     new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(c.getTimestamp()))));
    CommandResultDto result = new CommandResultDto();
    commandHandler.processCommands(commands).subscribe(
      zoo -> result.setCommandsExecuted(result.getCommandsExecuted() + 1));

    eventLogRepository.save(commands.stream().map(
     command -> new EventLog(Event.fromCommand(command.getCommand()),
                             command.getAnimalId(),
                             command.getTimestamp())).collect(Collectors.toList()));

    return result;
  }
}
