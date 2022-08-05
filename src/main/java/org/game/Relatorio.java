package org.game;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Relatorio {
    static Path relatorioFile;

    public Relatorio(LocalDate localDate) throws IOException {
        construirArquivo(localDate);
    }

    private static void construirArquivo(LocalDate date) throws IOException {
        relatorioFile = Paths.get( "src", "main", "java", "org", "relatorios", "relatorio-" + date.getYear() + ".txt");

        Files.writeString(relatorioFile, "");
        Files.write(relatorioFile, date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")).getBytes(), StandardOpenOption.APPEND);
        Files.write(relatorioFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
    }

    protected void escreverArquivo(String descricaoOperacao, Caminhao caminhao1, LocalTime localTime) throws IOException {
        synchronized (this) {
            String escrever = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " - " +
                    descricaoOperacao + " - " +
                    "Caminhão nº: " + caminhao1.getNumeroCaminhao() + " " +
                    "Quantidade Carga: " + caminhao1.getCarga() + " " +
                    "Plantação: " + caminhao1.getPlantacao();
            Files.write(relatorioFile, escrever.getBytes(), StandardOpenOption.APPEND);
            Files.write(relatorioFile, System.lineSeparator().getBytes(), StandardOpenOption.APPEND);
        }
    }

}