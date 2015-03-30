package controllers;

import models.CommandDto;
import models.CommandResultDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dueerkopra on 30.03.2015.
 */
@RestController
@RequestMapping("/commands")
public class CommandController {

  @RequestMapping(method= RequestMethod.POST)
  public CommandResultDto create(@RequestBody List<CommandDto> commands) {
    return new CommandResultDto();
  }
}
