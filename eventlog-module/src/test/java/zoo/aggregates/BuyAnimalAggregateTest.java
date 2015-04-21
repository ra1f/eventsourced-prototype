package zoo.aggregates;

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

import static org.junit.Assert.*;
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

    AnimalAggregate animalAggregate = replayFromOrigin("Lion#1", eventStore);

    assertNull(animalAggregate.getId());
    assertNull(animalAggregate.getTimestamp());
    assertFalse(animalAggregate.isExisting());
    assertNull(animalAggregate.getFeelingOfSatiety());

    Buy buy = new Buy("Lion#1");
    Events<Event> events = animalAggregate.asBuyCommandHandler().handleCommand(buy);
    eventStore.save(new Events(buy.getAnimalId(), events.getEvents(), buy.getSequenceId()));

    AnimalAggregate newState = replayFromOrigin("Lion#1", eventStore);

    assertEquals("Lion#1", newState.getId());
    assertEquals(0L, newState.getSequenceId().longValue());
    assertTrue(newState.isExisting());
    assertEquals(FeelingOfSatiety.full, newState.getFeelingOfSatiety());

    Collection<EventLogEntry> eventLogs =
        eventLogRepository.findById("Lion#1", new Sort(Sort.Direction.ASC, "occurence"));

    assertEquals(1, eventLogs.size());

    eventLogs.stream().forEach(eventLogEntry -> {
      assertEquals("Bought", eventLogEntry.getEvent());
      assertEquals("Lion#1", eventLogEntry.getId());
      assertEquals(0L, newState.getSequenceId().longValue());
    });
  }

  @Test
  public void cannotBeBoughtAfterAlreadyBeeingBought() throws Exception {

    eventLogRepository.save(new EventLogEntry("Elephant#1", "Bought", 1L, new Date()));

    AnimalAggregate animalAggregate = replayFromOrigin("Elephant#1", eventStore);

    Buy buy = new Buy("Elephant#1", 2L);
    try {
      animalAggregate.asBuyCommandHandler().handleCommand(buy);
      fail(String.format("Expected exception: %s", AnimalAlreadyThereException.class));
    } catch (AnimalAlreadyThereException e) {
      assertEquals(e.getMessage(), buy.toString());
    }
  }
}
