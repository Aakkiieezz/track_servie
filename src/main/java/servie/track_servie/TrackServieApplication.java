package servie.track_servie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
// import servie.track_servie.repository.UserRepository;

@SpringBootApplication
// @EnableJpaRepositories(basePackageClasses = UserRepository.class)
public class TrackServieApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(TrackServieApplication.class, args);
	}
}