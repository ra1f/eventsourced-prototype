package zoo.aggregates;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import zoo.ZooEventLogApp;
import zoo.commands.Feed;
import zoo.exceptions.AnimalAlreadyThereException;
import zoo.exceptions.NoSuchAnimalException;
import zoo.persistence.EventLogEntry;
import zoo.persistence.EventLogRepository;
import zoo.services.AggregateLoader;
import zoo.services.AggregateRegistry;
import zoo.services.AnimalService;
import zoo.states.FeelingOfSatiety;

import java.util.Date;

/**
 * Created by dueerkopra on 10.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZooEventLogApp.class)
@ActiveProfiles(profiles = "unittest")
public class FeedAnimalTest {

  @Autowired
  private AggregateLoader aggregateLoader;

  @Autowired
  private AnimalService animalService;

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
  public void notBecomingFullerThanFull() throws Exception {

    Date oldTimestamp = new Date();
    eventLogRepository.save(new EventLogEntry("Bought", "Elephant#1", oldTimestamp));

    Animal elephant1 = aggregateLoader.replayAnimalAggregate("Elephant#1");

    Assert.assertEquals("Elephant#1", elephant1.getId());
    Assert.assertEquals(oldTimestamp, elephant1.getTimestamp());
    Assert.assertTrue(elephant1.isExisting());

    //Starts always with full
    Assert.assertEquals(FeelingOfSatiety.full, elephant1.getFeelingOfSatiety());

    Date newTimestamp = new Date(oldTimestamp.getTime() + 1);
    animalService.feed(new Feed(elephant1.getId(), newTimestamp));
    Animal snapshot = (Animal)aggregateRegistry.findSnapshot(elephant1.getId());

    Assert.assertEquals("Elephant#1", snapshot.getId());
    Assert.assertEquals(newTimestamp, snapshot.getTimestamp());
    Assert.assertTrue(snapshot.isExisting());

    //QED: Animal still full (not fuller than full)
    Assert.assertEquals(FeelingOfSatiety.full, snapshot.getFeelingOfSatiety());
  }

  @Test
  public void becomingFullAgain() throws Exception {

    Date firstTimestamp = new Date();
    Date secondTimestamp = new Date(firstTimestamp.getTime() + 1);
    eventLogRepository.save(new EventLogEntry("Bought", "Elephant#1", firstTimestamp));
    eventLogRepository.save(new EventLogEntry("Digested", "Elephant#1", secondTimestamp));

    Animal elephant1 = aggregateLoader.replayAnimalAggregate("Elephant#1");

    Assert.assertEquals("Elephant#1", elephant1.getId());
    Assert.assertEquals(secondTimestamp, elephant1.getTimestamp());
    Assert.assertTrue(elephant1.isExisting());

    //Animal is hungry because of digestion
    Assert.assertEquals(FeelingOfSatiety.hungry, elephant1.getFeelingOfSatiety());

    Date thirdTimestamp = new Date(firstTimestamp.getTime() + 1);
    animalService.feed(new Feed(elephant1.getId(), thirdTimestamp));
    Animal snapshot = (Animal)aggregateRegistry.findSnapshot(elephant1.getId());

    Assert.assertEquals("Elephant#1", snapshot.getId());
    Assert.assertEquals(thirdTimestamp, snapshot.getTimestamp());
    Assert.assertTrue(snapshot.isExisting());

    //QED: Animal full again because it was being fed
    Assert.assertEquals(FeelingOfSatiety.full, snapshot.getFeelingOfSatiety());
  }

  @Test
  public void noFeedingAfterDeath() throws Exception {

    Date firstTimestamp = new Date();
    Date secondTimestamp = new Date(firstTimestamp.getTime() + 3);
    eventLogRepository.save(new EventLogEntry("Bought", "Elephant#1", firstTimestamp));
    eventLogRepository.save(new EventLogEntry("Digested", "Elephant#1", new Date(firstTimestamp.getTime() + 1)));
    eventLogRepository.save(new EventLogEntry("Digested", "Elephant#1", new Date(firstTimestamp.getTime() + 2)));
    eventLogRepository.save(new EventLogEntry("Died", "Elephant#1", secondTimestamp));

    Animal elephant1 = aggregateLoader.replayAnimalAggregate("Elephant#1");

    Assert.assertEquals("Elephant#1", elephant1.getId());
    Assert.assertEquals(secondTimestamp, elephant1.getTimestamp());
    Assert.assertEquals(FeelingOfSatiety.starving, elephant1.getFeelingOfSatiety());

    //Animal is dead because it digested too much
    Assert.assertFalse(elephant1.isExisting());

    //Trying to feed a dead animal
    try {
      animalService.feed(new Feed(elephant1.getId(), new Date(secondTimestamp.getTime() + 1)));
      Assert.fail(String.format("Expected exception: %s", AnimalAlreadyThereException.class));
    } catch (NoSuchAnimalException e) {
      // QED: Animal does not exist anymore
      Assert.assertEquals(e.getMessage(), elephant1.getId());
    }
  }

  @Test
  public void becomingHungryAfterStarving() throws Exception {

    Date firstTimestamp = new Date();
    Date secondTimestamp = new Date(firstTimestamp.getTime() + 2);
    eventLogRepository.save(new EventLogEntry("Bought", "Leopard#1", firstTimestamp));
    eventLogRepository.save(new EventLogEntry("Digested", "Leopard#1", new Date(firstTimestamp.getTime() + 1)));
    eventLogRepository.save(new EventLogEntry("Digested", "Leopard#1", secondTimestamp));

    Animal leopard1 = aggregateLoader.replayAnimalAggregate("Leopard#1");

    Assert.assertEquals("Leopard#1", leopard1.getId());
    Assert.assertEquals(secondTimestamp, leopard1.getTimestamp());
    Assert.assertTrue(leopard1.isExisting());

    //Animal is dead because it digested too much
    Assert.assertEquals(FeelingOfSatiety.starving, leopard1.getFeelingOfSatiety());

    //Feed the leopard
    Date thirdTimestamp = new Date(secondTimestamp.getTime() + 1);
    animalService.feed(new Feed(leopard1.getId(), thirdTimestamp));

    Animal snapshot = (Animal)aggregateRegistry.findSnapshot(leopard1.getId());

    Assert.assertEquals("Leopard#1", snapshot.getId());
    Assert.assertEquals(thirdTimestamp, snapshot.getTimestamp());
    Assert.assertTrue(snapshot.isExisting());

    //QED: Animal still hungry again it was being fed when it was starving before.
    Assert.assertEquals(FeelingOfSatiety.hungry, snapshot.getFeelingOfSatiety());

  }

}
