package br.com.resial.screenmatch;

import br.com.resial.screenmatch.model.DadosSerie;
import br.com.resial.screenmatch.service.ConsumoApi;
import br.com.resial.screenmatch.service.ConverteDados;
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
		var novaSerie = new ConsumoApi();
		var json = novaSerie.obterDados("https://omdbapi.com/?t=supernatural&apikey=6585022c");
		System.out.println(json);

		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
