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
    private GraphicsContext gc; // O "Pincel" para desenhar o jogo
    private Pane root;          // O Container principal
    private FaseNode faseAtual;
    private MapVisualizer mapVisualizer;
    private Player player;

    private List<Bullet> tiros = new ArrayList<>();
    private List<Asteroid> asteroides = new ArrayList<>();
    private Random random = new Random();
    private int pontos = 0;
    private AVLTree placar = new AVLTree(); // Nossa árvore AVL

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
        // Tecla 'E' para simular ir para a Esquerda (Fácil) - apenas para teste
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
                        // Entra na fase selecionada!
                        estadoAtual = EstadoJogo.JOGANDO;
                        break;
                }
            } else if (estadoAtual == EstadoJogo.JOGANDO) {
                // Controles do JOGO (Nave)
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
                        break; // Volta pro mapa
                    case SPACE:
                        tiros.add(new Bullet(player.getX() + 18, player.getY()));
                        break;
                }
            }else if (estadoAtual == EstadoJogo.GAMEOVER) {
                if (e.getCode().toString().equals("ENTER")) {
                    estadoAtual = EstadoJogo.MAPA;
                    inicializarJogo();
                }
                // --- NOVO: Tecla T para ver a árvore ---
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

        // 1. Criar novos asteroides (Spawn)
        // 2% de chance a cada frame (aprox 1 asteroide por segundo)
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

        // 3. Atualizar Asteroides e Checar Colisões
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
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, LARGURA, ALTURA);

        // Usa sua classe visualizadora
        FaseNode raiz = FaseBuilder.gerarArvoreFases(); // Simplificação: pegando a raiz de novo
        mapVisualizer.desenharArvore(gc, raiz, LARGURA, faseAtual);

        gc.setFill(Color.WHITE);
        gc.fillText("MAPA: Use A/D para escolher. ENTER para entrar na fase " + faseAtual.getId(), 20, 30);
    }

    private void desenharJogo() {
        // Limpa a tela
        //gc.setFill(Color.INDIGO);
        //gc.fillRect(0, 0, LARGURA, ALTURA);
        if (Assets.background != null) {
            // Desenha a imagem esticada para caber na tela inteira (LARGURA, ALTURA)
            gc.drawImage(Assets.background, 0, 0, LARGURA, ALTURA);
        } else {
            // Fallback caso a imagem falhe (tela preta)
            gc.setFill(Color.BLACK);
            gc.fillRect(0, 0, LARGURA, ALTURA);
        }
        // 2. Desenha a Árvore de Fases
        // Passamos 'gerarArvoreFases()' de novo só para pegar a RAIZ da árvore para desenhar tudo
        // (O ideal seria salvar a raiz numa variável separada, mas assim funciona rápido)]
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
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, LARGURA, ALTURA);
        gc.setFill(Color.RED);
        gc.setFont(javafx.scene.text.Font.font(30));
        gc.fillText("GAME OVER", LARGURA / 2 - 80, ALTURA / 2 - 100);
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font(12)); // Fonte normal
        gc.fillText("Sua Pontuação: " + pontos, LARGURA / 2 - 50, ALTURA / 2 - 60);
        gc.setFill(Color.CYAN);
        gc.fillText("--- RANKING (AVL TREE) ---", LARGURA / 2 - 80, ALTURA / 2 - 20);
        List<String> topScores = placar.getTopScores();
        int y = ALTURA / 2;
        for (String s : topScores) {
            gc.fillText(s, LARGURA / 2 - 60, y);
            y += 20;
        }
        gc.setFill(Color.YELLOW);
        gc.fillText("Pressione 'T' para visualizar a Árvore AVL Gráfica", LARGURA/2 - 100, ALTURA - 80);
        gc.setFill(Color.GRAY);
        gc.fillText("Pressione ENTER para voltar ao Mapa", LARGURA / 2 - 100, ALTURA - 50);
    }
    private void desenharArvoreAVL() {
        // Limpa a tela com uma cor de fundo diferente (ex: cinza escuro)
        gc.setFill(Color.rgb(30, 30, 40));
        gc.fillRect(0, 0, LARGURA, ALTURA);

        // Título
        gc.setFill(Color.WHITE);
        gc.setFont(javafx.scene.text.Font.font(20));
        gc.fillText("Estrutura da Árvore AVL (Scores)", 20, 30);
        gc.setFont(javafx.scene.text.Font.font(12));
        gc.fillText("Pressione ESC para voltar", 20, 50);

        // Chama o visualizador passando a raiz da AVL
        mapVisualizer.desenharArvoreAVL(gc, placar.getRaiz(), LARGURA);
    }

    public static void main(String[] args) {
        launch();
    }
}
