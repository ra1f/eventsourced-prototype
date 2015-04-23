package zoo.controllers;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import zoo.ZooEventLogApp;
import zoo.persistence.EventLogEntry;
import zoo.persistence.EventLogRepository;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    assertNotNull("the JSON message converter must not be null",
        this.mappingJackson2HttpMessageConverter);
  }

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
    this.eventLogRepository.deleteAll();
  }

  @Test
  public void successfulBuyCommand() throws Exception {

    mockMvc.perform(put("/buy/Tiger")
        .contentType(contentType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.sequenceId", is(0)));

    Collection<EventLogEntry> eventLogs =
        eventLogRepository.findByIdAndSequenceIdLessThan("Tiger", 1L,
            new Sort(Sort.Direction.ASC, "occurence"));

    assertEquals(1, eventLogs.size());

    eventLogs.stream().forEach(eventLogEntry -> {
      assertEquals("Bought", eventLogEntry.getEvent());
      assertEquals("Tiger", eventLogEntry.getId());
      assertEquals(new Long(0L), eventLogEntry.getSequenceId());
    });
  }

  @Test
  public void unsuccessfulBuyCommand() throws Exception {

    Date timestamp = new Date();
    eventLogRepository.save(new EventLogEntry("Lion", 0L, "Bought", timestamp));

    mockMvc.perform(put("/buy/Lion/1")
        .contentType(contentType))
        .andExpect(status().isBadRequest());// Must fail because Tiger#2 is already there.

    Collection<EventLogEntry> eventLogs =
        eventLogRepository.findByIdAndSequenceIdLessThan("Lion", 1L,
            new Sort(Sort.Direction.ASC, "occurence"));

    assertEquals(1, eventLogs.size());

    eventLogs.stream().forEach(eventLogEntry -> {
      assertEquals("Bought", eventLogEntry.getEvent());
      assertEquals("Lion", eventLogEntry.getId());
      assertEquals(new Long(0L), eventLogEntry.getSequenceId());
      assertEquals(timestamp, eventLogEntry.getOccurence());
    });
  }

}
