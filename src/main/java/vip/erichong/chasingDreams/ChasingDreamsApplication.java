package vip.erichong.chasingDreams;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author eric
 */
@SpringBootApplication
@ComponentScan("vip.erichong.chasingDreams.**")
public class ChasingDreamsApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChasingDreamsApplication.class, args);
    }
}
