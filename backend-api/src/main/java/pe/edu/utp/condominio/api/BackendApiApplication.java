package pe.edu.utp.condominio.api;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import java.util.TimeZone;

@SpringBootApplication
@ConfigurationPropertiesScan
public class BackendApiApplication {

	@PostConstruct
	public void inicializarZonaHoraria() {
		TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"));
	}

	public static void main(String[] args) {
		SpringApplication.run(BackendApiApplication.class, args);
	}

}