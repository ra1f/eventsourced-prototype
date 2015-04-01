package zoo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rx.Observable;
import zoo.common.*;
import zoo.models.CommandDto;
import zoo.persistence.Zoo;
import zoo.persistence.ZooRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by dueerkopra on 27.03.2015.
 */
@Service
public class CommandHandler {

  @Autowired
  private ZooRepository zooRepository;

  public Observable<Zoo> processCommands(List<CommandDto> commands) {

    List <AnimalId> animalIds = commands.stream().map(command -> command.getAnimalId())
        .parallel()
        .distinct()
        .collect(Collectors.toList());

    Map<AnimalId, List<CommandDto>> parts = commands.stream()
        .collect(Collectors.groupingBy(commandDto -> commandDto.getAnimalId()));
    //parts.forEach((id, cs) -> cs.stream().sorted(c -> c.getTimestamp()));
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

  private Zoo applyCommand(CommandDto commandDto, Zoo zoo) throws InconsistencyException {
    Zoo result = new Zoo(zoo.getAnimalId(),
        zoo.getLastOccurence(),
        zoo.getFeelingOfSatiety(),
        zoo.getMindstate(),
        zoo.getHygiene(),
        zoo.getVersion());
    Date oldTs = result.getLastOccurence();
    Date newTs = commandDto.getTimestamp();
    if (oldTs == null || oldTs.compareTo(newTs) < 0) {
      result.setLastOccurence(commandDto.getTimestamp());
    } else {
      throw new InconsistencyException(String.format("New timestamp='%s' must be later than current timestamp in %s",
          new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(newTs),
          result));
    }

    Command command = commandDto.getCommand();
    switch (command) {
      case Digest:
      case Feed:
        result.setFeelingOfSatiety(result.getFeelingOfSatiety().next(command));
        break;
      case Sadden:
      case Play:
        result.setMindstate(result.getMindstate().next(command));
        break;
      case MessUp:
      case CleanUp:
        result.setHygiene(result.getHygiene().next(command));
    }
    zooRepository.save(result);
    return result;
  }
}
