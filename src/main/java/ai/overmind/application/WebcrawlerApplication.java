package ai.overmind.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("ai.overmind.movie")
public class WebcrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebcrawlerApplication.class, args);
	}

}
