package zoo.controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import zoo.ZooEventLogApp;
import zoo.commands.Buy;
import zoo.persistence.EventLogEntry;
import zoo.persistence.EventLogRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by dueerkopra on 02.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZooEventLogApp.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@ActiveProfiles(profiles = "unittest")
public class AnimalAggregateLifecycleControllerTest {

  private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(),
      Charset.forName("utf8"));

  private MockMvc mockMvc;

  private HttpMessageConverter mappingJackson2HttpMessageConverter;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private EventLogRepository eventLogRepository;

  @Autowired
  void setConverters(HttpMessageConverter<?>[] converters) {

    this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
        hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

    Assert.assertNotNull("the JSON message converter must not be null",
        this.mappingJackson2HttpMessageConverter);
  }

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
    this.eventLogRepository.deleteAll();
  }

  @Test
  public void successfulBuyCommand() throws Exception {

    Date timestamp = new Date();
    Buy command = new Buy("Tiger#1", timestamp);
    mockMvc.perform(post("/buy")
        .content(this.json(command))
        .contentType(contentType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.success", is(true)));

    Collection<EventLogEntry> eventLogs =
        eventLogRepository.findByAnimalId("Tiger#1", new Sort(Sort.Direction.ASC, "occurence"));

    Assert.assertEquals(1, eventLogs.size());

    eventLogs.stream().forEach(eventLogEntry -> {
      Assert.assertEquals("Bought", eventLogEntry.getEvent());
      Assert.assertEquals("Tiger#1", eventLogEntry.getAnimalId());
      Assert.assertEquals(timestamp, eventLogEntry.getOccurence());
    });
  }

  @Test
  public void unsuccessfulBuyCommand() throws Exception {

    Date timestamp = new Date();
    eventLogRepository.save(new EventLogEntry("Bought", "Tiger#2", timestamp));

    Buy command = new Buy("Tiger#2", new Date(timestamp.getTime() + 1));
    mockMvc.perform(post("/buy")
        .content(this.json(command))
        .contentType(contentType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.success", is(false)));// Must fail because Tiger#2 is already there.

    Collection<EventLogEntry> eventLogs =
        eventLogRepository.findByAnimalId("Tiger#2", new Sort(Sort.Direction.ASC, "occurence"));

    Assert.assertEquals(1, eventLogs.size());

    eventLogs.stream().forEach(eventLogEntry -> {
      Assert.assertEquals("Bought", eventLogEntry.getEvent());
      Assert.assertEquals("Tiger#2", eventLogEntry.getAnimalId());
      Assert.assertEquals(timestamp, eventLogEntry.getOccurence());
    });
  }

  protected String json(Object o) throws IOException {
    MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
    this.mappingJackson2HttpMessageConverter.write(
        o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
    return mockHttpOutputMessage.getBodyAsString();
  }
}
