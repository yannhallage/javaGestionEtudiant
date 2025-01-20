package com.monprojet.application.model;


public class Etudiant {
    private String matricule;
    private String nom;
    private String prenom;
    private String classe;
    private String niveauClasse;
    private String dateInscription;

    public Etudiant(String matricule, String nom, String prenom, String classe, String niveauClasse, String dateInscription) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.classe = classe;
        this.niveauClasse = niveauClasse;
        this.dateInscription = dateInscription;
    }

    // Getters et setters

    public String getMatricule() {
        return matricule;
    }

    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getNiveauClasse() {
        return niveauClasse;
    }

    public void setNiveauClasse(String niveauClasse) {
        this.niveauClasse = niveauClasse;
    }

    public String getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(String dateInscription) {
        this.dateInscription = dateInscription;
    }
}