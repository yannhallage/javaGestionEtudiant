package com.monprojet.application.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import com.monprojet.application.controller.InterfaceAdministrateurAdd.DatabaseConnection;
import com.monprojet.application.model.Etudiant;
import com.monprojet.application.model.UserAccount;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

public class interfaceCompt_user {

    @FXML
    private Label adminName;

    @FXML
    private Button buttonSupprimer;

    @FXML
    private Button buttonquitter;

    @FXML
    private TableColumn<UserAccount, String> date_user;

    @FXML
    private TextField deletedEtudiant;

    @FXML
    private TableColumn<UserAccount, String> mail_user;

    @FXML
    private TableColumn<UserAccount, String> matricule_user;
    @FXML
    private Label nbr_admin;
    
    @FXML
    private Label nbr_user;

    @FXML
    private Label nbr_Enseignant;

    @FXML
    private Label nbr_Etudiant;

    @FXML
    private TableColumn<UserAccount, String> nom_user;

    @FXML
    private TableColumn<UserAccount, String> prenom_user;

    @FXML
    private TableColumn<UserAccount, String> role_user;

    @FXML
    private TableView<UserAccount> tableView;

    @FXML
    private TableColumn<UserAccount, String> type_user;
    
    private ObservableList<UserAccount> useraccount = FXCollections.observableArrayList();
    
    
    // Connection à la base de données
    public class DatabaseConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/gestionabsenceetudiant";
        private static final String USER = "root";
        private static final String PASSWORD = "root";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }
    
    
    @FXML
    public void initialize() {
        // Initialiser les colonnes du TableView
    	matricule_user.setCellValueFactory(new PropertyValueFactory<>("matricule_user"));
    	nom_user.setCellValueFactory(new PropertyValueFactory<>("nom_user"));
    	prenom_user.setCellValueFactory(new PropertyValueFactory<>("prenom_user"));
    	mail_user.setCellValueFactory(new PropertyValueFactory<>("mail_user"));
    	date_user.setCellValueFactory(new PropertyValueFactory<>("date_user"));
    	type_user.setCellValueFactory(new PropertyValueFactory<>("type_user"));
    	role_user.setCellValueFactory(new PropertyValueFactory<>("role_user"));
    	loadnombre();
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        
        
        adminName.setText(prefs.get("nomPrenom", "default"));
        
        // Charger les données des étudiants depuis la base de données
        loadEtudiants();
        
        
        
        // Action pour le bouton "Ajouter"
        buttonSupprimer.setOnAction(event -> {
            // Récupérer les valeurs des champs
            String matricule = deletedEtudiant.getText();
           
            // Valider les données (exemple de validation simple)
            if (matricule.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs !");
            } else {
                if (matricule.length() != 9) {
                    showAlert("Erreur", "La taille du matricule doit absolument être de 9 caractères !");
                } else {
                    // Ajouter l'étudiant à la base de données
                    supprimerEtudiant(matricule);
                    loadEtudiants(); // Recharger les étudiants dans la table
                    clearelement(); // Vider les champs
                }
            }
        });

        // Action pour le bouton "Vider"
        //buttonvider.setOnAction(event -> clearelement());

        // Action pour le bouton "Quitter"
        buttonquitter.setOnAction(event -> System.exit(0)); // Fermer l'application
    }

    private void loadEtudiants() {
        String sql = "SELECT * FROM compte_utilisateur";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

        	useraccount.clear(); // Vider la liste avant de la remplir

            while (rs.next()) {
                String matricule_user = rs.getString("matricule");
                String nom_user = rs.getString("nom");
                String prenom_user = rs.getString("prenom");
                String mail_user = rs.getString("email");
                String date_user = rs.getString("date_creation");
                String type_user = rs.getString("typeuser");
                String role_user = rs.getString("role");

                UserAccount user = new UserAccount(matricule_user, nom_user, prenom_user, mail_user, date_user, type_user, role_user);
                useraccount.add(user); // Ajouter l'étudiant à la liste
            }

            // Mettre à jour la table avec la nouvelle liste
            tableView.setItems(useraccount);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les étudiants : " + e.getMessage());
        }
    }
    
    private void loadnombre() {
        String sqlTotal = "SELECT COUNT(*) AS total FROM compte_utilisateur";
        String sqlEtudiant = "SELECT COUNT(*) AS totalEtudiant FROM compte_utilisateur WHERE typeuser = 'etudiant'";
        String sqlEnseignant = "SELECT COUNT(*) AS totalEnseignant FROM compte_utilisateur WHERE typeuser = 'enseignant'";
        String sqlAdministrateur = "SELECT COUNT(*) AS totalAdministrateur FROM compte_utilisateur WHERE typeuser = 'admin'";

        try (Connection conn = DatabaseConnection.getConnection()) {

            // Nombre total
            try (PreparedStatement pstmt = conn.prepareStatement(sqlTotal);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String total = rs.getString("total");
                    nbr_user.setText(total);
                    System.out.println("Nombre total d'utilisateurs : " + total);
                }
            }

            // Nombre d'étudiants
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEtudiant);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String totalEtudiant = rs.getString("totalEtudiant");
                    nbr_Etudiant.setText(totalEtudiant);
                    System.out.println("Nombre d'étudiants : " + totalEtudiant);
                }
            }

            // Nombre d'enseignants
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEnseignant);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                	String totalEnseignant = rs.getString("totalEnseignant");
                    nbr_Enseignant.setText(totalEnseignant);
                    System.out.println("Nombre d'enseignants : " + totalEnseignant);
                }
            }

            // Nombre d'administrateurs
            try (PreparedStatement pstmt = conn.prepareStatement(sqlAdministrateur);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                	String totalAdministrateur = rs.getString("totalAdministrateur");
                    nbr_admin.setText(totalAdministrateur);
                    System.out.println("Nombre d'administrateurs : " + totalAdministrateur);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les données : " + e.getMessage());
        }
    }

    
    private void supprimerEtudiant(String matricule) {
        String sql = "DELETE FROM compte_utilisateur WHERE matricule = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricule);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Succès", "Étudiant supprimé avec succès de la base de données !");
            } else {
                showAlert("Information", "Aucun étudiant trouvé avec le matricule spécifié.");
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
    
 // Méthode pour vider les champs
    private void clearelement() {
    	deletedEtudiant.clear();
    }
}
