package by.prus.siteinfocheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Clock;
import java.time.ZoneId;

@SpringBootApplication
@EnableScheduling
public class SiteInfoCheckApplication {

	public static void main(String[] args) {
		SpringApplication.run(SiteInfoCheckApplication.class, args);
	}

	@Bean
	public Clock clock() {
		return Clock.system(ZoneId.of("Europe/Rome"));
	}

}
