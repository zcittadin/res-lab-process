package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.effects.JFXDepthManager;
import com.servicos.estatica.resicolor.lab.app.ControlledScreen;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
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
	private ImageView imgNovus1;
	@FXML
	private ImageView imgNovus2;
	@FXML
	private ImageView imgNovus3;
	@FXML
	private ImageView imgMola3;
	@FXML
	private Rectangle rect1;
	@FXML
	private Rectangle rect2;
	@FXML
	private Rectangle rect3;
	@FXML
	private Line lnChapa1;
	@FXML
	private Line lnChapa2;
	@FXML
	private Line lnChapa3;

	Glow glow = new Glow(0);
	SepiaTone sepia = new SepiaTone(0);

	private static StrokeTransition chapaTransition1;
	private static StrokeTransition chapaTransition2;
	private static StrokeTransition chapaTransition3;
	private static Timeline tmlAcende;

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		configLayout();
		configAnimations();
	}

	private void configLayout() {
		rect1.setFill(Color.TRANSPARENT);
		rect2.setFill(Color.TRANSPARENT);
		rect3.setFill(Color.TRANSPARENT);
		imgGlass1.setImage(new Image("/com/servicos/estatica/resicolor/lab/style/glass.gif"));
		imgGlass2.setImage(new Image("/com/servicos/estatica/resicolor/lab/style/glass.gif"));
		imgGlass3.setImage(new Image("/com/servicos/estatica/resicolor/lab/style/glass.gif"));
		JFXDepthManager.setDepth(imgGlass1, 5);
		JFXDepthManager.setDepth(imgGlass2, 5);
		JFXDepthManager.setDepth(imgGlass3, 5);
		JFXDepthManager.setDepth(imgNovus1, 5);
		JFXDepthManager.setDepth(imgNovus2, 5);
		JFXDepthManager.setDepth(imgNovus3, 5);
		JFXDepthManager.setDepth(rect1, 5);
		JFXDepthManager.setDepth(rect2, 5);
		JFXDepthManager.setDepth(rect3, 5);
		sepia.setInput(glow);
		imgMola1.setEffect(sepia);
		imgMola2.setEffect(sepia);
		imgMola3.setEffect(sepia);

	}

	private void configAnimations() {
		tmlAcende = new Timeline(
				new KeyFrame(Duration.ZERO, new KeyValue(glow.levelProperty(), 0.3),
						new KeyValue(sepia.levelProperty(), 0.3)),
				new KeyFrame(Duration.seconds(1), new KeyValue(glow.levelProperty(), 0.85),
						new KeyValue(sepia.levelProperty(), 0.85)));
		tmlAcende.setCycleCount(Timeline.INDEFINITE);
		tmlAcende.setAutoReverse(true);
		tmlAcende.play();
		
		chapaTransition1 = new StrokeTransition(Duration.millis(1000), lnChapa1, Color.RED, Color.ORANGE);
		chapaTransition2 = new StrokeTransition(Duration.millis(1000), lnChapa2, Color.RED, Color.ORANGE);
		chapaTransition3 = new StrokeTransition(Duration.millis(1000), lnChapa3, Color.RED, Color.ORANGE);
		chapaTransition1.setCycleCount(Timeline.INDEFINITE);
		chapaTransition2.setCycleCount(Timeline.INDEFINITE);
		chapaTransition3.setCycleCount(Timeline.INDEFINITE);
		chapaTransition1.setAutoReverse(true);
		chapaTransition2.setAutoReverse(true);
		chapaTransition3.setAutoReverse(true);
		chapaTransition1.play();
		chapaTransition2.play();
		chapaTransition3.play();
	}
}
