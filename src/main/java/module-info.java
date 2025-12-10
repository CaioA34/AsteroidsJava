module org.asteroids.asteroids {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.asteroids.asteroids to javafx.fxml;
    exports org.asteroids.asteroids;
}