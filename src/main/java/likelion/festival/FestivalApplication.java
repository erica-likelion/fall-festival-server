package likelion.festival;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class FestivalApplication {

	public static void main(String[] args) {
		SpringApplication.run(FestivalApplication.class, args);
	}

}
