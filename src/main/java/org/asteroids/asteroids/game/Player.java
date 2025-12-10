package org.asteroids.asteroids.game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import org.asteroids.asteroids.Assets;

public class Player {

    private double x, y;
    private double largura = 40;
    private double altura = 40;
    private double velocidade = 5;
    // Controle de movimento
    private boolean movendoEsquerda = false;
    private boolean movendoDireita = false;

    // Limites da tela
    private double limiteTelaX;

    public Player(double xInicial, double yInicial, double larguraTela) {
        this.x = xInicial;
        this.y = yInicial;
        this.limiteTelaX = larguraTela;
    }

    public void update() {
        // Atualiza a posição baseada nas flags (bandeiras) de movimento
        if (movendoEsquerda && x > 0) {
            x -= velocidade;
        }
        if (movendoDireita && x < limiteTelaX - largura) {
            x += velocidade;
        }
    }

    public void draw(GraphicsContext gc) {
        //gc.setFill(Color.GREEN);

        // Desenha um triângulo simples representando a nave
        // Pontos X: [esquerda, centro, direita]
        //double[] xPoints = { x, x + largura / 2, x + largura };
        // Pontos Y: [base, topo, base]
        //double[] yPoints = { y + altura, y, y + altura };

        //gc.fillPolygon(xPoints, yPoints, 3);
        if (Assets.nave != null) {
            // Desenha a imagem nas coordenadas X, Y com o tamanho definido
            gc.drawImage(Assets.nave, x, y, largura, altura);
        }
    }

    // Métodos para o Teclado chamarem
    public void setMovendoEsquerda(boolean mover) { this.movendoEsquerda = mover; }
    public void setMovendoDireita(boolean mover) { this.movendoDireita = mover; }

    // Getters úteis para o futuro (tiros)
    public double getX() { return x; }
    public double getY() { return y; }
    public double getLargura() { return largura; }
}