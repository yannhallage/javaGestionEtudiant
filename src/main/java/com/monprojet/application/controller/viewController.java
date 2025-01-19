package com.monprojet.application.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.IOException;

public class viewController {

    @FXML
    private Hyperlink registerlink;
    @FXML
    private Button buttonlogin;
    @FXML
    private PasswordField motdepasse;
    @FXML
    private TextField matricule;
    @FXML
    public String Admin = "13028404K"; 
    @FXML
    public String mdp = "13028404K";

    public class DatabaseConnection {
        
        public static Connection getConnection() throws SQLException {
            try {
                // Charger le driver MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // URL de la base de données, à adapter selon votre configuration
                String url = "jdbc:mysql://localhost:3306/gestionabsenceetudiant"; // Exemple d'URL
                String user = "root";  // Remplacez par votre nom d'utilisateur MySQL
                String password = "root";  // Remplacez par votre mot de passe MySQL
                
                // Retourner la connexion
                return DriverManager.getConnection(url, user, password);
                
            } catch (ClassNotFoundException e) {
                // Si le driver n'est pas trouvé
                e.printStackTrace();
                throw new SQLException("Driver JDBC MySQL non trouvé");
            }
        }
    }
    private boolean testDatabaseConnection() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connexion à la base de données réussie !");
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @FXML
    public void initialize() {
        // Ajouter l'action au lien
        registerlink.setOnAction(event -> openWindowInscription(registerlink));
        buttonlogin.setOnAction(event -> actionButton(buttonlogin));
    }

    // Méthode pour ouvrir la fenêtre d'inscription
    private void openWindowInscription(Hyperlink hyperlink) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/windowInscription.fxml"));
            StackPane root = loader.load();

            Stage currentStage = (Stage) hyperlink.getScene().getWindow();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Inscription");
            stage.setResizable(false);
            stage.setOnCloseRequest(event -> currentStage.show());

            currentStage.hide();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openWindowInscription() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/InterfaceAdministrateur.fxml"));
            StackPane root = loader.load();

            Stage currentStage = (Stage) matricule.getScene().getWindow();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Inscription");
            stage.setResizable(false);
            stage.setOnCloseRequest(event -> currentStage.show());

            currentStage.hide();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void actionButton(Button buttonlogin) {
        if (matricule.getText().isEmpty() || motdepasse.getText().isEmpty()) {
            showErrorAlertEmpty();
        } else if (matricule.getText().length() != 9) {
            showErrorAlertLenght();
        } else {
            // Tester la connexion à la base de données
            if (testDatabaseConnection()) {
                System.out.println("Connexion à la base de données réussie");
                // Vérification des informations d'identification
                if (matricule.getText().equals(Admin) && motdepasse.getText().equals(mdp)) {
                    openWindowInscription();  // Accéder à la page d'inscription si l'utilisateur est valide
                } else {
                    showErrorAlertmdp(); // Mot de passe incorrect
                }
            } else {
                System.out.println("Erreur de connexion à la base de données");
                showErrorAlert();  // Alerte de connexion échouée
            }
        }
    }

    public void showErrorAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Une erreur s'est produite");
        alert.setContentText("Détails de l'erreur : Connexion à la base de données échouée.");
        alert.showAndWait();
    }

    public void showErrorAlertEmpty() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Une erreur s'est produite");
        alert.setContentText("Détails de l'erreur : Champ vide !");
        alert.showAndWait();
    }

    public void showErrorAlertLenght() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Une erreur s'est produite");
        alert.setContentText("Détails de l'erreur : La taille du matricule doit être égale à 9.");
        alert.showAndWait();
    }

    public void showErrorAlertmdp() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Une erreur s'est produite");
        alert.setContentText("Détails de l'erreur : Mot de passe incorrect !");
        alert.showAndWait();
    }
}
