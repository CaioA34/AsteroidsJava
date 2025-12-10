package org.asteroids.asteroids.view;

import org.asteroids.asteroids.model.FaseNode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.asteroids.asteroids.model.ScoreNode;

public class MapVisualizer {

    // Configurações visuais
    private static final int RAIO = 20;       // Tamanho da bolinha
    private static final int DISTANCIA_Y = 80; // Distância vertical entre níveis

    public void desenharArvore(GraphicsContext gc, FaseNode raiz, double larguraTela, FaseNode faseAtual) {
        if (raiz == null) return;

        // Começa a desenhar a partir do topo-centro da tela
        // x = meio da tela, y = 50px do topo, offset = 1/4 da tela
        desenharNoRecursivo(gc, raiz, larguraTela / 2, 50, larguraTela / 4, faseAtual);
    }

    private void desenharNoRecursivo(GraphicsContext gc, FaseNode no, double x, double y, double offsetX, FaseNode faseAtual) {
        if (no == null) return;

        // 1. Desenhar as Linhas (Arestas) PRIMEIRO
        // (Desenhamos antes para que a linha fique "atrás" da bolinha)
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        if (no.getEsquerda() != null) {
            // Linha da posição atual (x,y) até o filho esquerdo
            gc.strokeLine(x, y, x - offsetX, y + DISTANCIA_Y);
            // Chama recursão para o filho esquerdo
            desenharNoRecursivo(gc, no.getEsquerda(), x - offsetX, y + DISTANCIA_Y, offsetX / 2, faseAtual);
        }

        if (no.getDireita() != null) {
            // Linha da posição atual (x,y) até o filho direito
            gc.strokeLine(x, y, x + offsetX, y + DISTANCIA_Y);
            // Chama recursão para o filho direito
            desenharNoRecursivo(gc, no.getDireita(), x + offsetX, y + DISTANCIA_Y, offsetX / 2, faseAtual);
        }

        // 2. Desenhar a Bolinha (Nó)
        // Se for a fase que o jogador está agora, pintamos de AMARELO, senão CINZA
        // Aqui precisaríamos saber qual é a fase atual para pintar diferente.
        // Por enquanto vamos pintar tudo de azul ciano.
        if(no.getId() == faseAtual.getId()){
            gc.setFill(Color.YELLOW);
        }
        else{
            gc.setFill(Color.CYAN);
        }

        // O desenha oval desenha a partir do canto superior esquerdo, então subtraímos o RAIO para centralizar
        gc.fillOval(x - RAIO, y - RAIO, RAIO * 2, RAIO * 2);

        // Borda da bolinha
        gc.setStroke(Color.WHITE);
        gc.strokeOval(x - RAIO, y - RAIO, RAIO * 2, RAIO * 2);

        // 3. Desenhar o ID da fase dentro da bolinha
        gc.setFill(Color.BLACK);
        // Ajuste fino (-4, +4) para centralizar o texto visualmente
        gc.fillText(String.valueOf(no.getId()), x - 4, y + 4);
    }
    public void desenharArvoreAVL(GraphicsContext gc, ScoreNode raiz, double larguraTela) {
        if (raiz == null) return;
        // Desenha a partir do topo, similar à árvore de fases
        desenharScoreRecursivo(gc, raiz, larguraTela / 2, 50, larguraTela / 4);
    }
    private void desenharScoreRecursivo(GraphicsContext gc, ScoreNode no, double x, double y, double offsetX) {
        if (no == null) return;

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        // 1. Linhas (Arestas)
        if (no.esquerda != null) {
            gc.strokeLine(x, y, x - offsetX, y + 80);
            desenharScoreRecursivo(gc, no.esquerda, x - offsetX, y + 80, offsetX / 2);
        }
        if (no.direita != null) {
            gc.strokeLine(x, y, x + offsetX, y + 80);
            desenharScoreRecursivo(gc, no.direita, x + offsetX, y + 80, offsetX / 2);
        }

        // 2. O Nó (Bolinha) - Usaremos COR LILÁS/ROXO para diferenciar
        gc.setFill(Color.MEDIUMPURPLE);
        gc.fillOval(x - 25, y - 25, 50, 50); // Bolinha um pouco maior para caber o score

        gc.setStroke(Color.WHITE);
        gc.strokeOval(x - 25, y - 25, 50, 50);

        // 3. Texto (Score e Nome)
        gc.setFill(Color.WHITE);
        gc.setFont(new javafx.scene.text.Font("Arial", 12));

        // Centraliza o texto do Score
        String textoScore = String.valueOf(no.getPontuacao());
        gc.fillText(textoScore, x - (textoScore.length() * 3), y + 5);

        // Coloca o nome do jogador embaixo da bolinha
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(no.getNomeJogador(), x - (no.getNomeJogador().length() * 3), y + 40);
    }
}