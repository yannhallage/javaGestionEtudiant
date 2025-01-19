package com.monprojet.application.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class InterfaceAdministrateur {

    // Méthode générique pour ouvrir une nouvelle fenêtre
 private void openWindow(String fxmlFile, MouseEvent event) {
    try {
        // Charger le FXML pour la nouvelle fenêtre
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Pane root = loader.load();  // Utilisation de Pane au lieu de StackPane

        // Obtenir le stage actuel depuis l'événement de clic
        Stage currentStage = (Stage) ((Pane) event.getSource()).getScene().getWindow();

        // Créer une nouvelle scène et un nouveau stage
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Inscription");
        stage.setResizable(false);

        // Ajouter un événement pour réafficher la première fenêtre lorsque la seconde se ferme
        stage.setOnCloseRequest(e -> currentStage.show());

        // Cacher la première fenêtre avant d'afficher la deuxième
        currentStage.hide();

        // Afficher la nouvelle fenêtre
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    // Méthode pour gérer le clic sur le panneau "Ajouter un étudiant"
    @FXML
    private void handleAddEtudiantClick(MouseEvent event) {
        openWindow("/fxml/InterfaceAdministrateurAdd.fxml", event);
    }

    // Méthode pour gérer le clic sur le panneau "Ajouter un enseignant"
    @FXML
    private void handleAddEnseignantClick(MouseEvent event) {
        openWindow("/fxml/InterfaceAdministrateurAddenseign.fxml", event);
    }

    // Méthode pour gérer le clic sur le panneau "Ajouter une classe"
    @FXML
    private void handleAddClasseClick(MouseEvent event) {
        openWindow("/fxml/InterfaceAdministrateurAddclass.fxml", event);
    }

    // Méthode pour gérer le clic sur le panneau "Ajouter un module"
    @FXML
    private void handleAddModuleClick(MouseEvent event) {
        openWindow("/fxml/InterfaceAdministrateurAddmodule.fxml", event);
    }
}
