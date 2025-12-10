package org.asteroids.asteroids.view;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import org.asteroids.asteroids.model.FaseNode;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.asteroids.asteroids.model.ScoreNode;

public class MapVisualizer {

    private static final int RAIO = 20;
    private static final int DISTANCIA_Y = 80;

    public void desenharArvore(GraphicsContext gc, FaseNode raiz, double larguraTela, FaseNode faseAtual) {
        if (raiz == null) return;

        desenharNoRecursivo(gc, raiz, larguraTela / 2, 180, larguraTela / 4, faseAtual);
    }

    private void desenharNoRecursivo(GraphicsContext gc, FaseNode no, double x, double y, double offsetX, FaseNode faseAtual) {
        if (no == null) return;

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        if (no.getEsquerda() != null) {
            gc.strokeLine(x, y, x - offsetX, y + DISTANCIA_Y);
            desenharNoRecursivo(gc, no.getEsquerda(), x - offsetX, y + DISTANCIA_Y, offsetX / 2, faseAtual);
        }

        if (no.getDireita() != null) {
            gc.strokeLine(x, y, x + offsetX, y + DISTANCIA_Y);
            desenharNoRecursivo(gc, no.getDireita(), x + offsetX, y + DISTANCIA_Y, offsetX / 2, faseAtual);
        }

        if(no.getId() == faseAtual.getId()){
            gc.setFill(Color.YELLOW.deriveColor(0, 1, 1, 0.5));
            gc.fillOval(x - RAIO - 5, y - RAIO - 5, (RAIO * 2) + 10, (RAIO * 2) + 10);
            gc.setFill(Color.GOLD);
        }
        else{
            if (no.getDificuldade().contains("FACIL")) {
                gc.setFill(Color.LIGHTBLUE);
            } else if (no.getDificuldade().contains("DIFICIL")) {
                gc.setFill(Color.ORANGERED);
            } else {
                gc.setFill(Color.CYAN);
            }
        }

        gc.fillOval(x - RAIO, y - RAIO, RAIO * 2, RAIO * 2);

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeOval(x - RAIO, y - RAIO, RAIO * 2, RAIO * 2);

        gc.setFill(Color.BLACK);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(String.valueOf(no.getId()), x, y + 5);
    }
    public void desenharArvoreAVL(GraphicsContext gc, ScoreNode raiz, double larguraTela) {
        if (raiz == null) return;
        desenharScoreRecursivo(gc, raiz, larguraTela / 2, 50, larguraTela / 4);
    }
    private void desenharScoreRecursivo(GraphicsContext gc, ScoreNode no, double x, double y, double offsetX) {
        if (no == null) return;

        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);

        if (no.esquerda != null) {
            gc.strokeLine(x, y, x - offsetX, y + 80);
            desenharScoreRecursivo(gc, no.esquerda, x - offsetX, y + 80, offsetX / 2);
        }
        if (no.direita != null) {
            gc.strokeLine(x, y, x + offsetX, y + 80);
            desenharScoreRecursivo(gc, no.direita, x + offsetX, y + 80, offsetX / 2);
        }

        gc.setFill(Color.MEDIUMPURPLE);
        gc.fillOval(x - 25, y - 25, 50, 50);

        gc.setStroke(Color.WHITE);
        gc.strokeOval(x - 25, y - 25, 50, 50);

        gc.setFill(Color.WHITE);
        gc.setFont(new javafx.scene.text.Font("Arial", 12));

        String textoScore = String.valueOf(no.getPontuacao());
        gc.fillText(textoScore, x - (textoScore.length() * 3), y + 5);

        gc.setFill(Color.LIGHTGRAY);
        gc.fillText(no.getNomeJogador(), x - (no.getNomeJogador().length() * 3), y + 40);
    }
}