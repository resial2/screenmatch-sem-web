package br.com.resial.screenmatch.principal;

import br.com.resial.screenmatch.model.DadosEpisodio;
import br.com.resial.screenmatch.model.DadosSerie;
import br.com.resial.screenmatch.model.DadosTemporada;
import br.com.resial.screenmatch.service.ConsumoApi;
import br.com.resial.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    public void exibeMenu() {

        System.out.println("Digite o nome da série: ");
        var nomeSerie = scanner.nextLine();

        var enderecoFinalDadosSerie = ENDERECO + nomeSerie.replace(" ", "+") + API_KEY;
        var serie = consumoApi.obterDados(enderecoFinalDadosSerie);
        DadosSerie dadosSerie = conversor.obterDados(serie, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();
		for (int i = 1; i <= dadosSerie.totalTemporadas(); i++){
            var enderecoFinalDadosTemporada = ENDERECO + nomeSerie.replace(" ", "+") + "&season="+ i + API_KEY;
			var jsonTemporadas = consumoApi.obterDados(enderecoFinalDadosTemporada);
			DadosTemporada temporada = conversor.obterDados(jsonTemporadas, DadosTemporada.class);
			temporadas.add(temporada);
		}

        long inicio = System.nanoTime();
        exibeEpisodiosSerieComFor(temporadas);
        long fim = System.nanoTime();
        var fimComFor = (fim - inicio) / 1000000;


        inicio = System.nanoTime();
        exibeEpisodiosSerieComLambda(temporadas);
        fim = System.nanoTime();
        var fimComLambda = (fim - inicio) / 1000000;

        System.out.println("Tempo de execução (Lambda): " + fimComLambda + " ms");
        System.out.println("Tempo de execução (Normal): " + fimComFor + " ms");
    }


    private void exibeEpisodiosSerieComFor(List<DadosTemporada> temporadas) {
        for (DadosTemporada temporada : temporadas) {
            System.out.println("-------------------------");
            System.out.println("Temporada " + temporada.temporada());
            System.out.println("------- Episódios -------");
            for (DadosEpisodio episodio : temporada.episodios()) {
                System.out.println(episodio.titulo());
            }
        }
        System.out.println("-------------------------");
    }

    private void exibeEpisodiosSerieComLambda(List<DadosTemporada> temporadas) {
        temporadas.forEach(t -> {
            System.out.println("-------------------------");
            System.out.println("Temporada " + t.temporada());
            System.out.println("------- Episódios -------");
            t.episodios().forEach(e -> System.out.println(e.titulo()));
        });
        System.out.println("-------------------------");
    }
}
