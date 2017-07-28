package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.app.ControlledScreen;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class InicialController implements Initializable, ControlledScreen {

	@FXML
	private ImageView imgGlass1;
	@FXML
	private ImageView imgMola1;
	@FXML
	private ImageView imgGlass2;
	@FXML
	private ImageView imgMola2;
	@FXML
	private ImageView imgGlass3;
	@FXML
	private ImageView imgMola3;

	Glow glow = new Glow(0);
	SepiaTone sepia = new SepiaTone(0);

	Timeline tmlAcende;

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		imgGlass1.setImage(new Image("/com/servicos/estatica/resicolor/lab/style/glass.gif"));
		imgGlass2.setImage(new Image("/com/servicos/estatica/resicolor/lab/style/glass.gif"));
		imgGlass3.setImage(new Image("/com/servicos/estatica/resicolor/lab/style/glass.gif"));
		sepia.setInput(glow);
		imgMola1.setEffect(sepia);
		imgMola2.setEffect(sepia);
		imgMola3.setEffect(sepia);

		tmlAcende = new Timeline(
				new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.3),
						new KeyValue(sepia.levelProperty(), 0.3)),
				new KeyFrame(Duration.seconds(1), new KeyValue(glow.levelProperty(), 0.85),
						new KeyValue(sepia.levelProperty(), 0.85)));
		tmlAcende.setCycleCount(Timeline.INDEFINITE);
		tmlAcende.setAutoReverse(true);

		tmlAcende.play();

	}
}
