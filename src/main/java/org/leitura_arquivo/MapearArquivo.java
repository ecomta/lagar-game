package org.leitura_arquivo;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapearArquivo {
    static LocalDate dataCorreta;
    static List<String> variedades = new LinkedList<>();
    static List<String> plantacoes = new LinkedList<>();
    static List<Map<String, String>> mapPlantacoes = new LinkedList<>();
    static int[] capacidadeCaminhao = new int[2];

    public void mapearRegrasDoArquivoTXT(BufferedReader bufferedReader) throws IOException {
        // BufferedReader não pode ser null.
        Objects.requireNonNull(bufferedReader);

        String variedadesRegex = "(\\d) Variedades de Azeitonas:";
        String plantacoesRegex = "(\\d) Plantações de Azeitonas:";
        String capacidadesRegex = "(\\d) Capacidades de Recepção .*";

        String line;
        String qtdCapacidades = null;

        while ((line = bufferedReader.readLine()) != null) {
            if (dataCorreta == null) {
                dataCorreta = VerificarSessaoData.verificarSessaoDataDoArquivo(bufferedReader, line);
            }

            if (variedades.isEmpty()) {
                if (line.trim().matches(variedadesRegex)) {
                    // Preciso pular uma linha para não pegar o título.
                    line = bufferedReader.readLine();

                    // Se tiver linha em branco pula.
                    while (line.trim().isEmpty()) {
                        line = bufferedReader.readLine();
                    }

                    // Vai parar quando chegar no próximo regex (no regex das Plantações).
                    while (!line.trim().matches(plantacoesRegex)) {
                        // Pra não inserir vazio nem o título das Plantações.
                        if (!line.trim().isEmpty() && !line.matches(plantacoesRegex)) {
                            variedades.add(line.trim());
                        }
                        line = bufferedReader.readLine();
                    }
                }
            }

            if (plantacoes.isEmpty()) {
                if (line.trim().matches(plantacoesRegex)) {
                    // Vai parar de inserir quando encontrar o Regex da Quantidade de Capacidades.
                    while (!line.trim().matches(capacidadesRegex)) {
                        line = bufferedReader.readLine();
                        plantacoes.add(line.trim());
                    }
                }
            }

            // Capacidade de Recepção simultâneo.
            Matcher matcherQuantidadeRecepcoes = Pattern.compile(capacidadesRegex).matcher(line.trim());
            if (matcherQuantidadeRecepcoes.matches()) {
                qtdCapacidades = matcherQuantidadeRecepcoes.group(1);
            }

            // Capacidade de Transporte dos Caminhões.
            String transporteRegex = ".*(\\d+) até (\\d+).*";
            Matcher matcherCapacidadeCaminhao = Pattern.compile(transporteRegex).matcher(line.trim());
            if (matcherCapacidadeCaminhao.matches()) {
                // Não preciso de try-catch aqui... Regex vai pegar somente se houver \\d
                int value0 = Integer.parseInt(matcherCapacidadeCaminhao.group(1));
                int value1 = Integer.parseInt(matcherCapacidadeCaminhao.group(2));

                // Quero que o menor valor fique na posição 0 e o maior na posição 1 do array.
                capacidadeCaminhao[0] = Math.min(value0, value1);
                capacidadeCaminhao[1] = Math.max(value0, value1);
            }
        }

        mapearPlantacoes();

        //System.out.println(dataCorreta);
        //System.out.println(variedades);
        //System.out.println(plantacoes);
        //System.out.println(mapPlantacoes);
        //System.out.println(qtdCapacidades);
        //System.out.println(Arrays.toString(capacidadeCaminhao));

        bufferedReader.close();
    }

    private static void mapearPlantacoes() {
        String sobreCadaPlantacaoRegex = "(\\d) .* de (\\w+) .*(\\d).*";

        plantacoes.forEach(plantacao -> {
            Matcher matcher = Pattern.compile(sobreCadaPlantacaoRegex).matcher(plantacao);
            if (matcher.matches()) {
                mapPlantacoes.add(Map.of(
                        "plantacao", matcher.group(2),
                        "qtdPlantacoes", matcher.group(1),
                        "segundos", matcher.group(3)
                ));
            }
        });
    }

    public LocalDate getDataCorreta() {
        return dataCorreta;
    }

    public List<String> getPlantacoes() {
        return plantacoes;
    }

    public List<String> getVariedades() {
        return variedades;
    }

    public int[] getCapacidadeCaminhao() {
        return capacidadeCaminhao;
    }

    public List<Map<String, String>> getMapPlantacoes() {
        return mapPlantacoes;
    }
}