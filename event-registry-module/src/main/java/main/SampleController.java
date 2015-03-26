package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SampleController {

  public static class MyWorld {
    public String value = "Hello World!";
  }


  //@ResponseBody
  @RequestMapping("/")
  MyWorld home() {
    return new MyWorld();
  }

  public static void main(String[] args) throws Exception {
    SpringApplication.run(SampleController.class, args);
  }
}