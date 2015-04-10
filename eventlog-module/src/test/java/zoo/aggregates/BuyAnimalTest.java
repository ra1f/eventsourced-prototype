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
import zoo.events.Event;
import zoo.exceptions.AnimalAlreadyThereException;
import zoo.persistence.EventLogEntry;
import zoo.persistence.EventLogRepository;
import zoo.services.AggregateLoader;
import zoo.services.AggregateRegistry;
import zoo.services.EventPublisher;
import zoo.states.FeelingOfSatiety;

import java.util.Collection;
import java.util.Date;

/**
 * Created by dueerkopra on 09.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZooEventLogApp.class)
@ActiveProfiles(profiles = "unittest")
public class BuyAnimalTest {

  @Autowired
  private AggregateLoader aggregateLoader;

  @Autowired
  private EventPublisher publisher;

  @Autowired
  private AggregateRegistry aggregateRegistry;

  @Autowired
  private EventLogRepository eventLogRepository;

  @Before
  public void setup() throws Exception {
    eventLogRepository.deleteAll();
    aggregateRegistry.deleteAll();
  }

  @Test
  public void justBuy() throws Exception {

    Date date = new Date();
    Buy buy = new Buy("Lion#1", date);

    Assert.assertNull(aggregateRegistry.findSnapshot("Lion#1"));

    Animal animal = aggregateLoader.replayAnimalAggregate(buy.getAnimalId());

    Assert.assertNull(animal.getId());
    Assert.assertNull(animal.getTimestamp());
    Assert.assertFalse(animal.isExisting());
    Assert.assertNull(animal.getFeelingOfSatiety());

    Collection<Event> events = animal.asBuyCommandHandler().handleCommand(buy);
    publisher.publish(events, animal);

    Animal snapshot = (Animal)aggregateRegistry.findSnapshot("Lion#1");

    Assert.assertEquals("Lion#1", snapshot.getId());
    Assert.assertEquals(date, snapshot.getTimestamp());
    Assert.assertTrue(snapshot.isExisting());
    Assert.assertEquals(FeelingOfSatiety.full, snapshot.getFeelingOfSatiety());

    Collection<EventLogEntry> eventLogs =
        eventLogRepository.findByAnimalId("Lion#1", new Sort(Sort.Direction.ASC, "occurence"));

    Assert.assertEquals(1, eventLogs.size());

    eventLogs.stream().forEach(eventLogEntry -> {
      Assert.assertEquals("Bought", eventLogEntry.getEvent());
      Assert.assertEquals("Lion#1", eventLogEntry.getAnimalId());
      Assert.assertEquals(date, eventLogEntry.getOccurence());
    });
  }

  @Test
  public void cannotBeBoughtAfterAlreadyBeeingBought() throws Exception {

    Date oldDate = new Date();
    eventLogRepository.save(new EventLogEntry("Bought", "Elephant#1", oldDate));

    Animal animal = aggregateLoader.replayAnimalAggregate("Elephant#1");

    try {
      Buy buy = new Buy("Elephant#1", new Date(oldDate.getTime() + 1));
      animal.asBuyCommandHandler().handleCommand(buy);
      Assert.fail(String.format("Expected exception: %s", AnimalAlreadyThereException.class));
    } catch (AnimalAlreadyThereException e) {
      Assert.assertEquals(e.getMessage(), "Elephant#1");
    }
  }
}
