package org.asteroids.asteroids.model;

public class FaseNode {
    private int id;
    private String dificuldade;
    private boolean jaVisitada;

    private FaseNode esquerda;
    private FaseNode direita;

    public FaseNode(int id, String dificuldade){
        this.id = id;
        this.dificuldade = dificuldade;
        this.jaVisitada = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDificuldade() {
        return dificuldade;
    }

    public void setDificuldade(String dificuldade) {
        this.dificuldade = dificuldade;
    }

    public boolean isJaVisitada() {
        return jaVisitada;
    }

    public void setJaVisitada(boolean jaVisitada) {
        this.jaVisitada = jaVisitada;
    }

    public FaseNode getEsquerda() {
        return esquerda;
    }

    public void setEsquerda(FaseNode esquerda) {
        this.esquerda = esquerda;
    }

    public FaseNode getDireita() {
        return direita;
    }

    public void setDireita(FaseNode direita) {
        this.direita = direita;
    }
}
