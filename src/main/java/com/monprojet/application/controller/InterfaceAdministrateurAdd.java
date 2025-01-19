package com.monprojet.application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InterfaceAdministrateurAdd {
    @FXML
    private Button buttonajouter;
    @FXML
    private Button buttonvider;
    @FXML
    private Button buttonquitter; // Changé de Label en Button
    @FXML
    private DatePicker inputdateinscription;
    @FXML
    private TextField inputmatricule;
    @FXML
    private TextField inputnom;
    @FXML
    private TextField inputprenom;
    @FXML
    private TextField inputclasse;
    @FXML
    private TextField inputniveauclasse;
    
    @FXML
    public void initialize() {
        // Action pour le bouton "Ajouter"
        buttonajouter.setOnAction(event -> {
        	
        	
            // Récupérer les valeurs des champs
            String matricule = inputmatricule.getText();
            String nom = inputnom.getText();
            String prenom = inputprenom.getText();
            String classe = inputclasse.getText();
            String niveauClasse = inputniveauclasse.getText();
            // Utilisation correcte du DatePicker pour obtenir la date
            String dateInscription = inputdateinscription.getValue() != null ? inputdateinscription.getValue().toString() : "Non précisé";

            // Valider les données (exemple de validation simple)
            if (matricule.isEmpty() || nom.isEmpty() || prenom.isEmpty() || classe.isEmpty() || niveauClasse.isEmpty() || dateInscription.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs !");
            } else {
                // Afficher les informations dans la console pour le moment
                System.out.println("Matricule: " + matricule);
                System.out.println("Nom: " + nom);
                System.out.println("Prénom: " + prenom);
                System.out.println("Classe: " + classe);
                System.out.println("Niveau Classe: " + niveauClasse);
                System.out.println("Date d'inscription: " + dateInscription);
                
                // Tu peux remplacer ce code par une logique pour ajouter un étudiant à une base de données
                showAlert("Succès", "Étudiant ajouté avec succès !");
                addelement();
            }
        });

        
        
        // Action pour le bouton "Vider"
        buttonvider.setOnAction(event -> {
        	addelement();
        });

        // Action pour le bouton "Quitter"
        buttonquitter.setOnAction(event -> {
            System.exit(0); // Fermer l'application
        });
    }

    // Méthode utilitaire pour afficher des alertes
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void addelement() {
    	inputmatricule.clear();
        inputnom.clear();
        inputprenom.clear();
        inputclasse.clear();
        inputniveauclasse.clear();
        inputdateinscription.setValue(null);
    }
}
