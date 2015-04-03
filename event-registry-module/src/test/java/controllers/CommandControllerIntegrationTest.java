package controllers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import zoo.ZooApp;
import zoo.common.*;
import zoo.models.CommandDto;
import zoo.persistence.EventLog;
import zoo.persistence.EventLogRepository;
import zoo.persistence.Zoo;
import zoo.persistence.ZooRepository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by dueerkopra on 02.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZooApp.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
public class CommandControllerIntegrationTest {

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
  private ZooRepository zooRepository;

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
    this.zooRepository.deleteAll();
  }

  @Test
  public void successfulSingleCommand() throws Exception {

    Date timestamp = new Date();
    CommandDto command = new CommandDto(Command.Play, AnimalId.Elephants, timestamp);
    mockMvc.perform(post("/commands/")
        .content(this.json(Arrays.asList(command)))
        .contentType(contentType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.success", is(true)));

    EventLog eventLog = this.eventLogRepository.findByOccurence(timestamp);
    assertEquals(Event.Played, eventLog.getEvent());
    assertEquals(AnimalId.Elephants, eventLog.getAnimalId());

    Zoo zoo = this.zooRepository.findOne(AnimalId.Elephants);
    assertEquals(FeelingOfSatiety.full, zoo.getFeelingOfSatiety());
    assertEquals(Hygiene.tidy, zoo.getHygiene());
    assertEquals(Mindstate.happy, zoo.getMindstate());
  }

  @Test
  public void successfulCommandSequence() throws Exception {

    mockMvc.perform(post("/commands/")
          .content(this.json(Arrays.asList(
            new CommandDto(Command.Play, AnimalId.Monkeys, new Date()),
            new CommandDto(Command.Sadden, AnimalId.Monkeys, new Date()),
            new CommandDto(Command.Sadden, AnimalId.Monkeys, new Date()
            ))))
          .contentType(contentType))
          .andExpect(status().isOk())
          .andExpect(content().contentType(contentType))
          .andExpect(jsonPath("$.success", is(true)));
  }

  protected String json(Object o) throws IOException {
    MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
    this.mappingJackson2HttpMessageConverter.write(
        o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
    return mockHttpOutputMessage.getBodyAsString();
  }
}
