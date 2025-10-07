package br.com.resial.screenmatch.principal;

import br.com.resial.screenmatch.model.DadosEpisodio;
import br.com.resial.screenmatch.model.DadosSerie;
import br.com.resial.screenmatch.model.DadosTemporada;
import br.com.resial.screenmatch.model.Episodio;
import br.com.resial.screenmatch.service.ConsumoApi;
import br.com.resial.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";

    private final boolean EXIBE_TODOS_EPISODIOS = false;
    private final boolean EXIBE_TOP_EPISODIOS = true;

    public void exibeMenu() {

        System.out.println("Digite o nome da série: ");
        var nomeSerie = scanner.nextLine();

        System.out.println("Carregando dados das temporadas...");
        List<DadosTemporada> temporadas = buscaDadosTemporadasSerie(nomeSerie);
        System.out.println("Dados das temporadas carregados.");

        System.out.println("Montando lista de episodios...");
        List<Episodio> episodios = montaListaEpisodiosDasTemporadas(temporadas);
        System.out.println("Lista de episodios montada.");

        if (EXIBE_TODOS_EPISODIOS) {
//            exibeEpisodiosSerieComFor(temporadas);
            exibeEpisodiosSerieComLambda(temporadas);
        }

        if (EXIBE_TOP_EPISODIOS) {
//            exibeTopEpisodios(5, temporadas);
            exibeTopEpisodiosTratado(5, temporadas);
        }

//        buscarEpisodiosPorTrechoTitulo("Bastard", episodios);

        exibeMediaAvaliacaoPorTemporada(episodios);
        exibeEstatisticasEpisodios(episodios);

    }


    private DadosSerie buscaDadosSerie(String nomeSerie) {
        System.out.println("Buscando série...");
        long inicio = System.nanoTime();
        var enderecoFinalDadosSerie = ENDERECO + nomeSerie.replace(" ", "+") + API_KEY;
        var serie = consumoApi.obterDados(enderecoFinalDadosSerie);
        DadosSerie dadosSerie = conversor.obterDados(serie, DadosSerie.class);
        long fim = System.nanoTime();
        tempoExecucao(inicio, fim);

        return dadosSerie;
    }

    private List<DadosTemporada> buscaDadosTemporadasSerie(String nomeSerie) {
        DadosSerie dadosSerie = buscaDadosSerie(nomeSerie);

        System.out.println("Buscando temporadas...");
        long inicio = System.nanoTime();
        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++){
            var enderecoFinalDadosTemporada = ENDERECO + nomeSerie.replace(" ", "+") + "&season="+ i + API_KEY;
            var jsonTemporadas = consumoApi.obterDados(enderecoFinalDadosTemporada);
            DadosTemporada temporada = conversor.obterDados(jsonTemporadas, DadosTemporada.class);
            temporadas.add(temporada);
        }
        long fim = System.nanoTime();
        tempoExecucao(inicio, fim);

        return temporadas;
    }

    private List<Episodio> montaListaEpisodiosDasTemporadas(List<DadosTemporada> temporadas) {
        return temporadas.stream()
                .flatMap(
                        t -> t.episodios().stream()
                                .map(dadosEp -> new Episodio(t.temporada(), dadosEp))
                )
                .toList();
    }

    @Deprecated
    private void exibeEpisodiosSerieComFor(List<DadosTemporada> temporadas) {
        long inicio = System.nanoTime();
        for (DadosTemporada temporada : temporadas) {
            System.out.println("-------------------------");
            System.out.println("Temporada " + temporada.temporada());
            System.out.println("------- Episódios -------");
            for (DadosEpisodio episodio : temporada.episodios()) {
                System.out.println(episodio.titulo());
            }
        }
        System.out.println("-------------------------");
        long fim = System.nanoTime();
        var fimComFor = (fim - inicio) / 1000000;
        System.out.println("Tempo de execução (For): " + fimComFor + " ms");
    }

    private void exibeEpisodiosSerieComLambda(List<DadosTemporada> temporadas) {
        long inicio = System.nanoTime();
        temporadas.forEach(t -> {
            System.out.println("-------------------------");
            System.out.println("Temporada " + t.temporada());
            System.out.println("------- Episódios -------");
            t.episodios().forEach(e -> System.out.println(e.titulo()));
        });
        System.out.println("-------------------------");
        long fim = System.nanoTime();
        var fimComLambda = (fim - inicio) / 1000000;
        System.out.println("Tempo de execução (Lambda): " + fimComLambda + " ms");
    }

    @Deprecated
    private void exibeTopEpisodios(int qtdEpisodios, List<DadosTemporada> temporadas) {
        // toList() -> Cria listas imutaveis
        List<DadosEpisodio> todosEpisodiosSerie  = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .toList();

        // collect(Collectors.toList()) -> Cria listas mutaveis (podem ter mais dados adicionados posteriormente)
        System.out.println("Top " + qtdEpisodios + " episódios da série");
        todosEpisodiosSerie.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(qtdEpisodios)
                .collect(Collectors.toList())
                .forEach(System.out::println);
    }

    private void exibeTopEpisodiosTratado(int qtdEpisodios, List<DadosTemporada> temporadas) {
        List<Episodio> episodios = montaListaEpisodiosDasTemporadas(temporadas);

        System.out.println("Top " + qtdEpisodios + " episódios da série");
        episodios.stream()
                .sorted(Comparator.comparing(Episodio::getAvaliacao).reversed())
//                .peek(e -> System.out.println("Organização por avaliação" + e))
                .limit(qtdEpisodios)
                .toList()
                .forEach(System.out::println);
    }

    private List<Episodio> buscarEpisodiosPorTrechoTitulo(String trechoTitulo, List<Episodio> episodios) {

        List<Episodio> epBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toLowerCase().contains(trechoTitulo.toLowerCase()))
                .collect(Collectors.toList());

        if(!epBuscado.isEmpty()){
            System.out.println("Episódio(s) encontrado(s).");
            epBuscado.forEach(System.out::println);
        } else {
            System.out.println("Episódio não encontrado... :(");
        }

        return epBuscado;
    }

    private void exibeMediaAvaliacaoPorTemporada(List<Episodio> episodios) {
        Map<Integer, Double> avaliacoesTemporadas =
                episodios.stream()
                        .filter(e -> e.getAvaliacao() >= 0.0)
                        .collect(Collectors.groupingBy(Episodio::getTemporada,
                                Collectors.averagingDouble(Episodio::getAvaliacao)));

        System.out.println(avaliacoesTemporadas);
    }

    private void exibeEstatisticasEpisodios(List<Episodio> episodios) {
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() >= 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));

        System.out.println("Quantidade de episódios avaliados: " + est.getCount());
        System.out.println("Nota média dos episódios: " + est.getAverage());
        System.out.println("Episódio mais bem avaliado: " + est.getMax());
        System.out.println("Episódio pior avaliado: " + est.getMin());
    }

    private void tempoExecucao(long inicio, long fim){
        System.out.println("Tempo de execução: " + (fim - inicio) / 1000000 + " ms");
    }
}
