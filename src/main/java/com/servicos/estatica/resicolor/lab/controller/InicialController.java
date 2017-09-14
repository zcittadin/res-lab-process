package com.servicos.estatica.resicolor.lab.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.fazecast.jSerialComm.SerialPort;
import com.jfoenix.effects.JFXDepthManager;
import com.servicos.estatica.resicolor.lab.app.ControlledScreen;
import com.servicos.estatica.resicolor.lab.dao.LeituraDAO;
import com.servicos.estatica.resicolor.lab.dao.ProvaDAO;
import com.servicos.estatica.resicolor.lab.modbus.ModbusRTUService;
import com.servicos.estatica.resicolor.lab.model.Leitura;
import com.servicos.estatica.resicolor.lab.model.Projeto;
import com.servicos.estatica.resicolor.lab.model.Prova;
import com.servicos.estatica.resicolor.lab.property.AmostraProperty;
import com.servicos.estatica.resicolor.lab.property.ProvaProperty;
import com.servicos.estatica.resicolor.lab.property.UsedProjetosMap;
import com.servicos.estatica.resicolor.lab.util.Baloes;
import com.servicos.estatica.resicolor.lab.util.Chronometer;
import com.servicos.estatica.resicolor.lab.util.FxDialogs;
import com.servicos.estatica.resicolor.lab.util.Toast;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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

@SuppressWarnings("unused")
public class InicialController extends Balao1Controller implements Initializable, ControlledScreen {

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		configLayout();
		configAnimations();
		configModbusReadSlaves();
		getComPorts();
	}

	@FXML
	private void toggleConnect() {
		if (isConnected) {
			scanModbusSlaves.stop();
			modService.closeConnection();
			isConnected = false;
			chkBalao1.setDisable(true);
			chkBalao2.setDisable(true);
			chkBalao3.setDisable(true);
			btConnect.setStyle("-fx-graphic: url('/com/servicos/estatica/resicolor/lab/style/disconnect.png');");
			btConnect.setText("Conectar");
			tabForm.setDisable(true);
		} else {
			modService.setConnectionParams(comboPorts.getValue(), 9600);
			modService.openConnection();

			chkBalao1.setDisable(false);
			chkBalao2.setDisable(false);
			chkBalao3.setDisable(false);
			firstScan();

			scanModbusSlaves.play();
			isConnected = true;
			btConnect.setStyle("-fx-graphic: url('/com/servicos/estatica/resicolor/lab/style/connect.png');");
			btConnect.setText("Desconectar");
			tabForm.setDisable(false);
		}
	}

	private void firstScan() {
		if (chkBalao1.isSelected()) {
			tempBalao1 = modService.readMultipleRegisters(1, 1, 1);
			spBalao1 = modService.readMultipleRegisters(1, 0, 1);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblTemp1.setText(tempBalao1.toString());
					lblSp1.setText(spBalao1.toString());
				}
			});
		}
		if (chkBalao2.isSelected()) {
			tempBalao2 = modService.readMultipleRegisters(2, 1, 1);
			spBalao2 = modService.readMultipleRegisters(2, 0, 1);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblTemp2.setText(tempBalao2.toString());
					lblSp2.setText(spBalao2.toString());
				}
			});
		}
		if (chkBalao3.isSelected()) {
			tempBalao3 = modService.readMultipleRegisters(3, 1, 1);
			spBalao3 = modService.readMultipleRegisters(3, 0, 1);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblTemp3.setText(tempBalao3.toString());
					lblSp3.setText(spBalao3.toString());
				}
			});
		}
	}

	private void configLayout() {
		rect1.setFill(Color.TRANSPARENT);
		rect2.setFill(Color.TRANSPARENT);
		rect3.setFill(Color.TRANSPARENT);
		imgGlass1.setImage(imgGlassFile);
		imgGlass2.setImage(imgGlassFile);
		imgGlass3.setImage(imgGlassFile);
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

	private void configModbusReadSlaves() {
		scanModbusSlaves = new Timeline(new KeyFrame(Duration.millis(5000), new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

				Task<Void> readTask = new Task<Void>() {

					@Override
					protected Void call() throws Exception {
						if (chkBalao1.isSelected()) {
							tempBalao1 = modService.readMultipleRegisters(1, 1, 1);
							spBalao1 = modService.readMultipleRegisters(1, 0, 1);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									lblTemp1.setText(tempBalao1.toString());
									lblSp1.setText(spBalao1.toString());
								}
							});
						}
						if (chkBalao2.isSelected()) {
							tempBalao2 = modService.readMultipleRegisters(2, 1, 1);
							spBalao2 = modService.readMultipleRegisters(2, 0, 1);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									lblTemp2.setText(tempBalao2.toString());
									lblSp2.setText(spBalao2.toString());
								}
							});
						}
						if (chkBalao3.isSelected()) {
							tempBalao3 = modService.readMultipleRegisters(3, 1, 1);
							spBalao3 = modService.readMultipleRegisters(3, 0, 1);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									lblTemp3.setText(tempBalao3.toString());
									lblSp3.setText(spBalao3.toString());
								}
							});
						}
						return null;
					}
				};
				Thread t = new Thread(readTask);
				t.start();

				scanInterval++;

				if (scanInterval == 24) {
					if (isBalaoRunning1) {
						Leitura l = new Leitura(null, prova1, Calendar.getInstance().getTime(), tempBalao1, spBalao1);
						leituraDAO.saveLeitura(l);
						prova1.getLeituras().add(l);
						calculaMinMax1();
						ProvaProperty.provaTemp1Property().set(!tempProva1Changed);
						tempProva1Changed = !tempProva1Changed;
					}
					if (isBalaoRunning2) {
						Leitura l = new Leitura(null, prova2, Calendar.getInstance().getTime(), tempBalao2, spBalao2);
						leituraDAO.saveLeitura(l);
						prova2.getLeituras().add(l);
						calculaMinMax2();
						ProvaProperty.provaTemp2Property().set(!tempProva2Changed);
						tempProva2Changed = !tempProva2Changed;
					}
					if (isBalaoRunning3) {
						Leitura l = new Leitura(null, prova3, Calendar.getInstance().getTime(), tempBalao3, spBalao3);
						leituraDAO.saveLeitura(l);
						prova3.getLeituras().add(l);
						calculaMinMax3();
						ProvaProperty.provaTemp3Property().set(!tempProva3Changed);
						tempProva3Changed = !tempProva3Changed;
					}
					scanInterval = 0;
				}
			}
		}));
		scanModbusSlaves.setCycleCount(Timeline.INDEFINITE);
	}

	private void getComPorts() {
		SerialPort[] ports = modService.getComPorts();
		availablePorts = FXCollections.observableArrayList();
		for (SerialPort port : ports) {
			availablePorts.add(port.getSystemPortName());
		}
		if (!availablePorts.isEmpty()) {
			comboPorts.setItems(availablePorts);
			comboPorts.setValue(availablePorts.get(0));
		} else {
			btConnect.setDisable(Boolean.TRUE);
			comboPorts.setValue("COM indisponível");
		}
		tabForm.setDisable(true);
	}

}
