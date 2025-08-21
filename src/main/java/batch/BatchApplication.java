package batch;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ComponentScan(basePackages = {"service"})
@SpringBootApplication
public class BatchApplication {
    public static void main(String[] args) {
        SpringApplication.run(BatchApplication.class, args);
    }
}

