package org.asteroids.asteroids.model;

import java.util.ArrayList;
import java.util.List;

public class AVLTree {
    private ScoreNode raiz;

    // --- Métodos Auxiliares Básicos ---
    private int altura(ScoreNode N) {
        if (N == null) return 0;
        return N.altura;
    }

    private int max(int a, int b) {
        return (a > b) ? a : b;
    }

    private int getBalanceamento(ScoreNode N) {
        if (N == null) return 0;
        return altura(N.esquerda) - altura(N.direita);
    }

    // --- ROTAÇÕES (A mágica da AVL) ---

    // Rotação à Direita (Simples)
    private ScoreNode rotacaoDireita(ScoreNode y) {
        ScoreNode x = y.esquerda;
        ScoreNode T2 = x.direita;

        // Realiza a rotação
        x.direita = y;
        y.esquerda = T2;

        // Atualiza alturas
        y.altura = max(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = max(altura(x.esquerda), altura(x.direita)) + 1;

        // Retorna nova raiz
        return x;
    }

    // Rotação à Esquerda (Simples)
    private ScoreNode rotacaoEsquerda(ScoreNode x) {
        ScoreNode y = x.direita;
        ScoreNode T2 = y.esquerda;

        // Realiza a rotação
        y.esquerda = x;
        x.direita = T2;

        // Atualiza alturas
        x.altura = max(altura(x.esquerda), altura(x.direita)) + 1;
        y.altura = max(altura(y.esquerda), altura(y.direita)) + 1;

        return y;
    }

    // --- INSERÇÃO ---
    public void inserir(String nome, int pontuacao) {
        raiz = inserirRec(raiz, nome, pontuacao);
    }

    private ScoreNode inserirRec(ScoreNode node, String nome, int pontuacao) {
        // 1. Inserção normal de BST (Binary Search Tree)
        if (node == null)
            return (new ScoreNode(nome, pontuacao));

        // Ordenando por pontuação (Menores à esquerda, Maiores à direita)
        if (pontuacao < node.pontuacao)
            node.esquerda = inserirRec(node.esquerda, nome, pontuacao);
        else if (pontuacao > node.pontuacao)
            node.direita = inserirRec(node.direita, nome, pontuacao);
        else // Pontuações iguais não permitidas ou ignoradas para simplificar
            return node;

        // 2. Atualiza altura do ancestral
        node.altura = 1 + max(altura(node.esquerda), altura(node.direita));

        // 3. Checa o fator de balanceamento
        int balance = getBalanceamento(node);

        // Se estiver desbalanceado, temos 4 casos:

        // Caso Esquerda-Esquerda
        if (balance > 1 && pontuacao < node.esquerda.pontuacao)
            return rotacaoDireita(node);

        // Caso Direita-Direita
        if (balance < -1 && pontuacao > node.direita.pontuacao)
            return rotacaoEsquerda(node);

        // Caso Esquerda-Direita
        if (balance > 1 && pontuacao > node.esquerda.pontuacao) {
            node.esquerda = rotacaoEsquerda(node.esquerda);
            return rotacaoDireita(node);
        }

        // Caso Direita-Esquerda
        if (balance < -1 && pontuacao < node.direita.pontuacao) {
            node.direita = rotacaoDireita(node.direita);
            return rotacaoEsquerda(node);
        }

        return node;
    }

    // --- LISTAGEM (Para mostrar na tela) ---
    // Fazemos um percurso "In-Order Inverso" (Direita, Raiz, Esquerda)
    // para pegar do maior para o menor
    public List<String> getTopScores() {
        List<String> lista = new ArrayList<>();
        percorrerDecrescente(raiz, lista);
        return lista;
    }

    private void percorrerDecrescente(ScoreNode no, List<String> lista) {
        if (no != null) {
            percorrerDecrescente(no.direita, lista);
            lista.add(no.nomeJogador + " - " + no.pontuacao);
            percorrerDecrescente(no.esquerda, lista);
        }
    }

    public ScoreNode getRaiz() {
        return raiz;
    }
}
