package com.monprojet.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Charge le fichier FXML à partir du dossier resources/fxml
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/view.fxml"));
            
            // Crée la scène avec la taille spécifiée
            Scene scene = new Scene(root, 650, 400);
            
            // Ajoute le fichier CSS à la scène
            scene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
            
            // Configure la fenêtre principale
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
