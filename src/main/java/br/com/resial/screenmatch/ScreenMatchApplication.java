package br.com.resial.screenmatch;

import br.com.resial.screenmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal();
		principal.exibeMenu();

//		var jsonEpisodio = consumoApi.obterDados("https://omdbapi.com/?t=supernatural&season=1&episode=1&apikey=6585022c");
//		DadosEpisodio episodio = conversor.obterDados(jsonEpisodio, DadosEpisodio.class);
//		System.out.println(episodio);
	}
}
