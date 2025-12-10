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
        raiz.getEsquerda().setDireita(new FaseNode(5, "MEDIO_1"));

        // --- NÍVEL 3 (Filhos do lado Difícil) ---
        raiz.getDireita().setEsquerda(new FaseNode(6, "MEDIO_2"));
        raiz.getDireita().setDireita(new FaseNode(7, "CHEFE_DIFICIL"));

        // Dica: Para o trabalho final, você pode adicionar mais níveis aqui.
        // Uma árvore com altura 3 ou 4 já é suficiente para demonstrar o conceito.

        return raiz;
    }
}
