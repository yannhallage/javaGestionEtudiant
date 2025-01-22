package com.monprojet.application.model;

public class MarquageEnseignant {

	    private String matricule_table;
	    private String nom_table;
	    private String prenom_table;
	    private String niveau_table;
	    private String dateInscription_table;
	    private String nombreabs_table;
	    private String nombreheur_table;
	    
	    public MarquageEnseignant(String matricule_table, String nom_table, String prenom_table, String niveau_table, String dateInscription_table, String nombreabs_table ,String nombreheur_table) {
	        this.matricule_table = matricule_table;
	        this.nom_table = nom_table;
	        this.prenom_table = prenom_table;
	        this.niveau_table = niveau_table;
	        this.dateInscription_table = dateInscription_table;
	        this.nombreabs_table = nombreabs_table;
	        this.nombreheur_table = nombreheur_table;
	       //this.options = options;
	    }
	    
	    public String getMatricule_table() {
	        return matricule_table;
	    }
	    public String getNom_table() {
	        return nom_table;
	    }
	    
	    public String getPrenom_table() {
	        return prenom_table;
	    }
	    public String getNiveau_table() {
	        return niveau_table;
	    }
	    public String getDateInscription_table() {
	        return dateInscription_table;
	    }
	    public String getNombreabs_table() {
	        return nombreabs_table;
	    }
	    public String getNombreheur_table() {
	        return nombreheur_table;
	    }
	    
	    public void setMatricule_table(String matricule_table) {
	        this.matricule_table = matricule_table;
	    }
	    public void setPrenom_table(String prenom_table) {
	        this.prenom_table = prenom_table;
	    }
	    public void setNiveau_table(String niveau_table) {
	        this.niveau_table = niveau_table;
	    }
	    public void setDateInscription_table(String dateInscription_table) {
	        this.dateInscription_table = dateInscription_table;
	    }
	    
	    public void setNombreabs_table(String nombreABS_table) {
	        this.nombreabs_table = nombreABS_table;
	    }
	    
	    public void setNombreheur_table(String nombreHeur_table) {
	        this.nombreheur_table = nombreHeur_table;
	    }
}

