package org.asteroids.asteroids.game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet {
    private double x;
    private double y;
    private double velocidade = 10;
    private boolean ativo = true;   // Se sair da tela, vira false

    public Bullet(double xInicial, double yInicial) {
        this.x = xInicial;
        this.y = yInicial;
    }

    public void update() {
        y -= velocidade;
        if (y < 0) {
            ativo = false;
        }
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.YELLOW);
        gc.fillRect(x, y, 4, 10);
    }

    public boolean isAtivo() {
        return ativo;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelocidade() {
        return velocidade;
    }

    public void setVelocidade(double velocidade) {
        this.velocidade = velocidade;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
