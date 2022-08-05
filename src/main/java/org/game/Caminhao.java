package org.game;

public class Caminhao {

    private int carga;
    private Plantacao plantacao;
    private int numeroCaminhao;


    public Caminhao(int carga, Plantacao plantacao, int numeroCaminhao) {
        this.carga = carga;
        this.plantacao = plantacao;
        this.numeroCaminhao = numeroCaminhao;
    }

    public int getCarga() {
        return carga;
    }

    public Plantacao getPlantacao() {
        return plantacao;
    }

    public int getNumeroCaminhao() {
        return this.numeroCaminhao;
    }

    @Override
    public String toString() {
        return "Caminhao{" + "carga=" + carga + ", plantacao=" + plantacao + '}';
    }
}