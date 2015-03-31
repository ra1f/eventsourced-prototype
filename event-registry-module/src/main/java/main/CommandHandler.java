package main;

import common.AnimalId;
import common.Command;
import models.CommandDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;

import java.util.List;

/**
 * Created by dueerkopra on 27.03.2015.
 */
@Service
public class CommandHandler {

  @Autowired
  private ZooRepository zooRepository;

  public Observable<Zoo> processCommands(List<CommandDto> commands) {

    return Observable.create(subscriber -> {
          try {
            for (CommandDto command : commands) {
              subscriber.onNext(newState(command, getState(command.getAnimalId())));
            }

          } catch (Exception e) {
            subscriber.onError(e);
          } finally {
            subscriber.onCompleted();
          }
        }
    );
  }

  private Zoo getState(AnimalId animalId) {
    return zooRepository.findOne(animalId);
  }

  private Zoo newState(CommandDto commandDto, Zoo zoo) {
    zoo.setLastOccurence(commandDto.getTimestamp());
    Command command = commandDto.getCommand();
    switch (command) {
      case Digest:
        zoo.setFeelingOfSatiety(zoo.getFeelingOfSatiety().next(command));
      case Feed:
        zoo.setFeelingOfSatiety(zoo.getFeelingOfSatiety().next(command));
      case Sadden:
      case Play:
      case MessUp:
      case CleanUp:
    }
    return zoo;
  }
}
