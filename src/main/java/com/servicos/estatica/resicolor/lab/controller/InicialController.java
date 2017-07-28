package com.servicos.estatica.resicolor.lab.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.effects.JFXDepthManager;
import com.servicos.estatica.resicolor.lab.app.ControlledScreen;
import com.servicos.estatica.resicolor.lab.modbus.ModbusRTUService;
import com.servicos.estatica.resicolor.lab.util.Chronometer;
import com.servicos.estatica.resicolor.lab.util.FxDialogs;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InicialController implements Initializable, ControlledScreen {

	@FXML
	private AnchorPane pane1;
	@FXML
	private AnchorPane pane2;
	@FXML
	private AnchorPane pane3;
	@FXML
	private Label lblTemp1;
	@FXML
	private Label lblTemp2;
	@FXML
	private Label lblTemp3;
	@FXML
	private Label txtSp1;
	@FXML
	private Label txtSp2;
	@FXML
	private Label txtSp3;
	@FXML
	private Label lblCrono1;
	@FXML
	private Label lblCrono2;
	@FXML
	private Label lblCrono3;
	@FXML
	private Button btConnect1;
	@FXML
	private Button btConnect2;
	@FXML
	private Button btConnect3;
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

	private static Image gifGlassFile = new Image("/com/servicos/estatica/resicolor/lab/style/glass.gif");
	private static Image imgGlassFile = new Image("/com/servicos/estatica/resicolor/lab/style/glass.png");

	private static Glow glow1 = new Glow(0);
	private static SepiaTone sepia1 = new SepiaTone(0);
	private static Glow glow2 = new Glow(0);
	private static SepiaTone sepia2 = new SepiaTone(0);
	private static Glow glow3 = new Glow(0);
	private static SepiaTone sepia3 = new SepiaTone(0);

	private static StrokeTransition chapaTransition1;
	private static StrokeTransition chapaTransition2;
	private static StrokeTransition chapaTransition3;
	private static Timeline tmlHeater1;
	private static Timeline tmlHeater2;
	private static Timeline tmlHeater3;
	private static Timeline scanModbusSlaves = new Timeline();
	private static Chronometer chrono1 = new Chronometer();
	private static Chronometer chrono2 = new Chronometer();
	private static Chronometer chrono3 = new Chronometer();

	private static Boolean isConnected1 = false;
	private static Boolean isConnected2 = false;
	private static Boolean isConnected3 = false;

	private static ModbusRTUService modService = new ModbusRTUService();

	private static Double tempReator = new Double(0);

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		configLayout();
		configAnimations();
		modService.setConnectionParams("COM9", 9600);
		modService.openConnection();
		initModbusReadSlaves();
		initModbusReadSlaves();
	}

	@FXML
	private void changeSp1() {
		String sp = FxDialogs.showTextInput("Set-point", "Balão 1", "Digite o set-point:", "");
		if (sp != null) {
			txtSp1.setText(sp);
		}
	}

	@FXML
	private void changeSp2() {
		String sp = FxDialogs.showTextInput("Set-point", "Balão 2", "Digite o set-point:", "");
		if (sp != null) {
			txtSp2.setText(sp);
		}
	}

	@FXML
	private void changeSp3() {
		String sp = FxDialogs.showTextInput("Set-point", "Balão 3", "Digite o set-point:", "");
		if (sp != null) {
			txtSp3.setText(sp);
		}
	}

	@FXML
	private void initProc1() {
		imgGlass1.setImage(gifGlassFile);
		imgMola1.setEffect(sepia1);
		scanModbusSlaves.play();
		chapaTransition1.play();
		tmlHeater1.play();
		chrono1.start(lblCrono1);
	}

	@FXML
	private void initProc2() {
		imgGlass2.setImage(gifGlassFile);
		imgMola2.setEffect(sepia2);
		chapaTransition2.play();
		tmlHeater2.play();
		chrono2.start(lblCrono2);
	}

	@FXML
	private void initProc3() {
		imgGlass3.setImage(gifGlassFile);
		imgMola3.setEffect(sepia3);
		tmlHeater3.play();
		chapaTransition3.play();
		chrono3.start(lblCrono3);
	}

	@FXML
	private void stopProc1() {
		scanModbusSlaves.stop();
		imgGlass1.setImage(imgGlassFile);
		chapaTransition1.stop();
		tmlHeater1.stop();
		imgMola1.setEffect(null);
		lnChapa1.setStroke(Color.RED);
		chrono1.stop();
	}

	@FXML
	private void stopProc2() {
		imgGlass2.setImage(imgGlassFile);
		tmlHeater2.stop();
		chapaTransition2.stop();
		imgMola2.setEffect(null);
		lnChapa2.setStroke(Color.RED);
		chrono2.stop();
	}

	@FXML
	private void stopProc3() {
		imgGlass3.setImage(imgGlassFile);
		tmlHeater3.stop();
		chapaTransition3.stop();
		imgMola3.setEffect(null);
		lnChapa3.setStroke(Color.RED);
		chrono3.stop();
	}

	@FXML
	private void toggleConnect1() {
		if (isConnected1) {
			isConnected1 = false;
			btConnect1.setStyle("-fx-graphic: url('/com/servicos/estatica/resicolor/lab/style/disconnect.png');");
		} else {
			isConnected1 = true;
			btConnect1.setStyle("-fx-graphic: url('/com/servicos/estatica/resicolor/lab/style/connect.png');");
		}
	}

	@FXML
	private void toggleConnect2() {
		if (isConnected2) {
			isConnected2 = false;
			btConnect2.setStyle("-fx-graphic: url('/com/servicos/estatica/resicolor/lab/style/disconnect.png');");
		} else {
			isConnected2 = true;
			btConnect2.setStyle("-fx-graphic: url('/com/servicos/estatica/resicolor/lab/style/connect.png');");
		}
	}

	@FXML
	private void toggleConnect3() {
		if (isConnected3) {
			isConnected3 = false;
			btConnect3.setStyle("-fx-graphic: url('/com/servicos/estatica/resicolor/lab/style/disconnect.png');");
		} else {
			isConnected3 = true;
			btConnect3.setStyle("-fx-graphic: url('/com/servicos/estatica/resicolor/lab/style/connect.png');");
		}
	}

	@FXML
	public void openConfigEnsaio() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/ConfigEnsaio.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Informações sobre o cliente");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(btConnect1.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
	}

	private void configLayout() {
		rect1.setFill(Color.TRANSPARENT);
		rect2.setFill(Color.TRANSPARENT);
		rect3.setFill(Color.TRANSPARENT);
		imgGlass1.setImage(imgGlassFile);
		imgGlass2.setImage(imgGlassFile);
		imgGlass3.setImage(imgGlassFile);
		JFXDepthManager.setDepth(imgGlass1, 5);
		JFXDepthManager.setDepth(imgGlass2, 5);
		JFXDepthManager.setDepth(imgGlass3, 5);
		JFXDepthManager.setDepth(imgNovus1, 5);
		JFXDepthManager.setDepth(imgNovus2, 5);
		JFXDepthManager.setDepth(imgNovus3, 5);
		JFXDepthManager.setDepth(pane1, 5);
		JFXDepthManager.setDepth(pane2, 5);
		JFXDepthManager.setDepth(pane3, 5);
		JFXDepthManager.setDepth(rect1, 5);
		JFXDepthManager.setDepth(rect2, 5);
		JFXDepthManager.setDepth(rect3, 5);
		sepia1.setInput(glow1);
		imgMola1.setEffect(sepia1);
		sepia2.setInput(glow2);
		imgMola2.setEffect(sepia2);
		sepia3.setInput(glow3);
		imgMola3.setEffect(sepia3);

	}

	private void configAnimations() {
		tmlHeater1 = new Timeline(
				new KeyFrame(Duration.ZERO, new KeyValue(glow1.levelProperty(), 0.3),
						new KeyValue(sepia1.levelProperty(), 0.3)),
				new KeyFrame(Duration.seconds(1), new KeyValue(glow1.levelProperty(), 0.85),
						new KeyValue(sepia1.levelProperty(), 0.85)));
		tmlHeater1.setCycleCount(Timeline.INDEFINITE);
		tmlHeater1.setAutoReverse(true);

		tmlHeater2 = new Timeline(
				new KeyFrame(Duration.ZERO, new KeyValue(glow2.levelProperty(), 0.3),
						new KeyValue(sepia2.levelProperty(), 0.3)),
				new KeyFrame(Duration.seconds(1), new KeyValue(glow2.levelProperty(), 0.85),
						new KeyValue(sepia2.levelProperty(), 0.85)));
		tmlHeater2.setCycleCount(Timeline.INDEFINITE);
		tmlHeater2.setAutoReverse(true);

		tmlHeater3 = new Timeline(
				new KeyFrame(Duration.ZERO, new KeyValue(glow3.levelProperty(), 0.3),
						new KeyValue(sepia3.levelProperty(), 0.3)),
				new KeyFrame(Duration.seconds(1), new KeyValue(glow3.levelProperty(), 0.85),
						new KeyValue(sepia3.levelProperty(), 0.85)));
		tmlHeater3.setCycleCount(Timeline.INDEFINITE);
		tmlHeater3.setAutoReverse(true);

		chapaTransition1 = new StrokeTransition(Duration.millis(1000), lnChapa1, Color.RED, Color.ORANGE);
		chapaTransition2 = new StrokeTransition(Duration.millis(1000), lnChapa2, Color.RED, Color.ORANGE);
		chapaTransition3 = new StrokeTransition(Duration.millis(1000), lnChapa3, Color.RED, Color.ORANGE);
		chapaTransition1.setCycleCount(Timeline.INDEFINITE);
		chapaTransition2.setCycleCount(Timeline.INDEFINITE);
		chapaTransition3.setCycleCount(Timeline.INDEFINITE);
		chapaTransition1.setAutoReverse(true);
		chapaTransition2.setAutoReverse(true);
		chapaTransition3.setAutoReverse(true);
	}

	private void initModbusReadSlaves() {
		scanModbusSlaves = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				tempReator = modService.readMultipleRegisters(1, 0, 1);
				lblTemp1.setText(tempReator.toString());
				// setPointReator = modService.readMultipleRegisters(slaveID, 1,
				// 1);
			}
		}));
		scanModbusSlaves.setCycleCount(Timeline.INDEFINITE);

	}

}
