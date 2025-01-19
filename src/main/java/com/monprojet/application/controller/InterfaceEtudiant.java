package com.monprojet.application.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
	
public class InterfaceEtudiant {

	 @FXML
	    private ImageView sedeconnecter;

	    @FXML
	    public void initialize() {
	        // Ajouter un événement de clic à l'image
	        sedeconnecter.setOnMouseClicked(this::onImageClick);
	    }

	    private void onImageClick(MouseEvent event) {
	        System.out.println("arrté l'applis via FXML !");
	        Platform.exit();
	        // Ajouter d'autres actions ici
	    }
}
