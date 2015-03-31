package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
/*@ComponentScan(basePackages = {"main"})
@Configuration
@EnableJpaRepositories(basePackages = {"persistence"})
@EnableAutoConfiguration
@EnableTransactionManagement*/
public class ZooApp {

  public static void main(String[] args) throws Exception {
    SpringApplication.run(ZooApp.class, args);
  }
}