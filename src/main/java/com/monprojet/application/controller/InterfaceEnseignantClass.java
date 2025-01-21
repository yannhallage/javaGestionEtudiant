package com.monprojet.application.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.prefs.Preferences;

import com.monprojet.application.model.EnseignantClass;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class InterfaceEnseignantClass {

    @FXML
    private TableColumn<EnseignantClass, String> module;

    @FXML
    private TableColumn<EnseignantClass, String> niveau;

    @FXML
    private TableColumn<EnseignantClass, String> nomdelaclasse;

 
    //private TableColumn<EnseignantClass, String> options;

    @FXML
    private TableView<EnseignantClass> tableView;

    private ObservableList<EnseignantClass> enseignantclass = FXCollections.observableArrayList();
    
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
        nomdelaclasse.setCellValueFactory(new PropertyValueFactory<>("nomdelaclasse"));
        //options.setCellValueFactory(new PropertyValueFactory<>("options"));
        niveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        module.setCellValueFactory(new PropertyValueFactory<>("module"));
        
        // Lire les préférences au démarrage
        readPreferences();
        
        // Charger les données des étudiants depuis la base de données
        loadEnseignantModules();
    }

    // Méthode pour lire les préférences et afficher dans la console
    private void readPreferences() {
        // Accéder aux préférences
        Preferences prefs = Preferences.userNodeForPackage(InterfaceEnseignantClass.class);

        // Lire les préférences
        String username = prefs.get("matricule", "default");
        
        // Affichage des préférences dans la console
        System.out.println("Username: " + username);
        
        // Si vous voulez afficher ces valeurs dans un élément graphique, vous pouvez le faire ici, par exemple :
        // Vous pouvez afficher ces informations dans un Label ou autre composant si nécessaire
    }

    // Méthode pour charger les modules et les classes associés à l'enseignant depuis la base de données
    private void loadEnseignantModules() {
        String sqlModule = "SELECT intitule, classedispensemodule FROM module WHERE enseignantmodule = ?";
        String sqlClasse = "SELECT specialite, niveau FROM classe WHERE specialite = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Préparer la requête pour le module
            try (PreparedStatement pstmtModule = conn.prepareStatement(sqlModule)) {
                // Récupérer le matricule de l'enseignant depuis les préférences
                Preferences prefs = Preferences.userNodeForPackage(InterfaceEnseignantClass.class);
                String matricule = prefs.get("matricule", null);

                if (matricule == null || matricule.isEmpty()) {
                    showAlert("Erreur", "Le matricule de l'enseignant n'est pas défini.");
                    return;
                }

                pstmtModule.setString(1, matricule);

                try (ResultSet rsModule = pstmtModule.executeQuery()) {
                    enseignantclass.clear(); // Vider la liste avant de la remplir

                    while (rsModule.next()) {
                        String module = rsModule.getString("intitule");
                        String classes = rsModule.getString("classedispensemodule");

                        // Requête pour les informations de la classe
                        try (PreparedStatement pstmtClasse = conn.prepareStatement(sqlClasse)) {
                            pstmtClasse.setString(1, classes);

                            try (ResultSet rsClasse = pstmtClasse.executeQuery()) {
                                if (rsClasse.next()) {
                                    String nomdelaclasse = rsClasse.getString("specialite");
                                    String niveau = rsClasse.getString("niveau");

                                    // Créer une instance de EnseignantClass et l'ajouter à la liste
                                    EnseignantClass enseignant = new EnseignantClass(nomdelaclasse, niveau, module);
                                    enseignantclass.add(enseignant);
                                } else {
                                    System.out.println("Aucune classe trouvée pour le module : " + module);
                                }
                            }
                        }
                    }
                    // Mettre à jour le TableView avec les données chargées
                    tableView.setItems(enseignantclass);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les données : " + e.getMessage());
        }
    }


    // Méthode pour afficher une alerte
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    
}


