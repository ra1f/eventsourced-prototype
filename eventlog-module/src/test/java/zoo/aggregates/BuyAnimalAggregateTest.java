package zoo.aggregates;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zoo.ZooEventLogApp;
import zoo.commands.Buy;
import zoo.dto.Events;
import zoo.events.Event;
import zoo.exceptions.AnimalAlreadyThereException;
import zoo.persistence.EventLogEntry;
import zoo.persistence.EventLogRepository;
import zoo.services.EventStore;
import zoo.states.FeelingOfSatiety;

import java.util.Collection;
import java.util.Date;

import static zoo.services.AggregateLoader.replayFromOrigin;

/**
 * Created by dueerkopra on 09.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZooEventLogApp.class)
@ActiveProfiles(profiles = "unittest")
public class BuyAnimalAggregateTest {

  @Autowired
  private EventStore eventStore;

  @Autowired
  private EventLogRepository eventLogRepository;

  @Before
  public void setup() throws Exception {
    eventLogRepository.deleteAll();
  }

  @Test
  public void justBuy() throws Exception {

    Buy buy = new Buy("Lion#1", 1L);

    AnimalAggregate animalAggregate = replayFromOrigin(buy.getAnimalId(), eventStore);

    Assert.assertNull(animalAggregate.getId());
    Assert.assertNull(animalAggregate.getTimestamp());
    Assert.assertFalse(animalAggregate.isExisting());
    Assert.assertNull(animalAggregate.getFeelingOfSatiety());

    Collection<Event> events = animalAggregate.asBuyCommandHandler().handleCommand(buy);
    eventStore.save(new Events(buy.getAnimalId(), events));

    AnimalAggregate newState = replayFromOrigin("Lion#1", eventStore);

    Assert.assertEquals("Lion#1", newState.getId());
    Assert.assertEquals(1L, newState.getSequenceId().longValue());
    Assert.assertTrue(newState.isExisting());
    Assert.assertEquals(FeelingOfSatiety.full, newState.getFeelingOfSatiety());

    Collection<EventLogEntry> eventLogs =
        eventLogRepository.findById("Lion#1", new Sort(Sort.Direction.ASC, "occurence"));

    Assert.assertEquals(1, eventLogs.size());

    eventLogs.stream().forEach(eventLogEntry -> {
      Assert.assertEquals("Bought", eventLogEntry.getEvent());
      Assert.assertEquals("Lion#1", eventLogEntry.getId());
      Assert.assertEquals(1L, newState.getSequenceId().longValue());
    });
  }

  @Test
  public void cannotBeBoughtAfterAlreadyBeeingBought() throws Exception {

    Date oldDate = new Date();
    eventLogRepository.save(new EventLogEntry("Elephant#1", 1L, 1L, "Bought", oldDate));

    AnimalAggregate animalAggregate = replayFromOrigin("Elephant#1", eventStore);

    try {
      Buy buy = new Buy("Elephant#1", 2L);
      animalAggregate.asBuyCommandHandler().handleCommand(buy);
      Assert.fail(String.format("Expected exception: %s", AnimalAlreadyThereException.class));
    } catch (AnimalAlreadyThereException e) {
      Assert.assertEquals(e.getMessage(), "Elephant#1");
    }
  }
}
