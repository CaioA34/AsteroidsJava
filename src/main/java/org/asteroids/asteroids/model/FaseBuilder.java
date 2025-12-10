package org.asteroids.asteroids.model;

public class FaseBuilder {
    public static FaseNode gerarArvoreFases() {
        // --- NÍVEL 1 (Raiz) ---
        FaseNode raiz = new FaseNode(1, "INICIO");

        // --- NÍVEL 2 ---
        // Esquerda: Caminho mais fácil
        raiz.setEsquerda(new FaseNode(2, "FACIL_1"));
        // Direita: Caminho mais difícil
        raiz.setDireita(new FaseNode(3, "DIFICIL_1"));

        // --- NÍVEL 3 (Filhos do lado Fácil) ---
        raiz.getEsquerda().setEsquerda(new FaseNode(4, "FACIL_2"));
        raiz.getEsquerda().setDireita(new FaseNode(5, "FACIL_3"));

        // --- NÍVEL 3 (Filhos do lado Difícil) ---
        raiz.getDireita().setEsquerda(new FaseNode(6, "DIFICIL_2"));
        raiz.getDireita().setDireita(new FaseNode(7, "DIFICIL_3"));

        return raiz;
    }
}
