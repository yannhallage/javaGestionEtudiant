package com.monprojet.application.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class windowInscription {

    @FXML
    private TextField nomInscription;
    @FXML
    private TextField matriculeInscription;
    @FXML
    private TextField prenomInscription;
    @FXML
    private TextField emailInscription;
    @FXML
    private PasswordField motdepasseinscription;
    @FXML
    private PasswordField motdepasseinscription2;
    @FXML
    private TextField inscriptionEntantQue;
    @FXML
    private Button buttonInscription;

    @FXML
    public void initialize() {
        buttonInscription.setOnAction(event -> {
        	/*verificaiton au niveau des champs */
        	
        	if (matriculeInscription.getText().isEmpty() || inscriptionEntantQue.getText().isEmpty() || nomInscription.getText().isEmpty() || prenomInscription.getText().isEmpty() || emailInscription.getText().isEmpty() || motdepasseinscription.getText().isEmpty() || motdepasseinscription2.getText().isEmpty()) {
        		showErrorAlertChampVide();
        	}else {
        		if (matriculeInscription.getText().length() != 9) {
        			showErrorAlertMatricule();
        		}else {
        			
        			System.out.println("okay");
        		}
        	}
        });
    }
    
    public void another() {
    	buttonInscription.setOnAction(event -> {
    		/*System.out.println("lancer");*/
    		System.out.println("lancer interface etudiant");
    	});
    }

    private void openDashboard() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/fxml/InterfaceEtudiant.fxml"));
        AnchorPane root = loader.load();

        // Récupération du Stage actuel
        Stage currentStage = (Stage) buttonInscription.getScene().getWindow();

        // Création de la nouvelle fenêtre
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Dashboard Étudiant");
        stage.setResizable(false);

        // Masquer la fenêtre actuelle
        currentStage.hide();

        // Afficher la nouvelle fenêtre
        stage.show();

        // Réafficher la fenêtre actuelle si la nouvelle fenêtre est fermée
        stage.setOnCloseRequest(event -> currentStage.show());

    } catch (IOException e) {
        e.printStackTrace();
    }
}



	public void showErrorAlertChampVide() {
	    // Créer une alerte de type Erreur
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Erreur");
	    alert.setHeaderText("Une erreur s'est produite");
	    alert.setContentText("Détails de l'erreur : champs vide !");
	
	    // Afficher l'alerte
	    alert.showAndWait();
	}
	public void showErrorAlertMatricule() {
	    // Créer une alerte de type Erreur
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Erreur");
	    alert.setHeaderText("Une erreur s'est produite");
	    alert.setContentText("Détails de l'erreur : matricule doit etre egale a 9 !");
	
	    // Afficher l'alerte
	    alert.showAndWait();
	}
	public void showErrorAlertType() {
	    // Créer une alerte de type Erreur
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Erreur");
	    alert.setHeaderText("Une erreur s'est produite");
	    alert.setContentText("Détails de l'erreur : vous devez entree un type existant!");
	
	    // Afficher l'alerte
	    alert.showAndWait();
	}
}