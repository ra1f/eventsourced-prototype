package services;

import dao.ZooDao;
import models.CommandDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dueerkopra on 27.03.2015.
 */
@Service
public class CommandHandler {

  @Autowired
  private ZooDao zooDao;

  public void processCommand(List<CommandDto> commands) {



  }
}
