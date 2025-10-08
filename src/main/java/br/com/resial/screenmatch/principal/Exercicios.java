package br.com.resial.screenmatch.principal;

import br.com.resial.screenmatch.model.Produto;

import java.util.*;
import java.util.stream.Collectors;

public class Exercicios {

    public void executaExercicios(){
        exercicio1();
        exercicio2();
        exercicio3();
        exercicio4();
        exercicio5();
        exercicio6();
    }

    public void exercicio1() {
        System.out.println("Exercicio 1");
        List<Integer> numeros = Arrays.asList(10, 20, 50, 40, 30);

        numeros.stream()
                .sorted(Comparator.reverseOrder())
                .limit(1)
                .forEach(System.out::println);

//        Optional<Integer> max = numeros.stream()
//                .max(Integer::compare);
//        max.ifPresent(System.out::println);
    }

    public void exercicio2() {
        System.out.println("Exercicio 2");
        List<String> palavras = Arrays.asList("java", "stream", "lambda", "code");
        palavras.stream()
                .sorted(Comparator.comparing(String::length))
                .sorted(Comparator.naturalOrder())
                .forEach(System.out::println);

//        Map<Integer, List<String>> agrupamento = palavras.stream()
//                .collect(Collectors.groupingBy(String::length));
//        System.out.println(agrupamento);
    }

    public void exercicio3() {
        System.out.println("Exercicio 3");
        List<String> nomes = Arrays.asList("Alice", "Bob", "Charlie");

        System.out.println(
                nomes.stream()
                        .collect(Collectors.joining(",")));
    }

    public void exercicio4() {
        System.out.println("Exercicio 4");
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);

        System.out.println(numeros.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * n)
                .mapToInt(Integer::intValue)
                .sum()
        );

//        int somaDosQuadrados = numeros.stream()
//                .filter(n -> n % 2 == 0)
//                .map(n -> n * n)
//                .reduce(0, Integer::sum);
//        System.out.println(somaDosQuadrados);
    }

    public void exercicio5() {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5, 6);

        System.out.println("Números pares");
        numeros.stream()
                .filter(n -> n % 2 == 0)
                .forEach(System.out::println);

        System.out.println("Números ímpares");
        numeros.stream()
                .filter(n -> n % 2 != 0)
                .forEach(System.out::println);

//        Map<Boolean, List<Integer>> particionado = numeros.stream()
//                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
//        System.out.println("Pares: " + particionado.get(true));  // Esperado: [2, 4, 6]
//        System.out.println("Ímpares: " + particionado.get(false)); // Esperado: [1, 3, 5]

    }

    public void exercicio6() {
        List<Produto> produtos = Arrays.asList(
                new Produto("Smartphone", 800.0, "Eletrônicos"),
                new Produto("Notebook", 1500.0, "Eletrônicos"),
                new Produto("Teclado", 200.0, "Eletrônicos"),
                new Produto("Cadeira", 300.0, "Móveis"),
                new Produto("Monitor", 900.0, "Eletrônicos"),
                new Produto("Mesa", 700.0, "Móveis")
        );

        System.out.println("Exercicio 6");
        Map<String, List<Produto>> produtosPorCategoria = produtos.stream()
                .collect(Collectors.groupingBy(Produto::getCategoria));
        System.out.println(produtosPorCategoria);

        System.out.println("Exercicio 7");
        Map<String, Long> qtdProdutosPorCategoria = produtos.stream()
                .collect(Collectors.groupingBy(Produto::getCategoria, Collectors.counting()));
        System.out.println(qtdProdutosPorCategoria);

        System.out.println("Exercicio 8");
        Map<String, Optional<Produto>> produtoMaisCaroPorCategoria = produtos.stream()
                .collect(
                        Collectors.groupingBy(
                                Produto::getCategoria,
                                Collectors.maxBy(Comparator.comparingDouble(Produto::getPreco))));
        System.out.println(produtoMaisCaroPorCategoria);

        System.out.println("Exercicio 9");
        Map<String, Double> precoTotalPorCategoria = produtos.stream()
                .collect(Collectors.groupingBy(Produto::getCategoria, Collectors.summingDouble(Produto::getPreco)));
        System.out.println(precoTotalPorCategoria);
    }

}
