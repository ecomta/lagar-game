package org.leitura_arquivo;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;

public class VerificarSessaoData {

    public static LocalDate verificarSessaoDataDoArquivo(BufferedReader bufferedReader, String line) throws IOException {
        LocalDate dataCorreta = null;
        if (line.trim().matches("Data:")) {
            // Pular a primeira linha que é a própria Data.
            line = bufferedReader.readLine();

            while (line.isEmpty()) {
                line = bufferedReader.readLine();
            }

            dataCorreta = formatarData(line.trim());
        }
        return dataCorreta;
    }

    private static LocalDate formatarData(String data) {
        LocalDate dataCorreta;
        DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofPattern("[dd-MM-yyyy]" + "[yyyy-MM-dd]" + "[dd/MM/yyyy]" + "[yyyy/MM/dd]"));

        DateTimeFormatter formatter = formatterBuilder.toFormatter();
        try {
            dataCorreta = LocalDate.parse(data, formatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeException("Data inválida. " + e.getMessage());
        }

        return dataCorreta;
    }

}