package zoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@EnableTransactionManagement
public class ZooApp {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(ZooApp.class, args);
  }
}