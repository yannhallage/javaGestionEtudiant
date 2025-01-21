package com.monprojet.application.model;

public class EnseignantClass {

    private String nomdelaclasse;
    private String niveau;
    private String module;

    // Constructeur
    public EnseignantClass(String nomdelaclasse, String niveau, String module) {
        this.nomdelaclasse = nomdelaclasse;
        this.niveau = niveau;
        this.module = module;
    }

    // Getters
    public String getNomdelaclasse() {
        return nomdelaclasse;
    }

    public String getNiveau() {
        return niveau;
    }

    public String getModule() {
        return module;
    }

    // Setters
    public void setNomdelaclasse(String nomdelaclasse) {
        this.nomdelaclasse = nomdelaclasse;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
