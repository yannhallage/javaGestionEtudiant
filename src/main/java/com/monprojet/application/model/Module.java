package com.monprojet.application.model;

public class Module {

	 	private String nomDuModule;
	    private String idModule;
	    private String ecueModule;
	    private String heurModule;
	    private String dispenseENModule;
	    private String classeModule;
	    
	    
	    public Module(String nomDuModule, String ecueModule, String heurModule, String dispenseENModule, String classeModule, String idModule) {
	        this.nomDuModule = nomDuModule;
	        this.ecueModule= ecueModule;
	        this.heurModule = heurModule;
	        this.dispenseENModule = dispenseENModule;
	        this.classeModule = classeModule;
	        this.idModule = idModule;
	    }
	    
	    // Getters et setters
	    public String getNomDuModule() {
	        return nomDuModule;
	    }

	    public void setNomDuModule(String nomDuModule) {
	        this.nomDuModule = nomDuModule;
	    }

	    public String getEcueModule() {
	        return ecueModule;
	    }

	    public void setEcueModule(String ecueModule) {
	        this.ecueModule = ecueModule;
	    }

	    public String getHeurModule() {
	        return heurModule;
	    }

	    public void setHeurModule(String heurModule) {
	        this.heurModule = heurModule;
	    }

	    public String getDispenseENModule() {
	        return dispenseENModule;
	    }

	    public void setDispenseENModule(String dispenseENModule) {
	        this.dispenseENModule = dispenseENModule;
	    }

	    public String getClasseModule() {
	        return classeModule;
	    }

	    public void setClasseModule(String classeModule) {
	        this.classeModule = classeModule;
	    }

	    public String getIdModule() {
	        return idModule;
	    }

	    public void setIdModule(String idModule) {
	        this.idModule = idModule;
	    }
}