package com.monprojet.application.model;

public class Classe {

    private String nomDeLaClasse;
    private String niveau;
    private String annee;
    private String nombreDeModule;
    private String options;
    
    public Classe(String nomDeLaClasse, String niveau, String annee, String nombreDeModule, String options) {
        this.nomDeLaClasse = nomDeLaClasse;
        this.niveau = niveau;
        this.annee = annee;
        this.nombreDeModule = nombreDeModule;
        this.options = options;
    }

    // Getters et setters

    public String getNomDeLaClasse() {
        return nomDeLaClasse;
    }

    public void setNomDeLaClasse(String nomDeLaClasse) {
        this.nomDeLaClasse = nomDeLaClasse;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getAnnee() {
        return annee;
    }

    public void setAnnee(String annee) {
        this.annee = annee;
    }

    public String getNombreDeModule() {
        return nombreDeModule;
    }

    public void setNombreDeModule(String nombreDeModule) {
        this.nombreDeModule = nombreDeModule;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }
}
