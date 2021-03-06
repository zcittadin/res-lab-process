package com.servicos.estatica.resicolor.lab.controller;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.servicos.estatica.resicolor.lab.dao.LeituraDAO;
import com.servicos.estatica.resicolor.lab.dao.ProvaDAO;
import com.servicos.estatica.resicolor.lab.modbus.ModbusRTUService;
import com.servicos.estatica.resicolor.lab.util.Toast;

import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class BaseController {

	@FXML
	protected AnchorPane pane1;
	@FXML
	protected AnchorPane pane2;
	@FXML
	protected TabPane tabForm;
	@FXML
	protected AnchorPane pane3;
	@FXML
	protected ComboBox<String> comboPorts;
	@FXML
	protected Button btConnect;

	protected static String PROVA_1_KEY = "prova1";
	protected static String PROVA_2_KEY = "prova2";
	protected static String PROVA_3_KEY = "prova3";

	protected static Boolean CONNECTED_1 = false;
	protected static Boolean CONNECTED_2 = false;
	protected static Boolean CONNECTED_3 = false;

	protected static Image gifGlassFile = new Image("/com/servicos/estatica/resicolor/lab/style/glass.gif");
	protected static Image imgGlassFile = new Image("/com/servicos/estatica/resicolor/lab/style/glass.png");
	protected static Timeline scanModbusSlaves = new Timeline();
	protected static Boolean isConnected = false;
	protected static ModbusRTUService modService = new ModbusRTUService();
	protected static ObservableList<String> availablePorts;
	protected static ProvaDAO provaDAO = new ProvaDAO();
	protected static LeituraDAO leituraDAO = new LeituraDAO();
	protected static DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	protected int scanInterval = 0;

	protected void makeToast(String message) {
		String toastMsg = message;
		int toastMsgTime = 5000;
		int fadeInTime = 600;
		int fadeOutTime = 600;
		Stage stage = (Stage) tabForm.getScene().getWindow();
		Toast.makeToast(stage, toastMsg, toastMsgTime, fadeInTime, fadeOutTime);
	}

	protected void disableForm(Node... nodes) {
		for (Node node : nodes) {
			node.setDisable(true);
		}
	}

	protected void enableForm(Node... nodes) {
		for (Node node : nodes) {
			node.setDisable(false);
		}
	}

	protected void openAmostra() throws IOException {
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

}
