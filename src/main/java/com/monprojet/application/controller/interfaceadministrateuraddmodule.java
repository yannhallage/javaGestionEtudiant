package com.monprojet.application.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import com.monprojet.application.controller.interfaceadministrateuraddclass.DatabaseConnection;
import com.monprojet.application.model.Module;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;


public class interfaceadministrateuraddmodule {

	   @FXML
	    private Button buttonajouter;
	    @FXML
	    private Button buttonvider;
	    @FXML
	    private Button buttonSupprimer_module;
	    @FXML
	    private TextField codeModule_supprimer;
	    @FXML
	    private Button buttonquitter; // Changé de Label en Button
	   
	    @FXML
	    private TextField nomModule;
	    @FXML
	    private TextField codeModule;
	    @FXML
	    private TextField ecueModule;
	    @FXML
	    private TextField heurModule;
	    @FXML
	    private TextField dispenseENmodule;
	    @FXML
	    private TextField classeModule;
	    @FXML
	    private TextField niveauM;
	    @FXML
	    private Label adminName;
	    
	    @FXML
	    private TableView<Module> tableView;
	    
	    @FXML
	    private TableColumn<Module ,String> ecuemodule;

	    @FXML
	    private TableColumn<Module ,String> enseignantmodule;
	    @FXML
	    private TableColumn<Module ,String> classedispensemodule;
	    @FXML
	    private TableColumn<Module ,String> heurmodule;

	    @FXML
	    private TableColumn<Module ,String> idmodule;
	    @FXML
	    private TableColumn<Module ,String> niveau;

	    @FXML
	    private TableColumn<Module ,String> nommodule;
	    
	    private ObservableList<Module> ModuleList = FXCollections.observableArrayList();
	    
	    @FXML
	    public void initialize() {
	    	
	    	// Initialiser les colonnes du TableView
	        nommodule.setCellValueFactory(new PropertyValueFactory<>("nomDuModule"));
	        ecuemodule.setCellValueFactory(new PropertyValueFactory<>("ecueModule"));
	        heurmodule.setCellValueFactory(new PropertyValueFactory<>("heurModule"));
	        enseignantmodule.setCellValueFactory(new PropertyValueFactory<>("dispenseENModule"));
	        classedispensemodule.setCellValueFactory(new PropertyValueFactory<>("classeModule"));
	        idmodule.setCellValueFactory(new PropertyValueFactory<>("idModule"));
	        niveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));
	        
	        Preferences prefs = Preferences.userNodeForPackage(getClass());
	        
	        
	        adminName.setText(prefs.get("nomPrenom", "default"));
	        
	        loadModule();
	        
	        // Action pour le bouton "Ajouter"
	        buttonajouter.setOnAction(event -> {
	            // Récupérer les valeurs des champs
	            String nommodule = nomModule.getText();
	            String ecuemodule = ecueModule.getText();
	            String heurmodule = heurModule.getText();
	            String dispenseenmodule = dispenseENmodule.getText();
	            String classemodule = classeModule.getText();
	            String idmodule = codeModule.getText();
	            String niveau = niveauM.getText();
	         
	            // Valider les données (exemple de validation simple)
	            if (nommodule.isEmpty() || ecuemodule.isEmpty() || heurmodule.isEmpty() || dispenseenmodule.isEmpty() || classemodule.isEmpty() || niveau.isEmpty()) {
	                showAlert("Erreur", "Veuillez remplir tous les champs !");
	            } else {
	                
	            	ajouterModule(idmodule,nommodule,ecuemodule,heurmodule,dispenseenmodule,classemodule,niveau);
	                loadModule(); // Recharger les classes dans la table
	                clearelement(); // Vider les champs
	            }
	        });

	        //action pour supprimer le module 
	        buttonSupprimer_module.setOnAction(event -> {
	            // Récupérer les valeurs des champs
	            String codeModule = codeModule_supprimer.getText();
	           
	            // Valider les données (exemple de validation simple)
	            if (codeModule.isEmpty()) {
	                showAlert("Erreur", "Veuillez remplir tous les champs !");
	            } else {
	                    // Ajouter l'étudiant à la base de données
	                    supprimerModule(codeModule);
	                    loadModule(); // Recharger les étudiants dans la table
	                    clearelement(); // Vider les champs
	                }
	
	        });
	     // Action pour le bouton "Vider"
	        buttonvider.setOnAction(event -> {
	            // Vider les champs
	        	clearelement();
	        });

	        buttonquitter.setOnAction(event -> {
	            System.exit(0); // Fermer l'application
	        });
	    }
	        
	    
	    // Méthode pour ajouter une classe dans la base de données
	    private void ajouterModule(String idmodule ,String nommodule , String ecue,String heurmodule, String enseignantmodule , String classedispensemodule ,String niveau) {
	    	 String idENS = getIdENS(enseignantmodule); // Vérifier si l'enseignant qui dispense ce module existe
	    	 String CLASS = getClass(classedispensemodule,niveau);
	    	 
	    	 if (idENS == null) {
	    		 showAlert("Erreur", "L'enseignant avec le matricule '" + enseignantmodule + "' n'existe pas dans la base de données.");
	             return;
	         }
	    	 if (CLASS == null) {
	    		 showAlert("Erreur", "La classe '" + classedispensemodule + "' n'existe pas dans la base de données.");
	             return; 
	    	 }
	    	 
	        String sql = "INSERT INTO module (code , intitule , ecue , heurmodule, enseignantmodule , classedispensemodule,niveau, id_enseig) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            pstmt.setString(1, idmodule);
	            pstmt.setString(2, nommodule);
	            pstmt.setString(3, ecue);
	            pstmt.setString(4, heurmodule);
	            pstmt.setString(5, enseignantmodule);
	            pstmt.setString(6, CLASS);
	            pstmt.setString(7, niveau);
	            pstmt.setString(8, idENS);

	            int rowsAffected = pstmt.executeUpdate();
	            if (rowsAffected > 0) {
	                showAlert("Succès", "Module ajoutée avec succès à la base de données !");
	            } else {
	                showAlert("Erreur", "Une erreur s'est produite lors de l'ajout à la base de données.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Erreur", "Impossible d'ajouter le Module : " + e.getMessage());
	        }
	    }
	    
	    
	 // Méthode pour vérifier si la classe existe dans la base de données
	    private String getIdENS(String nomEN) {
	        String sql = "SELECT num FROM enseignant WHERE matricule = ?";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            pstmt.setString(1, nomEN);
	            ResultSet rs = pstmt.executeQuery();

	            if (rs.next()) {
	                return rs.getString("num"); // Retourne l'identifiant si la classe existe
	            } else {
	                return null; // Retourne null si la classe n'existe pas
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Erreur", "Erreur lors de la vérification de la classe : " + e.getMessage());
	            return null;
	        }
	    }
	    
	    private String getClass(String specialite, String niveau) {
	        String sql = "SELECT specialite FROM classe WHERE specialite = ? AND niveau = ?";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            // Assigner les paramètres à la requête préparée
	            pstmt.setString(1, specialite);
	            pstmt.setString(2, niveau);

	            // Exécuter la requête
	            ResultSet rs = pstmt.executeQuery();

	            // Vérifier le résultat
	            if (rs.next()) {
	                return rs.getString("specialite"); // Retourne la spécialité si trouvée
	            } else {
	                return null; // Retourne null si aucun résultat
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Erreur", "Erreur lors de la vérification de la classe : " + e.getMessage());
	            return null;
	        }
	    }

	    // Méthode pour charger les classes depuis la base de données
	    private void loadModule() {
	        String sql = "SELECT * FROM module";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql);
	             ResultSet rs = pstmt.executeQuery()) {

	        	ModuleList.clear(); // Vider la liste avant de la remplir

	            while (rs.next()) {
	                
	                String nomDuModule = rs.getString("intitule");
	                String ecueModule = rs.getString("ecue");
	                String heurModule = rs.getString("heurmodule");
	                String dispenseENModule = rs.getString("enseignantmodule");
	                String classeModule = rs.getString("classedispensemodule");
	                String idModule = rs.getString("code");
	                String niveau = rs.getString("niveau");
	                
	                Module module = new Module(nomDuModule,ecueModule,heurModule,dispenseENModule,classeModule,idModule,niveau);
	                ModuleList.add(module); // Ajouter la classe à la liste
	            }

	            // Mettre à jour la table avec la nouvelle liste
	            tableView.setItems(ModuleList);

	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Erreur", "Impossible de charger les classes : " + e.getMessage());
	        }
	    }
	    
	    
	    //methode pour supprimer un module 
	    private void supprimerModule(String code) {
	        String sql = "DELETE FROM module WHERE code = ?";
	        try (Connection conn = DatabaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            pstmt.setString(1, code);

	            int rowsAffected = pstmt.executeUpdate();
	            if (rowsAffected > 0) {
	                showAlert("Succès", "Module supprimé avec succès de la base de données !");
	                codeModule_supprimer.clear();
	            } else {
	                showAlert("Information", "Aucun module trouvé avec le matricule spécifié.");
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Erreur", "Une erreur s'est produite lors de la suppression : " + e.getMessage());
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
	        
	        // Méthode utilitaire pour afficher des alertes
	        private void clearelement() {
	        	nomModule.clear();
	        	ecueModule.clear();
	        	heurModule.clear();
	        	dispenseENmodule.clear();
	        	classeModule.clear();
	        	niveauM.clear();
	        
	    }
}
