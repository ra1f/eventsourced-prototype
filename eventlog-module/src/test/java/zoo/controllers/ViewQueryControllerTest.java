package zoo.controllers;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import zoo.ZooEventLogApp;
import zoo.commands.Buy;
import zoo.commands.Digest;
import zoo.commands.Sadden;
import zoo.persistence.AnimalRepository;
import zoo.persistence.EventLogRepository;
import zoo.services.AnimalService;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by dueerkopra on 14.04.2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ZooEventLogApp.class)
@WebAppConfiguration
@IntegrationTest("server.port:0")
@ActiveProfiles(profiles = "unittest")
public class ViewQueryControllerTest {

  protected MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
      MediaType.APPLICATION_JSON.getSubtype(),
      Charset.forName("utf8"));

  private HttpMessageConverter mappingJackson2HttpMessageConverter;

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private AnimalService animalService;

  @Autowired
  private void setConverters(HttpMessageConverter<?>[] converters) {

    this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream().filter(
        hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().get();

    assertNotNull("the JSON message converter must not be null",
        this.mappingJackson2HttpMessageConverter);
  }

  @Autowired
  private AnimalRepository animalRepository;

  @Autowired
  private EventLogRepository eventLogRepository;

  private String json(Object o) throws IOException {
    MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
    this.mappingJackson2HttpMessageConverter.write(
        o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
    return mockHttpOutputMessage.getBodyAsString();
  }

  @Before
  public void setup() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
    animalRepository.deleteAll();
    eventLogRepository.deleteAll();
  }

  @Test
  public void notFoundTheNoneExisting() throws Exception {

    mockMvc.perform(get("/animals/Bear1")
        .accept(contentType))
        .andExpect(status().isNotFound());
  }

  @Test
  public void foundAfterBuying() throws Exception {

    Buy buy = new Buy("Giraffe1");
    Long sequenceId = animalService.buy(buy);

    Thread.sleep(1000);

    mockMvc.perform(get("/animals/Giraffe1")
        .accept(contentType))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sequenceId", is(sequenceId.intValue())))
        .andExpect(jsonPath("$.animalId", is("Giraffe1")))
        .andExpect(jsonPath("$.feelingOfSatiety", is("full")))
        .andExpect(jsonPath("$.mindstate", is("happy")))
        .andExpect(jsonPath("$.hygiene", is("tidy")));
  }

  @Test
  public void hungryAfterDigesting() throws Exception {

    Buy buy = new Buy("Crocodile");
    Long sequenceId = animalService.buy(buy);
    sequenceId = animalService.digest(new Digest(buy.getAnimalId(), sequenceId + 1));

    Thread.sleep(1000);

    mockMvc.perform(get("/animals/Crocodile")
        .accept(contentType))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sequenceId", is(sequenceId.intValue())))
        .andExpect(jsonPath("$.animalId", is("Crocodile")))
        .andExpect(jsonPath("$.feelingOfSatiety", is("hungry")))
        .andExpect(jsonPath("$.mindstate", is("happy")))
        .andExpect(jsonPath("$.hygiene", is("tidy")));
  }

  @Test
  public void boredToDeath() throws Exception {

    // Let's buy a kangaroo
    Buy buy = new Buy("Kangaroo");
    mockMvc.perform(put("/buy")
        .content(this.json(buy))
        .contentType(contentType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.sequenceId", is(0)));

    Thread.sleep(1000);

    // What did we buy
    mockMvc.perform(get("/animals/Kangaroo")
        .accept(contentType))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sequenceId", is(0)))
        .andExpect(jsonPath("$.animalId", is("Kangaroo")))
        .andExpect(jsonPath("$.feelingOfSatiety", is("full")))
        .andExpect(jsonPath("$.mindstate", is("happy")))
        .andExpect(jsonPath("$.hygiene", is("tidy")));

    // The kangaroo is saddening
    Sadden sadden = new Sadden("Kangaroo", 1L);
    mockMvc.perform(put("/sadden")
        .content(this.json(sadden))
        .contentType(contentType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.sequenceId", is(1)));

    Thread.sleep(1000);

    // Kangaroo should be moody now?
    mockMvc.perform(get("/animals/Kangaroo")
        .accept(contentType))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sequenceId", is(1)))
        .andExpect(jsonPath("$.animalId", is("Kangaroo")))
        .andExpect(jsonPath("$.feelingOfSatiety", is("full")))
        .andExpect(jsonPath("$.mindstate", is("moody")))
        .andExpect(jsonPath("$.hygiene", is("tidy")));

    // The kangaroo is even more saddening
    sadden = new Sadden("Kangaroo", 2L);
    mockMvc.perform(put("/sadden")
        .content(this.json(sadden))
        .contentType(contentType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.sequenceId", is(2)));

    Thread.sleep(1000);

    // Kangaroo should be boredOut now?
    mockMvc.perform(get("/animals/Kangaroo")
        .accept(contentType))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sequenceId", is(2)))
        .andExpect(jsonPath("$.animalId", is("Kangaroo")))
        .andExpect(jsonPath("$.feelingOfSatiety", is("full")))
        .andExpect(jsonPath("$.mindstate", is("boredOut")))
        .andExpect(jsonPath("$.hygiene", is("tidy")));

    // The kangaroo is even more saddening
    sadden = new Sadden("Kangaroo", 3L);
    mockMvc.perform(put("/sadden")
        .content(this.json(sadden))
        .contentType(contentType))
        .andExpect(status().isOk())
        .andExpect(content().contentType(contentType))
        .andExpect(jsonPath("$.success", is(true)))
        .andExpect(jsonPath("$.sequenceId", is(3)));

    Thread.sleep(1000);

    // The kangaroo passed away
    mockMvc.perform(get("/animals/Kangaroo")
        .accept(contentType))
        .andExpect(status().isNotFound());
  }


}
