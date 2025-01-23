package com.monprojet.application.model;

public class EtudiantDash {
    private String table_code;
    private String table_nommodule;
    private String table_classe;
    private String table_niveaumodule;
    private String table_heurmodule;
    private String table_heurabsence;
    private String table_nombreabsence;
    private String table_enseignant;

    public EtudiantDash(String table_code, String table_nommodule, String table_classe, 
                        String table_niveaumodule, String table_heurmodule, 
                        String table_heurabsence, String table_nombreabsence, 
                        String table_enseignant) {
        this.table_code = table_code;
        this.table_nommodule = table_nommodule;
        this.table_classe = table_classe;
        this.table_niveaumodule = table_niveaumodule;
        this.table_heurmodule = table_heurmodule;
        this.table_heurabsence = table_heurabsence;
        this.table_nombreabsence = table_nombreabsence;
        this.table_enseignant = table_enseignant;
    }

    public String getTable_code() {
        return table_code;
    }

    public String getTable_nommodule() {
        return table_nommodule;
    }

    public String getTable_classe() {
        return table_classe;
    }

    public String getTable_niveaumodule() {
        return table_niveaumodule;
    }

    public String getTable_heurmodule() {
        return table_heurmodule;
    }

    public String getTable_heurabsence() {
        return table_heurabsence;
    }

    public String getTable_nombreabsence() {
        return table_nombreabsence;
    }

    public String getTable_enseignant() {
        return table_enseignant;
    }
}
