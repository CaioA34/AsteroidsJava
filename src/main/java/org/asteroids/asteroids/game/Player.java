package org.asteroids.asteroids.game;

import javafx.scene.canvas.GraphicsContext;
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
        if (movendoEsquerda && x > 0) {
            x -= velocidade;
        }
        if (movendoDireita && x < limiteTelaX - largura) {
            x += velocidade;
        }
    }

    public void draw(GraphicsContext gc) {
        if (Assets.nave != null) {
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