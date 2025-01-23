package com.monprojet.application.model;

public class UserAccount {

	private String matricule_user;
    private String nom_user;
    private String prenom_user;
    private String mail_user;
    private String date_user;
    private String type_user;
    private String role_user;
    
    public UserAccount(String matricule_user, String nom_user, String prenom_user, String mail_user, String date_user, String type_user, String role_user) {
        this.matricule_user = matricule_user;
        this.nom_user = nom_user;
        this.prenom_user = prenom_user;
        this.mail_user = mail_user;
        this.date_user = date_user;
        this.type_user = type_user;
        this.role_user = role_user;
    }

    // Getters et setters

    public String getMatricule_user() {
        return matricule_user; 
    }

    public void setMatricule_user(String matricule_user) {
        this.matricule_user = matricule_user;
    }

    public String getNom_user() {
        return nom_user;
    }

    public void setNom_user(String nom_user) {
        this.nom_user = nom_user;
    }

    public String getPrenom_user() {
        return prenom_user;
    }

    public void setPrenom_user(String prenom_user) {
        this.prenom_user = prenom_user;
    }

    public String getMail_user() {
        return mail_user;
    }

    public void setMail_user(String mail_user) {
        this.mail_user = mail_user;
    }

    public String getDate_user() {
        return date_user;
    }

    public void setDate_user(String date_user) {
        this.date_user= date_user;
    }

    public String getType_user() {
        return type_user;
    }

    public void setType_user(String type_user) {
        this.type_user = type_user;
    }
    
    public String getRole_user() {
        return role_user;
    }

    public void setRole_user(String role_user) {
        this.role_user = role_user;
    }
}

