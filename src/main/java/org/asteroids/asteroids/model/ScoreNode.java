package org.asteroids.asteroids.model;

public class ScoreNode {
    String nomeJogador;
    int pontuacao;
    int altura; // Essencial para AVL
    public ScoreNode esquerda;
    public ScoreNode direita;

    public ScoreNode(String nome, int pontuacao) {
        this.nomeJogador = nome;
        this.pontuacao = pontuacao;
        this.altura = 1; // Todo nó novo começa com altura 1
    }

    // Getters para exibição
    public String getNomeJogador() { return nomeJogador; }
    public int getPontuacao() { return pontuacao; }
}
