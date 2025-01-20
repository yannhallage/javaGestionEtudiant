package com.monprojet.application.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.monprojet.application.model.Classe;

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
    private TextField option_input;

    // TableView
    @FXML
    private TableView<Classe> tableView;

    @FXML
    private TableColumn<Classe, String> nomdelaclasse_column;
    @FXML
    private TableColumn<Classe, String> niveau_column;
    @FXML
    private TableColumn<Classe, String> annee_column;
    @FXML
    private TableColumn<Classe, String> nombredemodule_column;
    @FXML
    private TableColumn<Classe, String> option_column;

    private ObservableList<Classe> classes = FXCollections.observableArrayList();

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
        nomdelaclasse_column.setCellValueFactory(new PropertyValueFactory<>("nomDeLaClasse"));
        niveau_column.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        annee_column.setCellValueFactory(new PropertyValueFactory<>("annee"));
        nombredemodule_column.setCellValueFactory(new PropertyValueFactory<>("nombreDeModule"));
        option_column.setCellValueFactory(new PropertyValueFactory<>("options"));

        // Charger les données des classes depuis la base de données
        loadClasses();

        // Action pour le bouton "Ajouter"
        buttonajouter.setOnAction(event -> {
            // Récupérer les valeurs des champs
            String nomdelaclasse = nomclasse_input.getText();
            String niveau = niveauclasse_input.getText();
            String annee = anneeclasse_input.getText();
            String nombredemodule = module_input.getText();
            String option = option_input.getText();

            // Valider les données (exemple de validation simple)
            if (nomdelaclasse.isEmpty() || niveau.isEmpty() || annee.isEmpty() || nombredemodule.isEmpty() || option.isEmpty()) {
                showAlert("Erreur", "Veuillez remplir tous les champs !");
            } else {
                // Ajouter la classe à la base de données
                ajouterClasse(nomdelaclasse, niveau, annee, nombredemodule, option);
                loadClasses(); // Recharger les classes dans la table
                clearelement(); // Vider les champs
            }
        });

        // Action pour le bouton "Vider"
        buttonvider.setOnAction(event -> clearelement());

        // Action pour le bouton "Quitter"
        buttonquitter.setOnAction(event -> System.exit(0)); // Fermer l'application
    }

    // Méthode pour ajouter une classe dans la base de données
    private void ajouterClasse(String nomdelaclasse, String niveau, String annee, String nombredemodule, String option) {
        String sql = "INSERT INTO classe (specialite, option_s, niveau, nbr_module, annee) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nomdelaclasse);
            pstmt.setString(2, option);
            pstmt.setString(3, niveau);
            pstmt.setString(4, nombredemodule);
            pstmt.setString(5, annee);

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
    private void loadClasses() {
        String sql = "SELECT * FROM classe";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            classes.clear(); // Vider la liste avant de la remplir

            while (rs.next()) {
                String nomdelaclasse = rs.getString("specialite");
                String niveau = rs.getString("niveau");
                String annee = rs.getString("annee");
                String nombredemodule = rs.getString("nbr_module");
                String option = rs.getString("option_s");

                Classe classe = new Classe(nomdelaclasse, niveau, annee, nombredemodule, option);
                classes.add(classe); // Ajouter la classe à la liste
            }

            // Mettre à jour la table avec la nouvelle liste
            tableView.setItems(classes);

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

    // Méthode pour vider les champs
    private void clearelement() {
    	nomclasse_input.clear();
        niveauclasse_input.clear();
        anneeclasse_input.clear();
        module_input.clear();
        option_input.clear();
    }
}
