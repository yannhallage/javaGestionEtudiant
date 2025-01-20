package com.monprojet.application.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.monprojet.application.controller.interfaceadministrateuraddclass.DatabaseConnection;

import com.monprojet.application.model.Enseignant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

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
	    private TableView<Enseignant> tableView;
	    @FXML
	    private TableColumn<Enseignant, String> matriculeenseignant;
	    @FXML
	    private TableColumn<Enseignant, String> nomenseignant;
	    @FXML
	    private TableColumn<Enseignant, String> prenomenseignant;
	    @FXML
	    private TableColumn<Enseignant, String> emailenseignant;
	    @FXML
	    private TableColumn<Enseignant, String> telephoneenseignant;

	    private ObservableList<Enseignant> enseignantList = FXCollections.observableArrayList();
	    
	    
	    @FXML
	    public void initialize() {
	    	
	    	
	    	// Initialiser les colonnes du TableView
	        matriculeenseignant.setCellValueFactory(new PropertyValueFactory<>("matricule"));
	        nomenseignant.setCellValueFactory(new PropertyValueFactory<>("nom"));
	        prenomenseignant.setCellValueFactory(new PropertyValueFactory<>("prenom"));
	        emailenseignant.setCellValueFactory(new PropertyValueFactory<>("email"));
	        telephoneenseignant.setCellValueFactory(new PropertyValueFactory<>("numTel"));
	        
	        loadENS();
	        
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
		            	if (matricule.length() != 9) {
		                    showAlert("Erreur", "La taille du matricule doit absolument être de 9 caractères !");
		                    clearelement(); // Vider les champs
		                } else {
		                	ajouterENS(nom,matricule,prenom, numero_tel ,mail);
			                loadENS(); // Recharger les classes dans la table
			                clearelement(); // Vider les champs
		                }
		                
		            }
	        });

	        // Action pour le bouton "Vider"
	        buttonvider.setOnAction(event -> {
	           clearelement(); 	
	        });

	        // Action pour le bouton "Quitter"
	        buttonquitter.setOnAction(event -> {
	            System.exit(0); // Fermer l'application
	        });
	    }
	    
	    
	 // Méthode pour ajouter une classe dans la base de données
	    private void ajouterENS(String matricule ,String nom , String prenom,String num_tel, String mail) {
	        String sql = "INSERT INTO enseignant (matricule , nom , prenom , num_tel , email) VALUES (?, ?, ?, ?, ?)";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            pstmt.setString(1, matricule);
	            pstmt.setString(2, nom);
	            pstmt.setString(3, prenom);
	            pstmt.setString(4, num_tel);
	            pstmt.setString(5, mail);

	            int rowsAffected = pstmt.executeUpdate();
	            if (rowsAffected > 0) {
	                showAlert("Succès", "Classe ajoutée avec succès à la base de données !");
	            } else {
	                showAlert("Erreur", "Une erreur s'est produite lors de l'ajout à la base de données.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Erreur", "Impossible d'ajouter la classe : " + e.getMessage());
	        }
	    }

	 // Méthode pour charger les classes depuis la base de données
	    private void loadENS() {
	        String sql = "SELECT * FROM enseignant";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql);
	             ResultSet rs = pstmt.executeQuery()) {

	        	enseignantList.clear(); // Vider la liste avant de la remplir

	            while (rs.next()) {
	                String matricule = rs.getString("matricule");
	                String nom = rs.getString("nom");
	                String prenom = rs.getString("prenom");
	                String num_tel = rs.getString("num_tel");
	                String mail = rs.getString("email");

	                Enseignant EN = new Enseignant(matricule, nom,prenom, num_tel, mail);
	                enseignantList.add(EN); // Ajouter la classe à la liste
	            }

	            // Mettre à jour la table avec la nouvelle liste
	            tableView.setItems(enseignantList);

	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Erreur", "Impossible de charger les classes : " + e.getMessage());
	        }
	    }
	    
	    
	    // Méthode utilitaire pour afficher des alertes
	    private void showAlert(String title, String message) {
	        Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle(title);
	        alert.setHeaderText(null);
	        alert.setContentText(message);
	        alert.showAndWait();
	    }
	    
	    private void clearelement() {
	    	 // Vider les champs
	    	matriculeENS.clear();
        	mailEN.clear();
        	nomEN.clear();
        	prenomEN.clear();
            telephone.clear();
            
	    }
	   

}
