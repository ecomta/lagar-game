package org.game;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

public class Lagar {

    protected boolean isDisponivel = true;
    private final Queue<Caminhao> caminhao = new LinkedBlockingDeque<>();
    private static final Lagar lagar = new Lagar();
    private static Relatorio relatorio;
    private static LocalDate dataCorreta;

    private Lagar() {}

    public static Lagar getLagar() {
        return lagar;
    }

    protected void addCaminhao(Caminhao caminhao) throws InterruptedException, IOException {
        int quantidadeMaximaCaminhoes = 12;
        if (this.caminhao.size() <= quantidadeMaximaCaminhoes) {
            System.out.println("Adicionando caminhão à QUEUE: " + caminhao);

            this.caminhao.add(caminhao);
            relatorio.escreverArquivo("Carregando caminhão: ", caminhao, LocalTime.now());
        } else {
            this.isDisponivel = false;
        }
    }

    protected void recebeCaminhao() throws IOException {
        new Thread(() -> {
            System.out.println("Recebendo caminhão após tempo de descarga... ");
            try {
                relatorio.escreverArquivo(
                        "Recebendo Caminhão:  ",
                        Objects.requireNonNull(this.caminhao.poll()), LocalTime.now());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            this.isDisponivel = true;
        }).start();
    }


    public void setDataCorreta(LocalDate data) throws IOException {
        dataCorreta = data;
        relatorio = new Relatorio(dataCorreta);
    }

}