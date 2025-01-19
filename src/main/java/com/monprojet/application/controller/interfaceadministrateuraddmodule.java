package com.monprojet.application.controller;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import javafx.scene.control.Label;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class interfaceadministrateuraddmodule {

	   @FXML
	    private Button buttonajouter;
	    @FXML
	    private Button buttonvider;
	    @FXML
	    private Button buttonquitter; // Changé de Label en Button
	   
	    @FXML
	    private TextField nomModule;
	    @FXML
	    private TextField ecueModule;
	    @FXML
	    private TextField heurModule;
	    @FXML
	    private TextField dispenseENmodule;
	    @FXML
	    private TextField classeModule;
	    
	    @FXML
	    public void initialize() {
	        // Action pour le bouton "Ajouter"
	        buttonajouter.setOnAction(event -> {
	            // Récupérer les valeurs des champs
	            String nommodule = nomModule.getText();
	            String ecuemodule = ecueModule.getText();
	            String heurmodule = heurModule.getText();
	            String dispenseenmodule = dispenseENmodule.getText();
	            String classemodule = classeModule.getText();
	         
	            // Valider les données (exemple de validation simple)
	            if (nommodule.isEmpty() || ecuemodule.isEmpty() || heurmodule.isEmpty() || dispenseenmodule.isEmpty() || classemodule.isEmpty()) {
	                showAlert("Erreur", "Veuillez remplir tous les champs !");
	            } else {
	                // Afficher les informations dans la console pour le moment
	                System.out.println("Matricule: " + nommodule);
	                System.out.println("Nom: " + ecuemodule);
	                System.out.println("Prénom: " + heurmodule);
	                System.out.println("Classe: " + classemodule);
	                System.out.println("Niveau Classe: " + dispenseENmodule);
	                
	                // Tu peux remplacer ce code par une logique pour ajouter un étudiant à une base de données
	                showAlert("Succès", "Étudiant ajouté avec succès !");
	                addelement();
	            }
	        });

	     // Action pour le bouton "Vider"
	        buttonvider.setOnAction(event -> {
	            // Vider les champs
	        	addelement();
	        });

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
	        
	        // Méthode utilitaire pour afficher des alertes
	        private void addelement() {
	        	nomModule.clear();
	        	ecueModule.clear();
	        	heurModule.clear();
	        	dispenseENmodule.clear();
	        	classeModule.clear();	
	        
	    }
}
