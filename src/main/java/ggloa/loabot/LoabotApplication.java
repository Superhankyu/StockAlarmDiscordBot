package ggloa.loabot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoabotApplication {

	public static void main(String[] args) {

		SpringApplication application = new SpringApplication(LoabotApplication.class);
		application.setWebApplicationType(WebApplicationType.NONE);

		application.run(args);
		// SpringApplication.run(LoabotApplication.class, args);

	}

}
