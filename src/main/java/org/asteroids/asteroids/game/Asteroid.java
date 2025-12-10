package org.asteroids.asteroids.game;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
        // Define um tamanho aleatório entre 30 e 50
        this.tamanho = 30 + random.nextInt(20);

        // Nasce em uma posição X aleatória dentro da tela
        this.x = random.nextDouble() * (larguraTela - tamanho);

        // Nasce fora da tela (em cima) para vir descendo
        this.y = -tamanho;

        // Velocidade aleatória entre 2 e 5 (alguns são mais rápidos)
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
        //gc.setFill(Color.BROWN); // Cor de pedra
        //gc.fillOval(x, y, tamanho, tamanho);

        // Um detalhe cinza para parecer pedra (opcional)
        //gc.setFill(Color.GRAY);
        //gc.fillOval(x + 5, y + 5, tamanho / 3, tamanho / 3);

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
