package org.asteroids.asteroids;
import javafx.scene.image.Image;

public class Assets {
    // Variáveis estáticas para acessar de qualquer lugar
    public static Image nave;
    public static Image asteroide;
    public static Image background;
    public static Image background2;
    public static void carregar() {
        try {
            nave = new Image(Assets.class.getResourceAsStream("/org/asteroids/asteroids/assets/ship_1.png"));
            asteroide = new Image(Assets.class.getResourceAsStream("/org/asteroids/asteroids/assets/meteor.png"));
            background = new Image(Assets.class.getResourceAsStream("/org/asteroids/asteroids/assets/Starry_night_Layer_8.png"));
            background2 = new Image(Assets.class.getResourceAsStream("/org/asteroids/asteroids/assets/orig.png"));

            System.out.println("Imagens carregadas com sucesso!");
        } catch (Exception e) {
            System.err.println("ERRO AO CARREGAR IMAGENS: " + e.getMessage());
            e.printStackTrace(); // Ajuda a achar o erro se o nome estiver errado
        }
    }
}
