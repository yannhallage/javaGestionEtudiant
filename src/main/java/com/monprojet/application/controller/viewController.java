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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

  
    private void actionButton(Button buttonlogin) {
        if (matricule.getText().isEmpty() || motdepasse.getText().isEmpty()) {
            showErrorAlertEmpty();
        } else if (matricule.getText().length() != 9) {
            showErrorAlertLenght();
        } else {
            if (testDatabaseConnection()) {
                System.out.println("Connexion à la base de données réussie");

                // Vérifier les informations dans la base de données
                String typeuser = validateCredentials(matricule.getText(), motdepasse.getText());
                if (typeuser != null) {
                    // Rediriger vers la fenêtre en fonction du type d'utilisateur
                    switch (typeuser.toLowerCase()) {
                        case "etudiant":
                            openWindow("/fxml/InterfaceEtudiants.fxml", "Interface Étudiant");
                            break;
                        case "enseignant":
                            openWindow("/fxml/InterfaceEnseignantClass.fxml", "Interface Enseignant");
                            break;
                        case "admin":
                            openWindow("/fxml/InterfaceAdministrateur.fxml", "Interface Administrateur");
                            break;
                        default:
                            showErrorAlertUnknownUserType();
                    }
                } else {
                    showErrorAlertmdp(); // Matricule ou mot de passe incorrect
                }
            } else {
                showErrorAlert(); // Erreur de connexion à la base de données
            }
        }
    }

    // Méthode pour valider les informations dans la base de données
    private String validateCredentials(String matricule, String password) {
        String typeuser = null;
        String query = "SELECT typeuser FROM compte_utilisateur WHERE matricule = ? AND mot_de_passe = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, matricule);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                typeuser = rs.getString("typeuser"); // Récupérer le type d'utilisateur
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return typeuser;
    }

    // Méthode générique pour ouvrir une nouvelle fenêtre
    private void openWindow(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            StackPane root = loader.load();

            Stage currentStage = (Stage) matricule.getScene().getWindow();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(false);
            stage.setOnCloseRequest(event -> currentStage.show());

            currentStage.hide();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Alerte pour un type d'utilisateur inconnu
    private void showErrorAlertUnknownUserType() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText("Type d'utilisateur inconnu");
        alert.setContentText("Le type d'utilisateur fourni n'est pas valide. Contactez l'administrateur.");
        alert.showAndWait();
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
