package com.monprojet.application.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import com.monprojet.application.model.EtudiantDash;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class InterfaceEtudiant {

    @FXML
    private TableColumn<EtudiantDash, String> table_code;
    @FXML
    private Label label_classe;

    @FXML
    private Label label_niveau;

    @FXML
    private Label label_user;
    @FXML
    private Label label_username;
    @FXML
    private TableColumn<EtudiantDash, String> table_nommodule;
    @FXML
    private TableColumn<EtudiantDash, String> table_classe;
    @FXML
    private TableColumn<EtudiantDash, String> table_niveaumodule;
    @FXML
    private TableColumn<EtudiantDash, String> table_heurmodule;
    @FXML
    private TableColumn<EtudiantDash, String> table_heurabsence;
    @FXML
    private TableColumn<EtudiantDash, String> table_nombreabsence;
    @FXML
    private TableColumn<EtudiantDash, String> table_enseignant;
    @FXML
    private TableView<EtudiantDash> tableView;

    private ObservableList<EtudiantDash> etudiants = FXCollections.observableArrayList();

   
    @FXML
    public void initialize() {
        // Configurer les colonnes du TableView
        table_code.setCellValueFactory(new PropertyValueFactory<>("table_code"));
        table_nommodule.setCellValueFactory(new PropertyValueFactory<>("table_nommodule"));
        table_classe.setCellValueFactory(new PropertyValueFactory<>("table_classe"));
        table_niveaumodule.setCellValueFactory(new PropertyValueFactory<>("table_niveaumodule"));
        table_heurmodule.setCellValueFactory(new PropertyValueFactory<>("table_heurmodule"));
        table_heurabsence.setCellValueFactory(new PropertyValueFactory<>("table_heurabsence"));
        table_nombreabsence.setCellValueFactory(new PropertyValueFactory<>("table_nombreabsence"));
        table_enseignant.setCellValueFactory(new PropertyValueFactory<>("table_enseignant"));

        // Charger les données
        loadEtudiants();
    }

    private void loadEtudiants() {
        Preferences prefs = Preferences.userNodeForPackage(getClass());
        String matricule = prefs.get("matricule", null);

        if (matricule == null) {
            showAlert1("Erreur", "Matricule non défini.");
            return;
        }

        String filiere = null;
        String niveau = null;
        String allname = null; 

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Récupérer la filière et le niveau de l'étudiant
            String sqlEtudiant = "SELECT typeuser FROM compte_utilisateur WHERE matricule = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEtudiant)) {
                pstmt.setString(1, matricule);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        filiere = rs.getString("typeuser");
                        label_user.setText(filiere);
                    }
                }
            }

            if (filiere == null ) {
                showAlert1("Erreur", "Impossible de récupérer les informations de l'étudiant.");
                return;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert1("Erreur", "Impossible de charger les données : " + e.getMessage());
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Récupérer la filière et le niveau de l'étudiant
            String sqlEtudiant = "SELECT filiere, niveau, nom , prenom FROM etudiant WHERE matricule = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlEtudiant)) {
                pstmt.setString(1, matricule);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        filiere = rs.getString("filiere");
                        niveau = rs.getString("niveau");
                        allname = rs.getString("nom") + " " + rs.getString("prenom"); 
                        
                        label_classe.setText(filiere);
                        label_niveau.setText(niveau);
                        label_username.setText(allname);
                    }
                }
            }

            if (filiere == null || niveau == null) {
                showAlert1("Erreur", "Impossible de récupérer les informations de l'étudiant.");
                return;
            }

            // Récupérer les modules et absences
            String sqlModules = """
                SELECT 
                    m.code, 
                    m.intitule AS nommodule, 
                    m.heurmodule, 
                    m.enseignantmodule AS enseignant, 
                    COUNT(a.id) AS nombreabsence, 
                    SUM(a.heure_abs) AS heurabsence
                FROM 
                    module m
                LEFT JOIN 
                    absence a ON m.code = a.id_module AND a.id_etud = ?
                WHERE 
                    m.classedispensemodule = ? AND m.niveau = ?
                GROUP BY 
                    m.code, m.intitule, m.heurmodule, m.enseignantmodule;
            """;

            try (PreparedStatement pstmt = conn.prepareStatement(sqlModules)) {
                pstmt.setString(1, matricule);
                pstmt.setString(2, filiere);
                pstmt.setString(3, niveau);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        String code = rs.getString("code");
                        String nommodule = rs.getString("nommodule");
                        String heurmodule = rs.getString("heurmodule");
                        String enseignant = rs.getString("enseignant");
                        String nombreabsence = rs.getString("nombreabsence");
                        String heurabsence = rs.getString("heurabsence");

                        // Ajouter les données dans une liste d'étudiants
                        etudiants.add(new EtudiantDash(
                            code, 
                            nommodule, 
                            filiere, 
                            niveau, 
                            heurmodule,
                            heurabsence == null ? "0" : heurabsence,
                            nombreabsence == null ? "0" : nombreabsence,
                            enseignant
                        ));
                    }
                }
            }

            // Mettre à jour la TableView
            tableView.setItems(etudiants);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert1("Erreur", "Impossible de charger les données : " + e.getMessage());
        }
    }


    private void showAlert1(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    // Classe de connexion à la base de données
    public static class DatabaseConnection {
        private static final String URL = "jdbc:mysql://localhost:3306/gestionabsenceetudiant";
        private static final String USER = "root";
        private static final String PASSWORD = "root";

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        }
    }
}
