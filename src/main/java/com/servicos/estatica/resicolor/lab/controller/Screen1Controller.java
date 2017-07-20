package com.servicos.estatica.resicolor.lab.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.app.ControlledScreen;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import zan.inc.custom.components.ImageViewResizer;

public class Screen1Controller implements Initializable, ControlledScreen {

	@FXML
	private AnchorPane mainPane;
	@FXML
	private ImageView imgEstatica;

	private static FadeTransition estaticaFadeTransition;
	private static ImageViewResizer estaticaResizer;

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		imgEstatica.setImage(new Image("/com/servicos/estatica/resicolor/lab/style/logotipo.png"));
		estaticaResizer = new ImageViewResizer(imgEstatica, 138, 42);
		estaticaResizer.setLayoutX(150.0);
		estaticaResizer.setLayoutY(150.0);
		estaticaResizer.setLayoutX(1083);
		estaticaResizer.setLayoutY(607);
		mainPane.getChildren().addAll(estaticaResizer);

		estaticaFadeTransition = new FadeTransition(Duration.millis(1000), imgEstatica);
		estaticaFadeTransition.setCycleCount(1);

	}

	@FXML
	private void handleImgEstaticaAction() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/EstaticaInfo.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Informações sobre o fabricante");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgEstatica.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
		estaticaFadeTransition.setFromValue(imgEstatica.getOpacity());
		estaticaFadeTransition.setToValue(0.2);
		estaticaFadeTransition.play();
	}

	@FXML
	public void hoverImgEstatica() {
		estaticaFadeTransition.setFromValue(0.2);
		estaticaFadeTransition.setToValue(1.0);
		estaticaFadeTransition.play();
	}

	@FXML
	public void unhoverImgEstatica() {
		estaticaFadeTransition.setFromValue(1.0);
		estaticaFadeTransition.setToValue(0.2);
		estaticaFadeTransition.play();
	}

}
