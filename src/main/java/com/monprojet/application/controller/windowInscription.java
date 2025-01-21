package com.monprojet.application.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.monprojet.application.controller.viewController.DatabaseConnection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
            if (validateFields()) {
                if (registerUser()) {
                	openLoginPage();
                }
            }
        });
    }

    private boolean validateFields() {
        // Vérification des champs vides
        if (matriculeInscription.getText().isEmpty() || inscriptionEntantQue.getText().isEmpty() ||
                nomInscription.getText().isEmpty() || prenomInscription.getText().isEmpty() ||
                emailInscription.getText().isEmpty() || motdepasseinscription.getText().isEmpty() ||
                motdepasseinscription2.getText().isEmpty()) {
            showErrorAlert("Erreur", "Champ(s) vide(s)", "Veuillez remplir tous les champs.");
            return false;
        }

        // Vérification de la longueur du matricule
        if (matriculeInscription.getText().length() != 9) {
            showErrorAlert("Erreur", "Matricule invalide", "Le matricule doit contenir exactement 9 caractères.");
            return false;
        }

        // Vérification des mots de passe
        if (!motdepasseinscription.getText().equals(motdepasseinscription2.getText())) {
            showErrorAlert("Erreur", "Mots de passe non correspondants", "Les mots de passe ne correspondent pas.");
            return false;
        }

        // Vérification du type d'utilisateur
        String type = inscriptionEntantQue.getText().toLowerCase();
        if (!type.equals("etudiant") && !type.equals("enseignant") && !type.equals("admin")) {
            showErrorAlert("Erreur", "Type d'utilisateur invalide", "Le type doit être 'etudiant', 'enseignant' ou 'admin'.");
            return false;
        }

        return true;
    }

   /* private boolean registerUser() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Vérifie si le matricule existe dans la table "etudiant"
            String checkQuery = "SELECT COUNT(*) FROM etudiant WHERE matricule = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
            checkStatement.setString(1, matriculeInscription.getText());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                // Si le matricule est connu, on procède à l'inscription
                String insertQuery = "INSERT INTO compte_utilisateur (matricule, nom, prenom, email, mot_de_passe, typeuser) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                insertStatement.setString(1, matriculeInscription.getText());
                insertStatement.setString(2, nomInscription.getText());
                insertStatement.setString(3, prenomInscription.getText());
                insertStatement.setString(4, emailInscription.getText());
                insertStatement.setString(5, motdepasseinscription.getText());
                insertStatement.setString(6, inscriptionEntantQue.getText().toLowerCase());

                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    showInfoAlert("Succès", "Inscription réussie", "L'utilisateur a été enregistré avec succès.");
                    
                    return true;
                }
            } else {
                // Si le matricule n'existe pas, on affiche une erreur
                showErrorAlert("Erreur", "Matricule inconnu", "Le matricule n'existe pas dans la table des étudiants.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Problème de base de données", "Impossible d'enregistrer l'utilisateur.");
        }
        return false;
    }
    */

    private boolean registerUser() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Vérifie si le matricule existe dans la table "etudiant"
            String checkQueryEtudiant = "SELECT COUNT(*) FROM etudiant WHERE matricule = ?";
            PreparedStatement checkStatementEtudiant = conn.prepareStatement(checkQueryEtudiant);
            checkStatementEtudiant.setString(1, matriculeInscription.getText());
            ResultSet resultSetEtudiant = checkStatementEtudiant.executeQuery();

            if (resultSetEtudiant.next() && resultSetEtudiant.getInt(1) > 0) {
                // Si le matricule existe dans la table "etudiant", on procède à l'inscription
            	String insertQuery = "INSERT INTO compte_utilisateur (matricule, nom, prenom, email, mot_de_passe, typeuser) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                insertStatement.setString(1, matriculeInscription.getText());
                insertStatement.setString(2, nomInscription.getText());
                insertStatement.setString(3, prenomInscription.getText());
                insertStatement.setString(4, emailInscription.getText());
                insertStatement.setString(5, motdepasseinscription.getText());
                insertStatement.setString(6, inscriptionEntantQue.getText().toLowerCase());

                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    showInfoAlert("Succès", "Inscription réussie", "L'utilisateur a été enregistré avec succès.");
                    
                    return true;
                }
                return insertUser(conn);
            } else {
                // Si le matricule n'existe pas dans la table "etudiant", on vérifie la table "enseignant"
                String checkQueryEnseignant = "SELECT COUNT(*) FROM enseignant WHERE matricule = ?";
                PreparedStatement checkStatementEnseignant = conn.prepareStatement(checkQueryEnseignant);
                checkStatementEnseignant.setString(1, matriculeInscription.getText());
                ResultSet resultSetEnseignant = checkStatementEnseignant.executeQuery();

                if (resultSetEnseignant.next() && resultSetEnseignant.getInt(1) > 0) {
                    // Si le matricule existe dans la table "enseignant", on procède à l'inscription
                	String insertQuery = "INSERT INTO compte_utilisateur (matricule, nom, prenom, email, mot_de_passe, typeuser) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
                    insertStatement.setString(1, matriculeInscription.getText());
                    insertStatement.setString(2, nomInscription.getText());
                    insertStatement.setString(3, prenomInscription.getText());
                    insertStatement.setString(4, emailInscription.getText());
                    insertStatement.setString(5, motdepasseinscription.getText());
                    insertStatement.setString(6, inscriptionEntantQue.getText().toLowerCase());

                    int rowsInserted = insertStatement.executeUpdate();
                    if (rowsInserted > 0) {
                        showInfoAlert("Succès", "Inscription réussie", "L'utilisateur a été enregistré avec succès.");
                        
                        return true;
                    }
                    return insertUser(conn);
                } else {
                    // Si le matricule n'existe ni dans la table "etudiant" ni dans la table "enseignant"
                    showErrorAlert("Erreur", "Matricule inconnu", "Le matricule n'existe ni dans la table des étudiants ni dans celle des enseignants.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Problème de base de données", "Impossible d'enregistrer l'utilisateur.");
        }
        return false;
    }

    private boolean insertUser(Connection conn) throws SQLException {
        // Inscription de l'utilisateur dans la table "compte_utilisateur"
        String insertQuery = "INSERT INTO compte_utilisateur (matricule, nom, prenom, email, mot_de_passe, typeuser) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement insertStatement = conn.prepareStatement(insertQuery);
        insertStatement.setString(1, matriculeInscription.getText());
        insertStatement.setString(2, nomInscription.getText());
        insertStatement.setString(3, prenomInscription.getText());
        insertStatement.setString(4, emailInscription.getText());
        insertStatement.setString(5, motdepasseinscription.getText());
        insertStatement.setString(6, inscriptionEntantQue.getText().toLowerCase());

        int rowsInserted = insertStatement.executeUpdate();
        if (rowsInserted > 0) {
            showInfoAlert("Succès", "Inscription réussie", "L'utilisateur a été enregistré avec succès.");
            return true;
        }
        return false;
    }


    private void openLoginPage() {
        try {
            // Charge le fichier FXML pour la page de connexion
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/view.fxml"));
            StackPane root = loader.load();

            // Récupération de la fenêtre actuelle
            Stage currentStage = (Stage) buttonInscription.getScene().getWindow();

            // Création de la nouvelle scène
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
            currentStage.setTitle("Page de Connexion");
            currentStage.setResizable(false);

            // Affiche la page de connexion
            currentStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showErrorAlert("Erreur", "Impossible de charger la page de connexion.");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showErrorAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showInfoAlert(String title, String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
