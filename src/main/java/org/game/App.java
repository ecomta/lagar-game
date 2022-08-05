package org.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.Executors;

import org.leitura_arquivo.MapearArquivo;

public class App {

    public static void main(String[] args) throws IOException {

        MapearArquivo mapearArquivo = new MapearArquivo();        
        Path file = Paths.get("src", "main", "java", "org", "regras.txt");
        mapearArquivo.mapearRegrasDoArquivoTXT(Files.newBufferedReader(file));       

 
        Map<String, String> galega = mapearArquivo
                .getMapPlantacoes()
                .stream()
                .filter(map -> map.containsValue("Galega"))
                .findFirst()
                .get();

        Map<String, String> picual = mapearArquivo
                .getMapPlantacoes()
                .stream()
                .filter(map -> map.containsValue("Picual"))
                .findFirst()
                .get();

        Map<String, String> cordovil = mapearArquivo
                .getMapPlantacoes()
                .stream()
                .filter(map -> map.containsValue("Cordovil"))
                .findFirst()
                .get();  

        
        /**
         * Aqui eu vou criar 5 threads que possuem while true.
         */
        Lagar lagar = Lagar.getLagar();
        LocalDate dataRelatorio = mapearArquivo.getDataCorreta();
        lagar.setDataCorreta(dataRelatorio);
        var n = Executors.newFixedThreadPool(12);
        n.execute(() -> new Plantacao(lagar, "Plantação de Galega", galega, 10).carregaCaminhoes());
        n.execute(() -> new Plantacao(lagar, "Plantação de Galega", galega, 11).carregaCaminhoes());
        n.execute(() -> new Plantacao(lagar, "Plantação de Cordovil", cordovil, 20).carregaCaminhoes());
        n.execute(() -> new Plantacao(lagar, "Plantação de Cordovil", cordovil, 21).carregaCaminhoes());
        n.execute(() -> new Plantacao(lagar, "Plantação de Picual", picual, 30).carregaCaminhoes());

        /**
         * Isso faz com que após o tempo escolhido o programa pare de rodar.
         */
        n.shutdown();
    }

}