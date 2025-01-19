package com.monprojet.application.controller;

import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class interfaceadministrateuradden {

	 @FXML
	    private Button buttonajouter;
	    @FXML
	    private Button buttonvider;
	    @FXML
	    private Button buttonquitter; // Changé de Label en Button
	    @FXML
	    private  TextField mailEN;
	    @FXML
	    private  TextField telephone;
	    @FXML
	    private TextField matriculeENS;
	    @FXML
	    private TextField nomEN;
	    @FXML
	    private TextField prenomEN;
	 
	    
	    
	    @FXML
	    public void initialize() {
		        // Action pour le bouton "Ajouter"
		        buttonajouter.setOnAction(event -> {
		        	String nom = nomEN.getText();
		        	String matricule = matriculeENS.getText();
		        	String prenom = prenomEN.getText();
		        	
		            String mail = mailEN.getText();
		            String numero_tel = telephone.getText();
		            
		            if (matricule.isEmpty() || nom.isEmpty() || prenom.isEmpty() || mail.isEmpty() || numero_tel.isEmpty()) {
		                showAlert("Erreur", "Veuillez remplir tous les champs !");
		            } else {
		                // Afficher les informations dans la console pour le moment
		                System.out.println("Matricule: " + matricule);
		                System.out.println("Nom: " + nom);
		                System.out.println("Prénom: " + prenom);
		                System.out.println("mail: " + mail);
		                System.out.println("Niveau Classe: " + numero_tel);
		                /*System.out.println("Date d'inscription: " + dateInscription);*/
		                
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
	    	 // Vider les champs
	    	matriculeENS.clear();
        	mailEN.clear();
        	nomEN.clear();
        	prenomEN.clear();
            telephone.clear();
            
	    }
	   

}
