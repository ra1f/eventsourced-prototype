package zoo.controllers;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import zoo.ZooEventLogApp;
import zoo.commands.Buy;
import zoo.persistence.AnimalRepository;
import zoo.persistence.EventLogRepository;
import zoo.services.AnimalService;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

    Assert.assertNotNull("the JSON message converter must not be null",
        this.mappingJackson2HttpMessageConverter);
  }

  @Autowired
  private AnimalRepository animalRepository;

  @Autowired
  private EventLogRepository eventLogRepository;

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

    Date creationDate = new Date();
    Buy buy = new Buy("Giraffe1", creationDate);
    animalService.buy(buy);

    Thread.sleep(1000);

    mockMvc.perform(get("/animals/Giraffe1")
        .accept(contentType))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.animalId", is("Giraffe1")))
        .andExpect(jsonPath("$.lastOccurence", is(creationDate.getTime())))
        .andExpect(jsonPath("$.feelingOfSatiety", is("full")))
        .andExpect(jsonPath("$.mindstate", is("happy")))
        .andExpect(jsonPath("$.hygiene", is("tidy")));
  }


}
