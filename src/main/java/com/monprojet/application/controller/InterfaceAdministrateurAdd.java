package com.monprojet.application.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.monprojet.application.model.Etudiant;

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

    // TableView
    @FXML
    private TableView<Etudiant> tableView;

    @FXML
    private TableColumn<Etudiant, String> matricule_etudiant;

    @FXML
    private TableColumn<Etudiant, String> nom_etudiant;

    @FXML
    private TableColumn<Etudiant, String> prenom_etudiant;

    @FXML
    private TableColumn<Etudiant, String> classe_etudiant;

    @FXML
    private TableColumn<Etudiant, String> niveau_etudiant;

    @FXML
    private TableColumn<Etudiant, String> date_etudiant;

    private ObservableList<Etudiant> etudiants = FXCollections.observableArrayList();

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
        matricule_etudiant.setCellValueFactory(new PropertyValueFactory<>("matricule"));
        nom_etudiant.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenom_etudiant.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        classe_etudiant.setCellValueFactory(new PropertyValueFactory<>("classe"));
        niveau_etudiant.setCellValueFactory(new PropertyValueFactory<>("niveauClasse"));
        date_etudiant.setCellValueFactory(new PropertyValueFactory<>("dateInscription"));

        // Charger les données des étudiants depuis la base de données
        loadEtudiants();

        // Action pour le bouton "Ajouter"
        buttonajouter.setOnAction(event -> {
            // Récupérer les valeurs des champs
            String matricule = inputmatricule.getText();
            String nom = inputnom.getText();
            String prenom = inputprenom.getText();
            String classe = inputclasse.getText();
            String niveauClasse = inputniveauclasse.getText();
            String dateInscription = inputdateinscription.getValue() != null ? inputdateinscription.getValue().toString() : "Non précisé";

            // Valider les données (exemple de validation simple)
            if (matricule.isEmpty() || nom.isEmpty() || prenom.isEmpty() || classe.isEmpty() || niveauClasse.isEmpty() || dateInscription.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs !");
            } else {
                if (matricule.length() != 9) {
                    showAlert("Erreur", "La taille du matricule doit absolument être de 9 caractères !");
                } else {
                    // Ajouter l'étudiant à la base de données
                    ajouterEtudiant(matricule, nom, prenom, classe, niveauClasse, dateInscription);
                    loadEtudiants(); // Recharger les étudiants dans la table
                    clearelement(); // Vider les champs
                }
            }
        });

        // Action pour le bouton "Vider"
        buttonvider.setOnAction(event -> clearelement());

        // Action pour le bouton "Quitter"
        buttonquitter.setOnAction(event -> System.exit(0)); // Fermer l'application
    }

    // Méthode pour ajouter un étudiant dans la base de données
    private void ajouterEtudiant(String matricule, String nom, String prenom, String classe, String niveauClasse, String dateInscription) {
        Integer idClasse = getClasseId(classe); // Vérifier si la classe existe

        if (idClasse == null) {
            showAlert("Erreur", "La classe '" + classe + "' n'existe pas dans la base de données.");
            return;
        }

        String sql = "INSERT INTO etudiant (matricule, nom, prenom, filiere, niveau, date_inscription, id_classe) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, matricule);
            pstmt.setString(2, nom);
            pstmt.setString(3, prenom);
            pstmt.setString(4, classe);
            pstmt.setString(5, niveauClasse);
            pstmt.setString(6, dateInscription);
            pstmt.setInt(7, idClasse); // Utiliser l'identifiant de la classe
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert("Succès", "Étudiant ajouté avec succès à la base de données !");
            } else {
                showAlert("Erreur", "Une erreur s'est produite lors de l'ajout à la base de données.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ajouter l'étudiant : " + e.getMessage());
        }
    }

    // Méthode pour vérifier si la classe existe dans la base de données
    private Integer getClasseId(String classe) {
        String sql = "SELECT id FROM classe WHERE specialite = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, classe);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("id"); // Retourne l'identifiant si la classe existe
            } else {
                return null; // Retourne null si la classe n'existe pas
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la vérification de la classe : " + e.getMessage());
            return null;
        }
    }

    // Méthode pour charger les étudiants depuis la base de données
    private void loadEtudiants() {
        String sql = "SELECT * FROM etudiant";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            etudiants.clear(); // Vider la liste avant de la remplir

            while (rs.next()) {
                String matricule = rs.getString("matricule");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                String classe = rs.getString("filiere");
                String niveau = rs.getString("niveau");
                String dateInscription = rs.getString("date_inscription");

                Etudiant etudiant = new Etudiant(matricule, nom, prenom, classe, niveau, dateInscription);
                etudiants.add(etudiant); // Ajouter l'étudiant à la liste
            }

            // Mettre à jour la table avec la nouvelle liste
            tableView.setItems(etudiants);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les étudiants : " + e.getMessage());
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
        inputmatricule.clear();
        inputnom.clear();
        inputprenom.clear();
        inputclasse.clear();
        inputniveauclasse.clear();
        inputdateinscription.setValue(null);
    }
}
