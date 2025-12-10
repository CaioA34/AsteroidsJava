package org.asteroids.asteroids.game;
import javafx.scene.canvas.GraphicsContext;
import org.asteroids.asteroids.Assets;

import java.util.Random;

public class Asteroid {
    private double x;
    private double y;
    private double velocidade;
    private double tamanho;
    private boolean ativo = true;

    // Gerador de aleatoriedade para variar os asteroides
    private static final Random random = new Random();

    public Asteroid(double larguraTela) {
        this.tamanho = 30 + random.nextInt(20);
        this.x = random.nextDouble() * (larguraTela - tamanho);
        this.y = -tamanho;
        this.velocidade = 2 + random.nextDouble() * 3;
    }

    public void update() {
        y += velocidade; // Aumenta Y para descer

        // Se passar da parte de baixo da tela (600 + tamanho), desativa
        if (y > 600 + tamanho) {
            ativo = false;
        }
    }

    public void draw(GraphicsContext gc) {
        if (Assets.asteroide != null) {
            // Opcional: Efeito de rotação (avançado), mas desenhar simples funciona bem:
            gc.drawImage(Assets.asteroide, x, y, tamanho, tamanho);
        }
    }

    // Getters para colisão
    public double getX() { return x; }
    public double getY() { return y; }
    public double getTamanho() { return tamanho; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
