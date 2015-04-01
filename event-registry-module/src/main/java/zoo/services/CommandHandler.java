package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import zoo.common.*;
import zoo.models.CommandDto;
import zoo.persistence.Zoo;
import zoo.persistence.ZooRepository;

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
              subscriber.onNext(applyCommand(command, getState(command.getAnimalId())));
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
    Zoo zoo = zooRepository.findOne(animalId);
    if (zoo == null) zoo = new Zoo(animalId, FeelingOfSatiety.full, Mindstate.happy, Hygiene.tidy);
    return zoo;
  }

  private Zoo applyCommand(CommandDto commandDto, Zoo zoo) {
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
    zooRepository.save(zoo);
    return zoo;
  }
}
