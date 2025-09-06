package br.com.resial.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosEpisodio(@JsonAlias("Title") String titulo,
                            @JsonAlias("Year") int ano,
                            @JsonAlias("Season") int temporada,
                            @JsonAlias("Episode") int episodio,
                            @JsonAlias("imdbRating") String avaliacao,
                            @JsonAlias("Runtime") String duracao,
                            @JsonAlias("Released") String lancamento,
                            @JsonAlias("Poster") String poster) {
}
