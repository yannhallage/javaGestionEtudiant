package com.monprojet.application.controller;

import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class interfaceadministrateuraddclass {
	
	 @FXML
	    private Button buttonajouter;
	    @FXML
	    private Button buttonvider;
	    @FXML
	    private Button buttonquitter; 
	    @FXML
	    private TextField nomclasse_input;
	    @FXML
	    private TextField niveauclasse_input;
	    @FXML
	    private TextField anneeclasse_input;
	    @FXML
	    private TextField module_input;
	    
	    @FXML
	    public void initialize() {
	        // Action pour le bouton "Ajouter"
	        buttonajouter.setOnAction(event -> {
	            // Récupérer les valeurs des champs
	            String nom = nomclasse_input.getText();
	            String niveau = niveauclasse_input.getText();
	            String annee = anneeclasse_input.getText();
	            String prof = module_input.getText();
	            

	            // Valider les données (exemple de validation simple)
	            if (nom.isEmpty() || niveau.isEmpty() || niveau.isEmpty() || prof.isEmpty()) {
	                showAlert("Erreur", "Veuillez remplir tous les champs !");
	            } else {
	                // Afficher les informations dans la console pour le moment
	                
	                System.out.println("Nom: " + nom);
	                System.out.println("niveau de la classe: " + niveau);
	                System.out.println("annee : " + annee);
	                System.out.println("prof assigné : " + prof);
	                
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
	    	nomclasse_input.clear();
        	niveauclasse_input.clear();
        	anneeclasse_input.clear();
        	module_input.clear();
	    }
}

