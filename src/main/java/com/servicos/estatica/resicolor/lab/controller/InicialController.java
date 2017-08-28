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
	private TextField txtProjeto1;
	@FXML
	private TextField txtProjeto2;
	@FXML
	private TextField txtProjeto3;
	@FXML
	private TextField txtProduto1;
	@FXML
	private TextField txtProduto2;
	@FXML
	private TextField txtProduto3;
	@FXML
	private TextField txtNumero1;
	@FXML
	private TextField txtNumero2;
	@FXML
	private TextField txtNumero3;
	@FXML
	private TextField txtExecutor1;
	@FXML
	private TextField txtExecutor2;
	@FXML
	private TextField txtExecutor3;
	@FXML
	private TextArea txtObjetivo1;
	@FXML
	private TextArea txtObjetivo2;
	@FXML
	private TextArea txtObjetivo3;
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
	private Label lblTempMin1;
	@FXML
	private Label lblTempMax1;
	@FXML
	private Label lblTempMin2;
	@FXML
	private Label lblTempMax2;
	@FXML
	private Label lblTempMin3;
	@FXML
	private Label lblTempMax3;
	@FXML
	private Label lblInicio1;
	@FXML
	private Label lblInicio2;
	@FXML
	private Label lblInicio3;
	@FXML
	private ComboBox<String> comboPorts;
	@FXML
	private Button btAddProjeto1;
	@FXML
	private Button btAddProjeto2;
	@FXML
	private Button btAddProjeto3;
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
	private Button btAmostra1;
	@FXML
	private Button btAmostra2;
	@FXML
	private Button btAmostra3;
	@FXML
	private Button btSaveBalao1;
	@FXML
	private Button btSaveBalao2;
	@FXML
	private Button btSaveBalao3;
	@FXML
	private Button btExcluir1;
	@FXML
	private Button btExcluir2;
	@FXML
	private Button btExcluir3;
	@FXML
	private Button btSp1;
	@FXML
	private Button btSp2;
	@FXML
	private Button btSp3;
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

	private static Boolean tempProva1Changed = false;
	private static Boolean tempProva2Changed = false;
	private static Boolean tempProva3Changed = false;

	private static Boolean prova1Clear = false;
	private static Boolean prova2Clear = false;
	private static Boolean prova3Clear = false;

	private static ModbusRTUService modService = new ModbusRTUService();

	private static Integer tempBalao1 = new Integer(0);
	private static Integer tempBalao2 = new Integer(0);
	private static Integer tempBalao3 = new Integer(0);
	private static Integer spBalao1 = new Integer(0);
	private static Integer spBalao2 = new Integer(0);
	private static Integer spBalao3 = new Integer(0);

	private static Double tempMin1 = new Double(1900);
	private static Double tempMax1 = new Double(0);
	private static Double tempMin2 = new Double(1900);
	private static Double tempMax2 = new Double(0);
	private static Double tempMin3 = new Double(1900);
	private static Double tempMax3 = new Double(0);

	private static ObservableList<String> availablePorts;

	private static Projeto projeto1;
	private static Projeto projeto2;
	private static Projeto projeto3;

	private static Prova prova1;
	private static Prova prova2;
	private static Prova prova3;

	private static List<Leitura> leituras1;
	private static List<Leitura> leituras2;
	private static List<Leitura> leituras3;

	private static ProvaDAO provaDAO = new ProvaDAO();
	private static LeituraDAO leituraDAO = new LeituraDAO();

	private static DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

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
		if (txtProduto1.getText() == null || txtProduto1.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o produto.");
			txtProduto1.requestFocus();
			return;
		}
		if (txtNumero1.getText() == null || txtNumero1.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o número do projeto.");
			txtNumero1.requestFocus();
			return;
		}
		if (txtExecutor1.getText() == null || txtExecutor1.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o executor da prova.");
			txtExecutor1.requestFocus();
			return;
		}
		if (txtObjetivo1.getText() == null || txtObjetivo1.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o objetivo da prova.");
			txtObjetivo1.requestFocus();
			return;
		}

		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				prog1.setVisible(true);
				btSaveBalao1.setDisable(true);
				btExcluir1.setDisable(true);
				btAddProjeto1.setDisable(true);
				txtProduto1.setDisable(true);
				txtNumero1.setDisable(true);
				txtExecutor1.setDisable(true);
				txtObjetivo1.setDisable(true);
				leituras1 = new ArrayList<>();
				prova1 = new Prova(null, projeto1, leituras1, null, txtProduto1.getText(), txtObjetivo1.getText(),
						txtExecutor1.getText(), txtNumero1.getText(), "Balão 1", 0, 0, null, null);
				provaDAO.saveProva(prova1);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog1.setVisible(false);
				btPlay1.setDisable(false);
				btAmostra1.setDisable(false);
				btExcluir1.setDisable(false);
				isBalaoReady1 = true;
				makeToast("Prova registrada com sucesso.");
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog1.setVisible(true);
				btSaveBalao1.setDisable(false);
				btAddProjeto1.setDisable(false);
				isBalaoReady1 = false;
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	@FXML
	public void saveBalao2() {
		if (txtProduto2.getText() == null || txtProduto2.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o produto.");
			txtProduto2.requestFocus();
			return;
		}
		if (txtNumero2.getText() == null || txtNumero2.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o número do projeto.");
			txtNumero2.requestFocus();
			return;
		}
		if (txtExecutor2.getText() == null || txtExecutor2.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o executor da prova.");
			txtExecutor2.requestFocus();
			return;
		}
		if (txtObjetivo2.getText() == null || txtObjetivo2.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o objetivo da prova.");
			txtObjetivo2.requestFocus();
			return;
		}

		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				prog2.setVisible(true);
				btSaveBalao2.setDisable(true);
				btExcluir2.setDisable(true);
				btAddProjeto2.setDisable(true);
				txtProduto2.setDisable(true);
				txtNumero2.setDisable(true);
				txtExecutor2.setDisable(true);
				txtObjetivo2.setDisable(true);
				leituras2 = new ArrayList<>();
				prova2 = new Prova(null, projeto2, leituras2, null, txtProduto2.getText(), txtObjetivo2.getText(),
						txtExecutor2.getText(), txtNumero2.getText(), "Balão 2", 0, 0, null, null);
				provaDAO.saveProva(prova2);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog2.setVisible(false);
				btPlay2.setDisable(false);
				btAmostra2.setDisable(false);
				btExcluir2.setDisable(false);
				isBalaoReady2 = true;
				makeToast("Prova registrada com sucesso.");
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog2.setVisible(false);
				btSaveBalao2.setDisable(false);
				btAddProjeto2.setDisable(false);
				isBalaoReady2 = false;
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	@FXML
	public void saveBalao3() {
		if (txtProduto3.getText() == null || txtProduto3.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o produto.");
			txtProduto3.requestFocus();
			return;
		}
		if (txtNumero3.getText() == null || txtNumero3.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o número do projeto.");
			txtNumero3.requestFocus();
			return;
		}
		if (txtExecutor3.getText() == null || txtExecutor3.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o executor da prova.");
			txtExecutor3.requestFocus();
			return;
		}
		if (txtObjetivo3.getText() == null || txtObjetivo3.getText().trim().equals("")) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe o objetivo da prova.");
			txtObjetivo3.requestFocus();
			return;
		}

		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				prog3.setVisible(true);
				btSaveBalao3.setDisable(true);
				btExcluir3.setDisable(true);
				btAddProjeto3.setDisable(true);
				txtProduto3.setDisable(true);
				txtNumero3.setDisable(true);
				txtExecutor3.setDisable(true);
				txtObjetivo3.setDisable(true);
				leituras3 = new ArrayList<>();
				prova3 = new Prova(null, projeto3, leituras3, null, txtProduto3.getText(), txtObjetivo3.getText(),
						txtExecutor3.getText(), txtNumero3.getText(), "Balão 3", 0, 0, null, null);
				provaDAO.saveProva(prova3);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog3.setVisible(false);
				btPlay3.setDisable(false);
				btAmostra3.setDisable(false);
				btExcluir3.setDisable(false);
				isBalaoReady3 = true;
				makeToast("Prova registrada com sucesso.");
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog3.setVisible(false);
				btSaveBalao3.setDisable(false);
				btAddProjeto3.setDisable(false);
				isBalaoReady3 = false;
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	@FXML
	private void removeProva1() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar exclusão");
		alert.setHeaderText("Deseja realmente excluir esta prova?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		if (prova1 != null) {
			provaDAO.removeProva(prova1);
			txtProjeto1.clear();
			txtProduto1.clear();
			txtNumero1.clear();
			txtExecutor1.clear();
			txtObjetivo1.clear();
			btAddProjeto1.setDisable(false);
			btAmostra1.setDisable(true);
			btExcluir1.setDisable(true);
			UsedProjetosMap.discardProjetoByBalao(Baloes.BALAO1);
			makeToast("Prova removida com sucesso.");
			prova1 = null;
		}
	}

	@FXML
	private void removeProva2() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar exclusão");
		alert.setHeaderText("Deseja realmente excluir esta prova?");
		Optional<ButtonType> result = alert.showAndWait();
		if (prova2 != null) {
			provaDAO.removeProva(prova2);
			txtProjeto2.clear();
			txtProduto2.clear();
			txtNumero2.clear();
			txtExecutor2.clear();
			txtObjetivo2.clear();
			btAddProjeto2.setDisable(false);
			btAmostra2.setDisable(true);
			btExcluir2.setDisable(true);
			UsedProjetosMap.discardProjetoByBalao(Baloes.BALAO2);
			makeToast("Prova removida com sucesso.");
			prova2 = null;
		}
	}

	@FXML
	private void removeProva3() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar exclusão");
		alert.setHeaderText("Deseja realmente excluir esta prova?");
		Optional<ButtonType> result = alert.showAndWait();
		if (prova3 != null) {
			provaDAO.removeProva(prova3);
			txtProjeto3.clear();
			txtProduto3.clear();
			txtNumero3.clear();
			txtExecutor3.clear();
			txtObjetivo3.clear();
			btAddProjeto3.setDisable(false);
			btAmostra3.setDisable(true);
			btExcluir3.setDisable(true);
			UsedProjetosMap.discardProjetoByBalao(Baloes.BALAO3);
			makeToast("Prova removida com sucesso.");
			prova3 = null;
		}
	}

	@FXML
	private void changeSp1() {
		if (isBalaoRunning1 || isBalaoReady1) {
			String sp = FxDialogs.showNumericInput("Set-point", "Balão 1", "Digite o set-point:", "");
			if (sp != null) {
				modService.writeSingleRegister(1, 0, Integer.parseInt(sp));
				lblSp1.setText(sp);
			}
		}
	}

	@FXML
	private void changeSp2() {
		if (isBalaoRunning2 || isBalaoReady2) {
			String sp = FxDialogs.showNumericInput("Set-point", "Balão 2", "Digite o set-point:", "");
			if (sp != null) {
				modService.writeSingleRegister(1, 0, Integer.parseInt(sp));
				lblSp2.setText(sp);
			}
		}
	}

	@FXML
	private void changeSp3() {
		if (isBalaoRunning3 || isBalaoReady3) {
			String sp = FxDialogs.showNumericInput("Set-point", "Balão 3", "Digite o set-point:", "");
			if (sp != null) {
				modService.writeSingleRegister(1, 0, Integer.parseInt(sp));
				lblSp3.setText(sp);
			}
		}
	}

	@FXML
	private void initProc1() {
		provaDAO.updateDataInicial(prova1);
		lblInicio1.setText(horaFormatter.format(LocalDateTime.now()));
		btPlay1.setDisable(true);
		btStop1.setDisable(false);
		btExcluir1.setDisable(true);
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
		provaDAO.updateDataInicial(prova2);
		lblInicio2.setText(horaFormatter.format(LocalDateTime.now()));
		btPlay2.setDisable(true);
		btStop2.setDisable(false);
		btExcluir2.setDisable(true);
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
		provaDAO.updateDataInicial(prova3);
		lblInicio3.setText(horaFormatter.format(LocalDateTime.now()));
		btPlay3.setDisable(true);
		btStop3.setDisable(false);
		btExcluir3.setDisable(true);
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
		alert.setHeaderText("Deseja realmente encerrar a prova em andamento?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		UsedProjetosMap.discardProjeto(Baloes.BALAO1, projeto1);
		imgGlass1.setImage(imgGlassFile);
		chapaTransition1.stop();
		tmlHeater1.stop();
		imgMola1.setEffect(null);
		lnChapa1.setStroke(Color.RED);
		chrono1.stop();
		btAddProjeto1.setDisable(false);
		btPlay1.setDisable(true);
		btStop1.setDisable(true);
		btExcluir1.setDisable(false);
		isBalaoRunning1 = false;
		isBalaoFinished1 = true;
		provaDAO.updateDataFinal(prova1);
		makeToast("Balão 1: Prova encerrada.");
	}

	@FXML
	private void stopProc2() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente encerrar a prova em andamento?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		UsedProjetosMap.discardProjeto(Baloes.BALAO2, projeto2);
		imgGlass2.setImage(imgGlassFile);
		tmlHeater2.stop();
		chapaTransition2.stop();
		imgMola2.setEffect(null);
		lnChapa2.setStroke(Color.RED);
		chrono2.stop();
		btAddProjeto2.setDisable(false);
		btPlay2.setDisable(true);
		btStop2.setDisable(true);
		btExcluir2.setDisable(false);
		isBalaoRunning2 = false;
		isBalaoFinished2 = true;
		provaDAO.updateDataFinal(prova2);
		makeToast("Balão 2: Prova encerrada.");
	}

	@FXML
	private void stopProc3() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente encerrar a prova em andamento?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		UsedProjetosMap.discardProjeto(Baloes.BALAO3, projeto3);
		imgGlass3.setImage(imgGlassFile);
		tmlHeater3.stop();
		chapaTransition3.stop();
		imgMola3.setEffect(null);
		lnChapa3.setStroke(Color.RED);
		chrono3.stop();
		btAddProjeto3.setDisable(false);
		btPlay3.setDisable(true);
		btStop3.setDisable(true);
		btExcluir3.setDisable(false);
		isBalaoRunning3 = false;
		isBalaoFinished3 = true;
		provaDAO.updateDataFinal(prova3);
		makeToast("Balão 3: Prova encerrada.");
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

			Double t = modService.readMultipleRegisters(1, 0, 1);
			// tempBalao1 = modService.readMultipleRegisters(1, 0, 1);
			tempBalao1 = t.intValue();
			lblTemp1.setText(tempBalao1.toString());
			lblTemp2.setText(tempBalao1.toString());
			lblTemp3.setText(tempBalao1.toString());

			Double sp = modService.readMultipleRegisters(1, 0, 1);
			// spBalao1 = modService.readMultipleRegisters(1, 0, 1);
			spBalao1 = sp.intValue();
			lblSp1.setText(spBalao1.toString());
			lblSp2.setText(spBalao1.toString());
			lblSp3.setText(spBalao1.toString());

			scanModbusSlaves.play();
			isConnected = true;
			btConnect.setStyle("-fx-graphic: url('/com/servicos/estatica/resicolor/lab/style/connect.png');");
			btConnect.setText("Desconectar");
			tabForm.setDisable(false);
		}
	}

	@FXML
	private void addProjeto1() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/Projetos.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Gerenciamento de projetos");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(rect1.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();

		if (ProvaProperty.provaProjetoProperty().get() != null) {
			projeto1 = ProvaProperty.provaProjetoProperty().get();
			txtProjeto1.setText(projeto1.getNome());
			UsedProjetosMap.useProjeto(Baloes.BALAO1, projeto1);
			enableForm(txtProduto1, txtNumero1, txtExecutor1, txtObjetivo1, btSaveBalao1);
		} else {
			disableForm(txtProduto1, txtNumero1, txtExecutor1, txtObjetivo1, btSaveBalao1);
		}
		btAmostra1.setDisable(true);
		btExcluir1.setDisable(true);
		ProvaProperty.provaClear1Property().set(!prova1Clear);
		prova1Clear = !prova1Clear;

		txtProduto1.setText(null);
		txtNumero1.setText(null);
		txtExecutor1.setText(null);
		txtObjetivo1.setText(null);
		txtProduto1.requestFocus();

		// ensaio1 = null;
		// isBalaoFinished1 = false;
		// txtProdutoBalao1.setDisable(false);
		// btSaveBalao1.setDisable(false);
	}

	@FXML
	private void addProjeto2() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/Projetos.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Gerenciamento de projetos");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(rect1.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();

		if (ProvaProperty.provaProjetoProperty().get() != null) {
			projeto2 = ProvaProperty.provaProjetoProperty().get();
			txtProjeto2.setText(projeto2.getNome());
			UsedProjetosMap.useProjeto(Baloes.BALAO2, projeto2);
			enableForm(txtProduto2, txtNumero2, txtExecutor2, txtObjetivo2, btSaveBalao2);
		} else {
			disableForm(txtProduto2, txtNumero2, txtExecutor2, txtObjetivo2, btSaveBalao2);
		}
		btAmostra2.setDisable(true);
		btExcluir2.setDisable(true);
		ProvaProperty.provaClear2Property().set(!prova2Clear);
		prova2Clear = !prova2Clear;

		txtProduto2.setText(null);
		txtNumero2.setText(null);
		txtExecutor2.setText(null);
		txtObjetivo2.setText(null);
		txtProduto2.requestFocus();

		// prova2 = null;
		// isBalaoFinished2 = false;
		// txtProdutoBalao2.setDisable(false);
		// btSaveBalao2.setDisable(false);
		// txtProdutoBalao2.setText(null);
		// txtProdutoBalao2.requestFocus();
	}

	@FXML
	private void addProjeto3() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/Projetos.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Gerenciamento de projetos");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(rect1.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();

		if (ProvaProperty.provaProjetoProperty().get() != null) {
			projeto3 = ProvaProperty.provaProjetoProperty().get();
			txtProjeto3.setText(projeto3.getNome());
			UsedProjetosMap.useProjeto(Baloes.BALAO3, projeto3);
			enableForm(txtProduto3, txtNumero3, txtExecutor3, txtObjetivo3, btSaveBalao3);
		} else {
			disableForm(txtProduto3, txtNumero3, txtExecutor3, txtObjetivo3, btSaveBalao3);
		}
		btAmostra3.setDisable(true);
		btExcluir3.setDisable(true);
		ProvaProperty.provaClear1Property().set(!prova3Clear);
		prova3Clear = !prova3Clear;

		txtProduto3.setText(null);
		txtNumero3.setText(null);
		txtExecutor3.setText(null);
		txtObjetivo3.setText(null);
		txtProduto3.requestFocus();

		// prova3 = null;
		// isBalaoFinished3 = false;
		// txtProdutoBalao3.setDisable(false);
		// btSaveBalao3.setDisable(false);
		// txtProdutoBalao3.setText(null);
		// txtProdutoBalao3.requestFocus();
	}

	@FXML
	private void addOrManageAmostra1() throws IOException {
		AmostraProperty.tempProperty().set(tempBalao1);
		AmostraProperty.setPointProperty().set(spBalao1);
		AmostraProperty.provaProperty().set(prova1);
		openAmostra();
	}

	@FXML
	private void addOrManageAmostra2() throws IOException {
		AmostraProperty.tempProperty().set(tempBalao2);
		AmostraProperty.setPointProperty().set(spBalao2);
		AmostraProperty.provaProperty().set(prova2);
		openAmostra();
	}

	@FXML
	private void addOrManageAmostra3() throws IOException {
		AmostraProperty.tempProperty().set(tempBalao3);
		AmostraProperty.setPointProperty().set(spBalao3);
		AmostraProperty.provaProperty().set(prova3);
		openAmostra();
	}

	private void calculaMinMax1() {
		if (tempMin1 > tempBalao1) {
			tempMin1 = tempBalao1.doubleValue();
			Integer i = tempMin1.intValue();
			lblTempMin1.setText(i.toString());
			prova1.setTempMin(tempMin1.intValue());
			provaDAO.updateTemperaturaMin(prova1);
		}
		if (tempMax1 < tempBalao1) {
			tempMax1 = roundToHalf(tempBalao1);
			Integer i = tempMax1.intValue();
			lblTempMax1.setText(i.toString());
			prova1.setTempMax(tempMax1.intValue());
			provaDAO.updateTemperaturaMax(prova1);
		}
	}

	private void calculaMinMax2() {
		if (tempMin2 > tempBalao2) {
			tempMin2 = tempBalao2.doubleValue();
			Integer i = tempMin2.intValue();
			lblTempMin2.setText(i.toString());
			prova2.setTempMin(tempMin2.intValue());
			provaDAO.updateTemperaturaMin(prova2);
		}
		if (tempMax2 < tempBalao2) {
			tempMax2 = roundToHalf(tempBalao2);
			Integer i = tempMax2.intValue();
			lblTempMax2.setText(i.toString());
			prova2.setTempMax(tempMax2.intValue());
			provaDAO.updateTemperaturaMax(prova2);
		}
	}

	private void calculaMinMax3() {
		if (tempMin3 > tempBalao3) {
			tempMin3 = tempBalao3.doubleValue();
			Integer i = tempMin3.intValue();
			lblTempMin3.setText(i.toString());
			prova3.setTempMin(tempMin3.intValue());
			provaDAO.updateTemperaturaMin(prova3);
		}
		if (tempMax3 < tempBalao3) {
			tempMax3 = roundToHalf(tempBalao3);
			Integer i = tempMax3.intValue();
			lblTempMax3.setText(i.toString());
			prova3.setTempMax(tempMax3.intValue());
			provaDAO.updateTemperaturaMax(prova3);
		}
	}

	public static double roundToHalf(double d) {
		return (Math.ceil(d * 2) / 2);
	}

	private void openAmostra() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/Amostra.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Gerenciamento de amostras");
		stage.initModality(Modality.APPLICATION_MODAL);
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

				Double t1 = modService.readMultipleRegisters(1, 0, 1);
				tempBalao1 = t1.intValue();
				Double t2 = modService.readMultipleRegisters(1, 0, 1);
				tempBalao2 = t2.intValue();
				Double t3 = modService.readMultipleRegisters(1, 0, 1);
				tempBalao3 = t3.intValue();

				Double sp1 = modService.readMultipleRegisters(1, 0, 1);
				spBalao1 = sp1.intValue();
				Double sp2 = modService.readMultipleRegisters(1, 0, 1);
				spBalao2 = sp2.intValue();
				Double sp3 = modService.readMultipleRegisters(1, 0, 1);
				spBalao3 = sp3.intValue();

				lblTemp1.setText(tempBalao1.toString());
				lblTemp2.setText(tempBalao2.toString());
				lblTemp3.setText(tempBalao3.toString());
				// setPointReator = modService.readMultipleRegisters(slaveID, 1,
				// 1);

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

	private void disableForm(Node... nodes) {
		for (Node node : nodes) {
			node.setDisable(true);
		}
	}

	private void enableForm(Node... nodes) {
		for (Node node : nodes) {
			node.setDisable(false);
		}
	}

	private void makeToast(String message) {
		String toastMsg = message;
		int toastMsgTime = 5000;
		int fadeInTime = 600;
		int fadeOutTime = 600;
		Stage stage = (Stage) tabForm.getScene().getWindow();
		Toast.makeToast(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
	}

	public Integer getTemp1() {
		return tempBalao1;
	}

	public Integer getTemp2() {
		return tempBalao2;
	}

	public Integer getTemp3() {
		return tempBalao3;
	}

	private void makeAlert(AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(message);
		alert.showAndWait();
	}

}
