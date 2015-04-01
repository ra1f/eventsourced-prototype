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
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Created by dueerkopra on 27.03.2015.
 */
@Service
public class CommandHandler {

  @Autowired
  private ZooRepository zooRepository;

  /**
   *
   * @param commands
   * @return
   */
  public Observable<Zoo> processCommands(List<CommandDto> commands) {

    // Make many lists out of one list grouping by AnimalId
    Map<AnimalId, List<CommandDto>> commandsPerAnimalId = commands.stream()
        .collect(Collectors.groupingBy(commandDto -> commandDto.getAnimalId()));

    // Reduce a list of commands by applying it to a Zoo entity
    BiFunction<Zoo, ? super CommandDto, Zoo> reduceFunction = (zoo, commandDto) -> applyCommand(commandDto, zoo);

    return Observable.create(subscriber -> {
          try {
            commandsPerAnimalId.forEach((id, cs) -> {
              subscriber.onNext(zooRepository.save(cs.stream().reduce(getState(id), reduceFunction, (z1, z2) -> z2)));
            });
          } catch (Exception e) {
            subscriber.onError(e);
          } finally {
            subscriber.onCompleted();
          }
        }
    );
  }

  /**
   * Retrieves a Zoo entity as a snapshot from database or creates a new default if there is none for the animalId.
   * @param animalId
   * @return
   */
  private Zoo getState(AnimalId animalId) {
    Zoo zoo = zooRepository.findOne(animalId);
    if (zoo == null) zoo = new Zoo(animalId, FeelingOfSatiety.full, Mindstate.happy, Hygiene.tidy);
    return zoo;
  }

  /**
   * Applies a command to a Zoo entity and thereby changes its state.
   * @param commandDto
   * @param zoo
   * @return The new Zoo entity
   */
  private Zoo applyCommand(CommandDto commandDto, Zoo zoo) {
    Zoo result = new Zoo(zoo.getAnimalId(),
        zoo.getLastOccurence(),
        zoo.getFeelingOfSatiety(),
        zoo.getMindstate(),
        zoo.getHygiene(),
        zoo.getVersion());
    Date oldTs = zoo.getLastOccurence();
    Date newTs = commandDto.getTimestamp();
    // The condition must hold that the successor timestamp is later otherwise an exception is thrown.
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
    return result;
  }
}
