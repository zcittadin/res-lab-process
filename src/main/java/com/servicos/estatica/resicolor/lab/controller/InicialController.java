package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

import com.fazecast.jSerialComm.SerialPort;
import com.jfoenix.effects.JFXDepthManager;
import com.servicos.estatica.resicolor.lab.app.ControlledScreen;
import com.servicos.estatica.resicolor.lab.dao.EnsaioDAO;
import com.servicos.estatica.resicolor.lab.dao.LeituraDAO;
import com.servicos.estatica.resicolor.lab.modbus.ModbusRTUService;
import com.servicos.estatica.resicolor.lab.model.Ensaio;
import com.servicos.estatica.resicolor.lab.model.Leitura;
import com.servicos.estatica.resicolor.lab.property.EnsaioProperty;
import com.servicos.estatica.resicolor.lab.util.Chronometer;
import com.servicos.estatica.resicolor.lab.util.FxDialogs;
import com.servicos.estatica.resicolor.lab.util.Toast;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class InicialController implements Initializable, ControlledScreen {

	@FXML
	private AnchorPane pane1;
	@FXML
	private AnchorPane pane2;
	@FXML
	private TabPane tabForm;
	@FXML
	private AnchorPane pane3;
	@FXML
	private TextField txtLoteBalao1;
	@FXML
	private TextField txtLoteBalao2;
	@FXML
	private TextField txtLoteBalao3;
	@FXML
	private Label lblTemp1;
	@FXML
	private Label lblTemp2;
	@FXML
	private Label lblTemp3;
	@FXML
	private Label lblSp1;
	@FXML
	private Label lblSp2;
	@FXML
	private Label lblSp3;
	@FXML
	private Label lblCrono1;
	@FXML
	private Label lblCrono2;
	@FXML
	private Label lblCrono3;
	@FXML
	private ComboBox<String> comboPorts;
	@FXML
	private Button btAdd1;
	@FXML
	private Button btAdd2;
	@FXML
	private Button btAdd3;
	@FXML
	private Button btConnect;
	@FXML
	private Button btPlay1;
	@FXML
	private Button btPlay2;
	@FXML
	private Button btPlay3;
	@FXML
	private Button btStop1;
	@FXML
	private Button btStop2;
	@FXML
	private Button btStop3;
	@FXML
	private Button btChart1;
	@FXML
	private Button btChart2;
	@FXML
	private Button btChart3;
	@FXML
	private Button btReport1;
	@FXML
	private Button btReport2;
	@FXML
	private Button btReport3;
	@FXML
	private Button btSaveBalao1;
	@FXML
	private Button btSaveBalao2;
	@FXML
	private Button btSaveBalao3;
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
	@FXML
	private ProgressIndicator prog1;
	@FXML
	private ProgressIndicator prog2;
	@FXML
	private ProgressIndicator prog3;

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

	private static Boolean isConnected = false;
	private static Boolean isBalaoReady1 = false;
	private static Boolean isBalaoReady2 = false;
	private static Boolean isBalaoReady3 = false;
	private static Boolean isBalaoRunning1 = false;
	private static Boolean isBalaoRunning2 = false;
	private static Boolean isBalaoRunning3 = false;
	private static Boolean isBalaoFinished1 = false;
	private static Boolean isBalaoFinished2 = false;
	private static Boolean isBalaoFinished3 = false;

	private static Boolean tempEnsaio1Changed = false;
	private static Boolean tempEnsaio2Changed = false;
	private static Boolean tempEnsaio3Changed = false;

	private static Boolean ensaio1Clear = false;
	private static Boolean ensaio2Clear = false;
	private static Boolean ensaio3Clear = false;

	private static ModbusRTUService modService = new ModbusRTUService();

	private static Double tempBalao1 = new Double(0);
	private static Double spBalao1 = new Double(0);
	private static ObservableList<String> availablePorts;

	private static Ensaio ensaio1;
	private static Ensaio ensaio2;
	private static Ensaio ensaio3;

	private static EnsaioDAO ensaioDAO = new EnsaioDAO();
	private static LeituraDAO leituraDAO = new LeituraDAO();

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
	public void saveBalao1() {
		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				prog1.setVisible(true);
				btSaveBalao1.setDisable(true);
				btAdd1.setDisable(true);
				txtLoteBalao1.setDisable(true);
				ensaio1 = new Ensaio(null, new ArrayList<Leitura>(), txtLoteBalao1.getText(), "Balão 1", 0, 0, null,
						null);
				ensaioDAO.saveEnsaio(ensaio1);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog1.setVisible(false);
				btPlay1.setDisable(false);
				btChart1.setDisable(false);
				btReport1.setDisable(false);
				isBalaoReady1 = true;
				makeToast("Ensaio salvo com sucesso.");
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog1.setVisible(true);
				isBalaoReady1 = false;
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	@FXML
	public void saveBalao2() {
		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				prog2.setVisible(true);
				btSaveBalao2.setDisable(true);
				btAdd2.setDisable(true);
				txtLoteBalao2.setDisable(true);
				ensaio2 = new Ensaio(null, new ArrayList<Leitura>(), txtLoteBalao2.getText(), "Balão 2", 0, 0, null,
						null);
				ensaioDAO.saveEnsaio(ensaio2);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog2.setVisible(false);
				btPlay2.setDisable(false);
				btChart2.setDisable(false);
				btReport2.setDisable(false);
				isBalaoReady2 = true;
				makeToast("Ensaio salvo com sucesso.");
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog2.setVisible(false);
				btSaveBalao2.setDisable(false);
				isBalaoReady2 = false;
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	@FXML
	public void saveBalao3() {
		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				prog3.setVisible(true);
				btSaveBalao3.setDisable(true);
				btAdd3.setDisable(true);
				txtLoteBalao3.setDisable(true);
				ensaio3 = new Ensaio(null, new ArrayList<Leitura>(), txtLoteBalao3.getText(), "Balão 3", 0, 0, null,
						null);
				ensaioDAO.saveEnsaio(ensaio3);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog3.setVisible(false);
				btPlay3.setDisable(false);
				btChart3.setDisable(false);
				btReport3.setDisable(false);
				isBalaoReady3 = true;
				makeToast("Ensaio salvo com sucesso.");
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog3.setVisible(false);
				btSaveBalao3.setDisable(false);
				btAdd3.setDisable(false);
				isBalaoReady3 = false;
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	@FXML
	private void changeSp1() {
		String sp = FxDialogs.showTextInput("Set-point", "Balão 1", "Digite o set-point:", "");
		if (sp != null) {
			modService.writeSingleRegister(1, 0, Integer.parseInt(sp));
			lblSp1.setText(sp);
		}
	}

	@FXML
	private void changeSp2() {
		String sp = FxDialogs.showTextInput("Set-point", "Balão 2", "Digite o set-point:", "");
		if (sp != null) {
			modService.writeSingleRegister(1, 0, Integer.parseInt(sp));
			lblSp2.setText(sp);
		}
	}

	@FXML
	private void changeSp3() {
		String sp = FxDialogs.showTextInput("Set-point", "Balão 3", "Digite o set-point:", "");
		if (sp != null) {
			modService.writeSingleRegister(1, 0, Integer.parseInt(sp));
			lblSp3.setText(sp);
		}
	}

	@FXML
	private void initProc1() {
		btPlay1.setDisable(true);
		btStop1.setDisable(false);
		imgGlass1.setImage(gifGlassFile);
		imgMola1.setEffect(sepia1);
		chapaTransition1.play();
		tmlHeater1.play();
		chrono1.start(lblCrono1);
		isBalaoRunning1 = true;
		isBalaoReady1 = false;
	}

	@FXML
	private void initProc2() {
		btPlay2.setDisable(true);
		btStop2.setDisable(false);
		imgGlass2.setImage(gifGlassFile);
		imgMola2.setEffect(sepia2);
		chapaTransition2.play();
		tmlHeater2.play();
		chrono2.start(lblCrono2);
		isBalaoRunning2 = true;
		isBalaoReady2 = false;
	}

	@FXML
	private void initProc3() {
		btPlay3.setDisable(true);
		btStop3.setDisable(false);
		imgGlass3.setImage(gifGlassFile);
		imgMola3.setEffect(sepia3);
		tmlHeater3.play();
		chapaTransition3.play();
		chrono3.start(lblCrono3);
		isBalaoRunning3 = true;
		isBalaoReady3 = false;
	}

	@FXML
	private void stopProc1() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente encerrar o ensaio em andamento?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		imgGlass1.setImage(imgGlassFile);
		chapaTransition1.stop();
		tmlHeater1.stop();
		imgMola1.setEffect(null);
		lnChapa1.setStroke(Color.RED);
		chrono1.stop();
		btAdd1.setDisable(false);
		btPlay1.setDisable(true);
		btStop1.setDisable(true);
		btChart1.setDisable(true);
		btReport1.setDisable(true);
		isBalaoRunning1 = false;
		isBalaoFinished1 = true;
		makeToast("Balão 1: Ensaio encerrado.");
	}

	@FXML
	private void stopProc2() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente encerrar o ensaio em andamento?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		imgGlass2.setImage(imgGlassFile);
		tmlHeater2.stop();
		chapaTransition2.stop();
		imgMola2.setEffect(null);
		lnChapa2.setStroke(Color.RED);
		chrono2.stop();
		btAdd2.setDisable(false);
		btPlay2.setDisable(true);
		btStop2.setDisable(true);
		btChart2.setDisable(true);
		btReport2.setDisable(true);
		isBalaoRunning2 = false;
		isBalaoFinished2 = true;
		makeToast("Balão 2: Ensaio encerrado.");
	}

	@FXML
	private void stopProc3() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente encerrar o ensaio em andamento?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		imgGlass3.setImage(imgGlassFile);
		tmlHeater3.stop();
		chapaTransition3.stop();
		imgMola3.setEffect(null);
		lnChapa3.setStroke(Color.RED);
		chrono3.stop();
		btAdd3.setDisable(false);
		btPlay3.setDisable(true);
		btStop3.setDisable(true);
		btChart3.setDisable(true);
		btReport3.setDisable(true);
		isBalaoRunning3 = false;
		isBalaoFinished3 = true;
		makeToast("Balão 3: Ensaio encerrado.");
	}

	@FXML
	private void toggleConnect() {
		if (isConnected) {
			scanModbusSlaves.stop();
			modService.closeConnection();
			isConnected = false;
			btConnect.setStyle("-fx-graphic: url('/com/servicos/estatica/resicolor/lab/style/disconnect.png');");
			btConnect.setText("Conectar");
			tabForm.setDisable(true);
		} else {
			modService.setConnectionParams(comboPorts.getValue(), 9600);
			modService.openConnection();
			
			tempBalao1 = modService.readMultipleRegisters(1, 1, 1);
			Integer i = tempBalao1.intValue();
			lblTemp1.setText(i.toString());
			lblTemp2.setText(i.toString());
			lblTemp3.setText(i.toString());
			spBalao1 = modService.readMultipleRegisters(1, 0, 1);
			Integer sp = spBalao1.intValue();
			lblSp1.setText(sp.toString());
			lblSp2.setText(sp.toString());
			lblSp3.setText(sp.toString());
			
			scanModbusSlaves.play();
			isConnected = true;
			btConnect.setStyle("-fx-graphic: url('/com/servicos/estatica/resicolor/lab/style/connect.png');");
			btConnect.setText("Desconectar");
			tabForm.setDisable(false);
		}
	}

	@FXML
	private void addEnsaio1() {
		EnsaioProperty.ensaioClear1Property().set(!ensaio1Clear);
		ensaio1Clear = !ensaio1Clear;
		ensaio1 = null;
		isBalaoFinished1 = false;
		txtLoteBalao1.setDisable(false);
		btSaveBalao1.setDisable(false);
		txtLoteBalao1.setText(null);
		txtLoteBalao1.requestFocus();
	}

	@FXML
	private void addEnsaio2() {
		EnsaioProperty.ensaioClear2Property().set(!ensaio2Clear);
		ensaio2Clear = !ensaio2Clear;
		ensaio2 = null;
		isBalaoFinished2 = false;
		txtLoteBalao2.setDisable(false);
		btSaveBalao2.setDisable(false);
		txtLoteBalao2.setText(null);
		txtLoteBalao2.requestFocus();
	}

	@FXML
	private void addEnsaio3() {
		EnsaioProperty.ensaioClear3Property().set(!ensaio3Clear);
		ensaio3Clear = !ensaio3Clear;
		ensaio3 = null;
		isBalaoFinished3 = false;
		txtLoteBalao3.setDisable(false);
		btSaveBalao3.setDisable(false);
		txtLoteBalao3.setText(null);
		txtLoteBalao3.requestFocus();
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
				tempBalao1 = modService.readMultipleRegisters(1, 1, 1);
				Integer i = tempBalao1.intValue();
				lblTemp1.setText(i.toString());
				lblTemp2.setText(i.toString());
				lblTemp3.setText(i.toString());
				// setPointReator = modService.readMultipleRegisters(slaveID, 1,
				// 1);

				if (isBalaoRunning1) {
					Leitura l = new Leitura(null, ensaio1, Calendar.getInstance().getTime(), tempBalao1, 0);
					leituraDAO.saveLeitura(l);
					ensaio1.getLeituras().add(l);
					EnsaioProperty.ensaioTemp1Property().set(!tempEnsaio1Changed);
					tempEnsaio1Changed = !tempEnsaio1Changed;
				}
				if (isBalaoRunning2) {
					Leitura l = new Leitura(null, ensaio2, Calendar.getInstance().getTime(), tempBalao1, 0);
					leituraDAO.saveLeitura(l);
					ensaio2.getLeituras().add(l);
					EnsaioProperty.ensaioTemp2Property().set(!tempEnsaio2Changed);
					tempEnsaio2Changed = !tempEnsaio2Changed;
				}
				if (isBalaoRunning3) {
					Leitura l = new Leitura(null, ensaio3, Calendar.getInstance().getTime(), tempBalao1, 0);
					leituraDAO.saveLeitura(l);
					ensaio3.getLeituras().add(l);
					EnsaioProperty.ensaioTemp3Property().set(!tempEnsaio3Changed);
					tempEnsaio3Changed = !tempEnsaio3Changed;
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

	private void makeToast(String message) {
		String toastMsg = message;
		int toastMsgTime = 5000;
		int fadeInTime = 600;
		int fadeOutTime = 600;
		Stage stage = (Stage) tabForm.getScene().getWindow();
		Toast.makeToast(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
	}

	public Double getTemp() {
		return tempBalao1;
	}

}
