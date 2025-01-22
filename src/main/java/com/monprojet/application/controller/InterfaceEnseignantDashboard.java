package com.monprojet.application.controller;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.prefs.Preferences;

import com.monprojet.application.controller.InterfaceAdministrateurAdd.DatabaseConnection;
import com.monprojet.application.model.EnseignantClass;
import com.monprojet.application.model.Etudiant;
import com.monprojet.application.model.MarquageEnseignant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class InterfaceEnseignantDashboard {

    @FXML
    private TextField Heur;

    @FXML
    private Text Module;
    @FXML
    private Label module;

    @FXML
    private TextField absenceMatricule;

    @FXML
    private DatePicker dateABS;

    @FXML
    private TableColumn<MarquageEnseignant, String> dateInscription_table;

    @FXML
    private TableColumn<MarquageEnseignant, String> matricule_table;

    @FXML
    private Label niveau2;

    @FXML
    private TableColumn<MarquageEnseignant, String> niveau_table;

    @FXML
    private Text nomClasse;

    @FXML
    private Label nomEn;

    @FXML
    private TableColumn<MarquageEnseignant, String> nom_table;

    @FXML
    private TableColumn<MarquageEnseignant, String> nombreabs_table;

    @FXML
    private TableColumn<MarquageEnseignant, String> nombreheur_table;

    @FXML
    private TableColumn<MarquageEnseignant, String> prenom_table;
    @FXML
    private TableColumn<MarquageEnseignant, Void> action;

    @FXML
    private Button quitter;

    @FXML
    private Button valider;
    @FXML
    private TableView<MarquageEnseignant> tableView;
    private ObservableList<MarquageEnseignant> MarquageEnseignant = FXCollections.observableArrayList();
    
    
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
    	matricule_table.setCellValueFactory(new PropertyValueFactory<>("matricule_table"));
    	nom_table.setCellValueFactory(new PropertyValueFactory<>("nom_table"));
    	prenom_table.setCellValueFactory(new PropertyValueFactory<>("prenom_table"));
    	niveau_table.setCellValueFactory(new PropertyValueFactory<>("niveau_table"));
    	dateInscription_table.setCellValueFactory(new PropertyValueFactory<>("dateInscription_table"));
    	nombreabs_table.setCellValueFactory(new PropertyValueFactory<>("nombreabs_table"));
    	nombreheur_table.setCellValueFactory(new PropertyValueFactory<>("nombreheur_table"));

        // Ajouter une colonne avec un bouton d'action
        addActionButtonToTable();
     // Lire les préférences utilisateur au démarrage
        
        Preferences prefs = Preferences.userNodeForPackage(InterfaceEnseignantClass.class);
        String username = prefs.get("matricule", "default");
        System.out.println("Matricule de l'enseignant : " + username);
        String matEN = prefs.get("module", "default");
        
        Module.setText(prefs.get("module","default"));
        niveau2.setText(prefs.get("niveau","default"));
        nomClasse.setText(prefs.get("classe", "default"));
        module.setText(prefs.get("module", "default"));
        // Charger les données des étudiants depuis la base de données
        loadEtudiants();
        
        

        // Action pour le bouton "Ajouter"
        valider.setOnAction(event -> {
            if (validateFields()) {
                String matricule_table = absenceMatricule.getText();
                String heur = Heur.getText();
                String dateInscription = dateABS.getValue().toString();
                ajouterAbsence(matricule_table, heur, dateInscription);
                loadEtudiants(); // Recharger la table après ajout
                clearelement();  // Vider les champs
            }
        });


        // Action pour le bouton "Vider"
        //buttonvider.setOnAction(event -> clearelement());

        // Action pour le bouton "Quitter"
        quitter.setOnAction(event -> System.exit(0)); // Fermer l'application
    }
    private void ajouterAbsence(String matricule_table2, String heur2, String dateInscription) {
    	Preferences prefs = Preferences.userNodeForPackage(InterfaceEnseignantClass.class);
        //String username = prefs.get("matricule", "default");
        //System.out.println("Matricule de l'enseignant : " + username);
        
        String sql = "INSERT INTO absence (date_abs, heure_abs, id_enseig, id_etud, id_module) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Définir les valeurs des paramètres
            pstmt.setString(1, dateInscription);
            pstmt.setString(2, heur2);
            pstmt.setString(3, prefs.get("matricule", "default")); // Matricule de l'enseignant
            pstmt.setString(4, matricule_table2);                  // Matricule de l'étudiant
            pstmt.setString(5, prefs.get("module", "default"));   // Module enseigné

            // Exécuter la requête
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


	private void addActionButtonToTable() {
        Callback<TableColumn<MarquageEnseignant, Void>, TableCell<MarquageEnseignant, Void>> cellFactory = param -> {
            return new TableCell<>() {
                private final Button btn = new Button("Marquer");

                {
                    btn.setStyle("-fx-background-color: #8eb486; cursor:Hand; -fx-font-size: 11px; -fx-text-fill: white; -fx-padding: 5px 10px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
                    btn.setOnAction(event -> {
                        MarquageEnseignant data = getTableView().getItems().get(getIndex());
                        // Afficher les données de l'étudiant dans la console
                        System.out.println("Bouton cliqué pour : " +  data.getMatricule_table() );
                        
                        // Appel à une méthode (optionnel)
                        // envoiesurledash(data.getNomdelaclasse(), data.getNiveau(), data.getModule(), true);
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


  
	private boolean validateFields() {
	    if (absenceMatricule.getText().isEmpty() || Heur.getText().isEmpty() || dateABS.getValue() == null) {
	        showAlert("Erreur", "Veuillez remplir tous les champs !");
	        return false;
	    }
	    if (absenceMatricule.getText().length() != 9) {
	        showAlert("Erreur", "La taille du matricule doit absolument être de 9 caractères !");
	        return false;
	    }
	    return true;
	}

    
    // Méthode pour charger les étudiants depuis la base de données
	private void loadEtudiants() {
	    Preferences prefs = Preferences.userNodeForPackage(InterfaceEnseignantClass.class);
	    
	    // Récupérer les préférences pour FILIERE et niveau, avec des valeurs par défaut
	    String filiere = prefs.get("classe", "default");
	    String niveau = prefs.get("niveau", "default");

	    String sqlEtudiants = "SELECT * FROM etudiant WHERE FILIERE = ? AND niveau = ?";
	    String sqlAbsences = "SELECT COUNT(id_etud) AS nombre_absences, SUM(heure_abs) AS heures_absences FROM absence WHERE id_etud = ?";

	    try (Connection conn = DatabaseConnection.getConnection();
	         PreparedStatement pstmtEtudiants = conn.prepareStatement(sqlEtudiants)) {

	        // Définir les paramètres de la requête SQL avec les valeurs récupérées
	        pstmtEtudiants.setString(1, filiere);
	        pstmtEtudiants.setString(2, niveau);

	        try (ResultSet rsEtudiants = pstmtEtudiants.executeQuery()) {

	            // Vider la liste avant de la remplir
	            MarquageEnseignant.clear();

	            while (rsEtudiants.next()) {
	                String matricule = rsEtudiants.getString("matricule");
	                String nom = rsEtudiants.getString("nom");
	                String prenom = rsEtudiants.getString("prenom");
	                String niveauEtudiant = rsEtudiants.getString("niveau");
	                String dateInscription = rsEtudiants.getString("date_inscription");

	                // Initialiser les valeurs d'absence
	                String nombreDeFois = "0";
	                String heuresAbsenceAdditionnee = "0";

	                // Récupérer les absences pour cet étudiant
	                try (PreparedStatement pstmtAbsences = conn.prepareStatement(sqlAbsences)) {
	                    pstmtAbsences.setString(1, matricule); // Utiliser le matricule comme identifiant
	                    try (ResultSet rsAbsences = pstmtAbsences.executeQuery()) {
	                        if (rsAbsences.next()) {
	                            nombreDeFois = rsAbsences.getString("nombre_absences");
	                            heuresAbsenceAdditionnee = rsAbsences.getString("heures_absences");

	                            // Gérer les cas où les valeurs sont nulles
	                            if (nombreDeFois == null) {
	                                nombreDeFois = "0";
	                            }
	                            if (heuresAbsenceAdditionnee == null) {
	                                heuresAbsenceAdditionnee = "0";
	                            }
	                        }
	                    }
	                }

	                // Ajouter l'étudiant avec ses données et ses absences à la liste
	                MarquageEnseignant etudiant = new MarquageEnseignant(
	                        matricule,
	                        nom,
	                        prenom,
	                        niveauEtudiant,
	                        dateInscription,
	                        nombreDeFois,
	                        heuresAbsenceAdditionnee
	                );

	                MarquageEnseignant.add(etudiant); // Ajouter l'étudiant à la liste
	            }

	            // Mettre à jour la table avec la nouvelle liste
	            tableView.setItems(MarquageEnseignant);

	        } catch (SQLException e) {
	            e.printStackTrace();
	            showAlert("Erreur", "Impossible de charger les étudiants : " + e.getMessage());
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        showAlert("Erreur de connexion", "Problème de connexion à la base de données : " + e.getMessage());
	    }
	}


    
    private void readPreferences() {
        Preferences prefs = Preferences.userNodeForPackage(InterfaceEnseignantClass.class);
        String username = prefs.get("matricule", "default");
        System.out.println("Matricule de l'enseignant : " + username);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour vider les champs
    private void clearelement() {
    	absenceMatricule.getText();
    	Heur.getText();
        dateABS.setValue(null);
        
    }
    

}
