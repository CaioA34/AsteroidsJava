package org.asteroids.asteroids;

import org.asteroids.asteroids.model.AVLTree;
import org.asteroids.asteroids.model.FaseNode;
import org.asteroids.asteroids.model.FaseBuilder;
import org.asteroids.asteroids.game.Player;
import org.asteroids.asteroids.game.Bullet;
import org.asteroids.asteroids.game.Asteroid;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import org.asteroids.asteroids.view.MapVisualizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.io.IOException;

public class AsteroidsGame extends Application {
    public static final int LARGURA = 800;
    public static final int ALTURA = 600;
    public int numPlayer = 0;
    private enum EstadoJogo {
        MAPA,
        JOGANDO,
        GAMEOVER,
        VISUALIZAR_AVL
    }
    private EstadoJogo estadoAtual = EstadoJogo.MAPA;

    // Componentes Gráficos
    private GraphicsContext gc;
    private Pane root;
    private FaseNode faseAtual;
    private MapVisualizer mapVisualizer;
    private Player player;

    private List<Bullet> tiros = new ArrayList<>();
    private List<Asteroid> asteroides = new ArrayList<>();
    private Random random = new Random();
    private int pontos = 0;
    private AVLTree placar = new AVLTree();

    @Override
    public void start(Stage stage) {
        Assets.carregar();
        faseAtual = FaseBuilder.gerarArvoreFases();
        mapVisualizer = new MapVisualizer();
        inicializarJogo();

        root = new Pane();
        Canvas canvas = new Canvas(LARGURA, ALTURA);
        root.getChildren().add(canvas);
        gc = canvas.getGraphicsContext2D();

        Scene scene = new Scene(root, LARGURA, ALTURA);

        // Tecla 'D' para simular ir para a Direita (Difícil)
        // Tecla 'E' para simular ir para a Esquerda (Fácil)
        scene.setOnKeyPressed(e -> {
            if (estadoAtual == EstadoJogo.MAPA) {
                // Controles do MAPA (Navegação na Árvore)
                switch (e.getCode()) {
                    case D:
                        if (faseAtual.getDireita() != null) faseAtual = faseAtual.getDireita();
                        break;
                    case A:
                        if (faseAtual.getEsquerda() != null) faseAtual = faseAtual.getEsquerda();
                        break;
                    case ENTER:
                        estadoAtual = EstadoJogo.JOGANDO;
                        break;
                }
            } else if (estadoAtual == EstadoJogo.JOGANDO) {
                switch (e.getCode()) {
                    case A:
                    case LEFT:
                        player.setMovendoEsquerda(true);
                        break;
                    case D:
                    case RIGHT:
                        player.setMovendoDireita(true);
                        break;
                    case ESCAPE:
                        estadoAtual = EstadoJogo.MAPA;
                        break;
                    case SPACE:
                        tiros.add(new Bullet(player.getX() + 18, player.getY()));
                        break;
                }
            }else if (estadoAtual == EstadoJogo.GAMEOVER) {
                if (e.getCode().toString().equals("ENTER")) {
                    estadoAtual = EstadoJogo.MAPA;
                    inicializarJogo();
                }
                if (e.getCode().toString().equals("T")) {
                    estadoAtual = EstadoJogo.VISUALIZAR_AVL;
                }
            }else if (estadoAtual == EstadoJogo.VISUALIZAR_AVL) {
                // ESC ou ENTER volta para o Game Over
                if (e.getCode().toString().equals("ESCAPE") || e.getCode().toString().equals("ENTER")) {
                    estadoAtual = EstadoJogo.GAMEOVER;
                }
            }
        });
        scene.setOnKeyReleased(e -> {
            if (estadoAtual == EstadoJogo.JOGANDO) {
                switch (e.getCode()) {
                    case A:
                    case LEFT:  player.setMovendoEsquerda(false); break;
                    case D:
                    case RIGHT: player.setMovendoDireita(false); break;
                }
            }
        });
        stage.setTitle("Asteroids - Estrutura de Dados II");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime) {
                if (estadoAtual == EstadoJogo.MAPA) {
                    desenharMapa();
                } else if (estadoAtual == EstadoJogo.JOGANDO){
                    atualizarJogo();
                    desenharJogo();
                }
                else if (estadoAtual == EstadoJogo.GAMEOVER){
                    desenharGameOver();
                }
                else if (estadoAtual == EstadoJogo.VISUALIZAR_AVL) { // <--- Novo Estado
                    desenharArvoreAVL();
                }
            }
        }.start();
    }
    private void inicializarJogo() {
        player = new Player(LARGURA / 2 - 20, ALTURA - 60, LARGURA);
        tiros.clear();
        asteroides.clear();
        pontos = 0;
    }
    private void atualizarJogo() {
        player.update();

        // Geração de asteroides

        if (random.nextInt(100) < 1) {
            asteroides.add(new Asteroid(LARGURA));
        }

        // 2. Atualizar Tiros
        for (int i = 0; i < tiros.size(); i++) {
            Bullet t = tiros.get(i);
            t.update();
            if (!t.isAtivo()) {
                tiros.remove(i);
                i--;
            }
        }

        // Atualizar Asteroides e Checar Colisões
        for (int i = 0; i < asteroides.size(); i++) {
            Asteroid a = asteroides.get(i);
            a.update();

            // Colisão: Asteroide x Player (Game Over)
            if (colisao(player.getX(), player.getY(), player.getLargura(), 40, a.getX(), a.getY(), a.getTamanho(), a.getTamanho())) {
                estadoAtual = EstadoJogo.GAMEOVER;
                placar.inserir("Player" + this.numPlayer, pontos);
                this.numPlayer++;
            }

            // Colisão: Asteroide x Tiro
            for (int j = 0; j < tiros.size(); j++) {
                Bullet t = tiros.get(j);
                if (colisao(t.getX(), t.getY(), 4, 10,
                        a.getX(), a.getY(), a.getTamanho(), a.getTamanho())) {
                    t.setAtivo(false); // Remove o tiro
                    a.setAtivo(false); // Remove o asteroide
                    pontos += 10;
                }
            }

            if (!a.isAtivo()) {
                asteroides.remove(i);
                i--;
            }
        }

    }
    private boolean colisao(double x1, double y1, double w1, double h1,
                            double x2, double y2, double w2, double h2) {
        return x1 < x2 + w2 && x1 + w1 > x2 &&
                y1 < y2 + h2 && y1 + h1 > y2;
    }

    private void desenharMapa() {
        if (Assets.background2 != null) {
            gc.drawImage(Assets.background2, 0, 0, LARGURA, ALTURA);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, LARGURA, ALTURA);
        }
        gc.setFill(Color.rgb(0, 0, 0, 0.6)); // Preto 60% transparente
        gc.fillRect(0, 0, LARGURA, ALTURA);

        // TÍTULO NO TOPO
        gc.setTextAlign(TextAlignment.CENTER); // Centraliza tudo automaticamente

        // Sombra do título
        gc.setFont(Font.font("Courier New", FontWeight.BOLD,42));
        gc.setFill(Color.BLACK);
        gc.fillText("SELEÇÃO DE FASE", LARGURA / 2 + 3, 83);

        // Título Principal
        gc.setFill(Color.CYAN); // Ou YELLOW, a cor que preferir
        gc.fillText("SELEÇÃO DE FASE", LARGURA / 2, 80);

        // Subtítulo
        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 18));
        gc.setFill(Color.WHITE);
        gc.fillText("Fase Atual: " + faseAtual.getId() + " - [" + faseAtual.getDificuldade() + "]", LARGURA / 2, 120);

        // DESENHA A ÁRVORE
        FaseNode raiz = FaseBuilder.gerarArvoreFases();
        mapVisualizer.desenharArvore(gc, raiz, LARGURA, faseAtual);

        // RODAPÉ COM INSTRUÇÕES
        gc.setFill(Color.rgb(0, 0, 0, 0.8));
        gc.fillRect(0, ALTURA - 60, LARGURA, 60);

        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 16));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Navegue com [ A / D ]   |   Pressione [ ENTER ] para Iniciar Missão", LARGURA / 2, ALTURA - 25);

        gc.setTextAlign(TextAlignment.LEFT);
    }

    private void desenharJogo() {
        if (Assets.background != null) {
            gc.drawImage(Assets.background, 0, 0, LARGURA, ALTURA);
        } else {
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, LARGURA, ALTURA);
        }
        player.draw(gc);


        for (Bullet t : tiros) {
            t.draw(gc);
        }
        for (Asteroid a : asteroides) a.draw(gc);

        gc.setFill(Color.WHITE);
        gc.fillText("FASE " + faseAtual.getId() + " - " + faseAtual.getDificuldade(), 20, 30);
        gc.fillText("ESC para voltar ao mapa", 20, 50);

        gc.setFill(Color.YELLOW);
        gc.fillText("SCORE: " + pontos, LARGURA - 100, 30);
    }
    private void desenharGameOver() {
        if (Assets.background != null) {
            gc.drawImage(Assets.background, 0, 0, LARGURA, ALTURA);
        }

        gc.setFill(Color.rgb(0, 0, 0, 0.85));
        gc.fillRect(0, 0, LARGURA, ALTURA);

        gc.setTextAlign(TextAlignment.CENTER);

        // TÍTULO "GAME OVER"
        gc.setFont(Font.font("Arial", FontWeight.BLACK, 60)); // Fonte Grossa

        gc.setFill(Color.BLACK);
        gc.fillText("GAME OVER", LARGURA / 2 + 3, ALTURA / 2 - 147);

        gc.setFill(Color.RED);
        gc.fillText("GAME OVER", LARGURA / 2, ALTURA / 2 - 150);

        // PONTUAÇÃO FINAL DO JOGADOR
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.setFill(Color.WHITE);
        gc.fillText("Sua Pontuação Final", LARGURA / 2, ALTURA / 2 - 90);

        gc.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 40));
        gc.setFill(Color.YELLOW);
        gc.fillText(String.valueOf(pontos), LARGURA / 2, ALTURA / 2 - 50);

        double boxWidth = 400;
        double boxHeight = 250;
        double boxX = (LARGURA - boxWidth) / 2;
        double boxY = ALTURA / 2 - 8;

        gc.setFill(Color.rgb(50, 50, 70, 0.8));
        gc.setStroke(Color.CYAN); // Borda Neon
        gc.setLineWidth(2);
        gc.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20); // Cantos arredondados
        gc.strokeRoundRect(boxX, boxY, boxWidth, boxHeight, 20, 20);

        // Ranking
        gc.setFill(Color.CYAN);
        gc.setFont(Font.font("Courier New", FontWeight.BOLD, 18));
        gc.fillText("- SCORE RANKING (AVL TREE) -", LARGURA / 2, boxY + 30);

        // Lista os Top Scores
        List<String> topScores = placar.getTopScores();
        gc.setFont(Font.font("Courier New", 16));
        gc.setFill(Color.WHITE);

        int maxScores = 7;
        for (int i = 0; i < Math.min(topScores.size(), maxScores); i++) {
            String s = topScores.get(i);
            // Desenha linha por linha
            gc.fillText(s, LARGURA / 2, boxY + 60 + (i * 25));
        }

        // RODAPÉ
        gc.setFont(Font.font("Courier New", 16));

        gc.setFill(Color.rgb(100, 255, 100)); // Verde Matrix
        gc.fillText("[ T ]  VISUALIZAR ESTRUTURA AVL", LARGURA / 2, ALTURA - 80);

        gc.setFont(Font.font("Courier New", 16));
        gc.setFill(Color.LIGHTGRAY);
        gc.fillText("Pressione [ ENTER ] para Reiniciar a Missão", LARGURA / 2, ALTURA - 30);

        gc.setTextAlign(TextAlignment.LEFT);
    }
    private void desenharArvoreAVL() {
        gc.setFill(Color.rgb(30, 30, 40));
        gc.fillRect(0, 0, LARGURA, ALTURA);

        // Título
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font(20));
        gc.fillText("Estrutura da Árvore AVL (Scores)", 20, 30);
        gc.setFont(javafx.scene.text.Font.font(12));
        gc.fillText("Pressione ESC para voltar", 20, 50);

        mapVisualizer.desenharArvoreAVL(gc, placar.getRaiz(), LARGURA);
    }

    public static void main(String[] args) {
        launch();
    }
}
