package org.game;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class Plantacao {

    protected Lagar lagar;
    private String nome;
    private Map<String, String> mapAzeitona = new HashMap<>();
    private int numeroCaminhao;

    private static final int TEMPO_PLANTACAO = 120_000;

    public Plantacao(Lagar lagar, String nome, Map<String, String> mapAzeitona, int numeroCaminhao) {
        this.lagar = lagar;
        this.nome = nome;
        this.mapAzeitona = mapAzeitona;
        this.numeroCaminhao = numeroCaminhao;
    }

    public void carregaCaminhoes() {
        new Thread(() -> {
            long milis = System.currentTimeMillis();
            do {
                int tempoCarregamento = (1 + new SecureRandom().nextInt(3) + Integer.valueOf(mapAzeitona.get("segundos"))) * 1000;

                /*
                 * Quando lagar não estará disponível?
                 * Quando a fila de caminhões for >= 12
                 */
                if (!this.lagar.isDisponivel) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }

                /*
                 * Cada caminhão possui um tempo de carregamento, isso é outro Thread.sleep();
                 */
                try {
                    this.lagar.addCaminhao(new Caminhao(2 + new SecureRandom().nextInt(6), this, numeroCaminhao));
                    Thread.sleep(tempoCarregamento);
                    this.lagar.recebeCaminhao();
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            } while (System.currentTimeMillis() - milis < TEMPO_PLANTACAO);
        }).start();
    }

    @Override
    public String toString() {
        return "Plantacao{" +
                "lagar=" + lagar +
                ", nome='" + nome + '\'' +
                '}';
    }
}