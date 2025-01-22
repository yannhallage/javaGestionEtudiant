package com.monprojet.application.controller;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class InterfaceEnseignantClass {

    @FXML
    private TableColumn<EnseignantClass, String> module;

    @FXML
    private TableColumn<EnseignantClass, String> niveau;

    @FXML
    private TableColumn<EnseignantClass, String> nomdelaclasse;

    @FXML
    private TableColumn<EnseignantClass, Void> action;

    @FXML
    private TableView<EnseignantClass> tableView;

    private ObservableList<EnseignantClass> enseignantclass = FXCollections.observableArrayList();

    // Classe de connexion à la base de données
    public static class DatabaseConnection {
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
        niveau.setCellValueFactory(new PropertyValueFactory<>("niveau"));
        module.setCellValueFactory(new PropertyValueFactory<>("module"));

        // Ajouter une colonne avec un bouton d'action
        addActionButtonToTable();

        // Lire les préférences utilisateur au démarrage
        readPreferences();

        // Charger les données des enseignants depuis la base de données
        loadEnseignantModules();
    }

    private void addActionButtonToTable() {
        Callback<TableColumn<EnseignantClass, Void>, TableCell<EnseignantClass, Void>> cellFactory = param -> {
            return new TableCell<>() {
                private final Button btn = new Button("Action");

                {
                    btn.setStyle("-fx-background-color: #8eb486; cursor:Hand; -fx-font-size: 11px; -fx-text-fill: white; -fx-padding: 5px 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                    btn.setOnAction(event -> {
                        EnseignantClass data = getTableView().getItems().get(getIndex());
                        envoiesurledash(data.getNomdelaclasse(), data.getNiveau(), data.getModule(), true);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(btn);
                    }
                }
            };
        };

        action.setCellFactory(cellFactory);
    }

    private void envoiesurledash(String classe, String niveau, String module, boolean d) {
        System.out.println("Classe : " + classe);
        System.out.println("Niveau : " + niveau);
        System.out.println("Module : " + module);

        // Sauvegarder les préférences
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        prefs.put("classe", classe);
        prefs.put("niveau", niveau);
        prefs.put("module", module);

        // Ouvrir une nouvelle fenêtre
        openWindow("/fxml/InterfaceEnseignantDashboard.fxml");
    }

    private void readPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(InterfaceEnseignantClass.class);
        String username = prefs.get("matricule", "default");
        System.out.println("Matricule de l'enseignant : " + username);
    }

    private void loadEnseignantModules() {
        String sqlModule = "SELECT intitule, classedispensemodule FROM module WHERE enseignantmodule = ?";
        String sqlClasse = "SELECT specialite, niveau FROM classe WHERE specialite = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement pstmtModule = conn.prepareStatement(sqlModule)) {
                Preferences prefs = Preferences.userNodeForPackage(InterfaceEnseignantClass.class);
                String matricule = prefs.get("matricule", null);

                if (matricule == null || matricule.isEmpty()) {
                    showAlert("Erreur", "Le matricule de l'enseignant n'est pas défini.");
                    return;
                }

                pstmtModule.setString(1, matricule);

                try (ResultSet rsModule = pstmtModule.executeQuery()) {
                    enseignantclass.clear();

                    while (rsModule.next()) {
                        String module = rsModule.getString("intitule");
                        String classes = rsModule.getString("classedispensemodule");

                        try (PreparedStatement pstmtClasse = conn.prepareStatement(sqlClasse)) {
                            pstmtClasse.setString(1, classes);

                            try (ResultSet rsClasse = pstmtClasse.executeQuery()) {
                                if (rsClasse.next()) {
                                    String nomdelaclasse = rsClasse.getString("specialite");
                                    String niveau = rsClasse.getString("niveau");

                                    EnseignantClass enseignant = new EnseignantClass(nomdelaclasse, niveau, module);
                                    enseignantclass.add(enseignant);
                                }
                            }
                        }
                    }

                    tableView.setItems(enseignantclass);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger les données : " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void openWindow(String fxmlFile) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane root = loader.load();

            // Créer une nouvelle fenêtre
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Nouvelle Fenêtre");
            stage.setResizable(false);

            // Afficher la nouvelle fenêtre
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la fenêtre : " + e.getMessage());
        }
    }
}
