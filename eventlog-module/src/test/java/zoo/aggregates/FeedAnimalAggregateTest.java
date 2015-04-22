package zoo.aggregates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zoo.ZooEventLogApp;
import zoo.commands.Feed;
import zoo.exceptions.NoSuchAnimalException;
import zoo.persistence.EventLogEntry;
import zoo.persistence.EventLogRepository;
import zoo.services.AggregateLoader;
import zoo.services.AnimalService;
import zoo.services.EventStore;
import zoo.states.FeelingOfSatiety;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by dueerkopra on 10.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZooEventLogApp.class)
@ActiveProfiles(profiles = "unittest")
public class FeedAnimalAggregateTest {

  @Autowired
  private AnimalService animalService;

  @Autowired
  private EventLogRepository eventLogRepository;

  @Autowired
  private EventStore eventStore;

  @Before
  public void setup() throws Exception {
    eventLogRepository.deleteAll();
  }

  @Test
  public void notBecomingFullerThanFull() throws Exception {

    eventLogRepository.save(new EventLogEntry("Elephant#1", 0L, "Bought", new Date()));

    AnimalAggregate elephant1 = AggregateLoader.replayFromOrigin("Elephant#1", 1L, eventStore);

    assertEquals("Elephant#1", elephant1.getId());
    assertTrue(elephant1.isExisting());

    //Starts always with full
    assertEquals(FeelingOfSatiety.full, elephant1.getFeelingOfSatiety());

    animalService.feed(new Feed(elephant1.getId(), 1L));
    AnimalAggregate newState = AggregateLoader.replayFromOrigin(elephant1.getId(), 2L, eventStore);

    assertEquals("Elephant#1", newState.getId());
    assertTrue(newState.isExisting());

    //QED: Animal still full (not fuller than full)
    assertEquals(FeelingOfSatiety.full, newState.getFeelingOfSatiety());
  }

  @Test
  public void becomingFullAgain() throws Exception {

    eventLogRepository.save(new EventLogEntry("Elephant#1", 0L, "Bought", new Date()));
    eventLogRepository.save(new EventLogEntry("Elephant#1", 1L, "Digested", new Date()));

    AnimalAggregate elephant1 = AggregateLoader.replayFromOrigin("Elephant#1", 2L, eventStore);

    assertEquals("Elephant#1", elephant1.getId());
    assertTrue(elephant1.isExisting());

    //Animal is hungry because of digestion
    assertEquals(FeelingOfSatiety.hungry, elephant1.getFeelingOfSatiety());

    animalService.feed(new Feed(elephant1.getId(), 2L));
    AnimalAggregate newState = AggregateLoader.replayFromOrigin(elephant1.getId(), 3L, eventStore);

    assertEquals("Elephant#1", newState.getId());
    assertTrue(newState.isExisting());

    //QED: Animal full again because it was being fed
    assertEquals(FeelingOfSatiety.full, newState.getFeelingOfSatiety());
  }

  @Test
  public void noFeedingAfterDeath() throws Exception {

    eventLogRepository.save(new EventLogEntry("Elephant#1", 0L, "Bought", new Date()));
    eventLogRepository.save(new EventLogEntry("Elephant#1", 1L, "Digested", new Date()));
    eventLogRepository.save(new EventLogEntry("Elephant#1", 2L, "Digested", new Date()));
    eventLogRepository.save(new EventLogEntry("Elephant#1", 3L, "Died", new Date()));

    AnimalAggregate elephant1 = AggregateLoader.replayFromOrigin("Elephant#1", 4L, eventStore);

    assertEquals("Elephant#1", elephant1.getId());
    assertEquals(new Long(3L), elephant1.getSequenceId());
    assertEquals(FeelingOfSatiety.starving, elephant1.getFeelingOfSatiety());

    //Animal is dead because it digested too much
    assertFalse(elephant1.isExisting());

    //Trying to feed a dead animal
    Feed feed = new Feed(elephant1.getId(), 4L);
    try {
      animalService.feed(feed);
      fail(String.format("Expected exception: %s", NoSuchAnimalException.class));
    } catch (NoSuchAnimalException e) {
      // QED: Animal does not exist anymore
      assertEquals(e.getMessage(), feed.toString());
    }
  }

  @Test
  public void becomingHungryAfterStarving() throws Exception {

    eventLogRepository.save(new EventLogEntry("Leopard#1", 0L, "Bought", new Date()));
    eventLogRepository.save(new EventLogEntry("Leopard#1", 1L, "Digested", new Date()));
    eventLogRepository.save(new EventLogEntry("Leopard#1", 2L, "Digested", new Date()));

    AnimalAggregate leopard1 = AggregateLoader.replayFromOrigin("Leopard#1", 4L, eventStore);

    assertEquals("Leopard#1", leopard1.getId());
    assertEquals(new Long(2L), leopard1.getSequenceId());
    assertTrue(leopard1.isExisting());

    //Animal is dead because it digested too much
    assertEquals(FeelingOfSatiety.starving, leopard1.getFeelingOfSatiety());

    //Feed the leopard
    animalService.feed(new Feed(leopard1.getId(), 3L));

    AnimalAggregate newState = AggregateLoader.replayFromOrigin(leopard1.getId(), 5L, eventStore);

    assertEquals("Leopard#1", newState.getId());
    assertEquals(new Long(3L), newState.getSequenceId());
    assertTrue(newState.isExisting());

    //QED: Animal still hungry again it was being fed when it was starving before.
    assertEquals(FeelingOfSatiety.hungry, newState.getFeelingOfSatiety());

  }

}
