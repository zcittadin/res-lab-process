package com.servicos.estatica.resicolor.lab.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.property.CurrentScreenProperty;
import com.servicos.estatica.resicolor.lab.property.EnsaioProperty;
import com.servicos.estatica.resicolor.lab.util.EstaticaInfoUtil;

import eu.hansolo.medusa.Clock;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import zan.inc.custom.components.ImageViewResizer;

public class MainController extends EstaticaInfoUtil implements Initializable {

	public static String screenInicialID = "inicial";
	public static String screenInicialFile = "/com/servicos/estatica/resicolor/lab/app/InicialScreen.fxml";
	public static String screen1ID = "screen1";
	public static String screen1File = "/com/servicos/estatica/resicolor/lab/app/Screen1.fxml";
	public static String screen2ID = "screen2";
	public static String screen2File = "/com/servicos/estatica/resicolor/lab/app/Screen2.fxml";
	public static String screen3ID = "screen3";
	public static String screen3File = "/com/servicos/estatica/resicolor/lab/app/Screen3.fxml";
	public static String screenConsultaID = "Consulta";
	public static String screenConsultaFile = "/com/servicos/estatica/resicolor/lab/app/Consulta.fxml";

	@FXML
	private AnchorPane mainPane;
	@FXML
	private Pane centralPane;
	@FXML
	private Rectangle rectClock;
	@FXML
	private ImageView imgCliente;
	@FXML
	private ImageView imgExit;
	@FXML
	private Clock clock;

	private static ImageViewResizer imgClienteResizer;
	private static ImageViewResizer imgExitResizer;

	private InicialController inicialController = new InicialController();
	private Screen1Controller screen1Controller = new Screen1Controller();
	private Screen2Controller screen2Controller = new Screen2Controller();
	private Screen3Controller screen3Controller = new Screen3Controller();

	ScreensController mainContainer = new ScreensController();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		initEstaticaInfo();

		rectClock.setFill(Color.TRANSPARENT);

		imgCliente.setImage(new Image("/com/servicos/estatica/resicolor/lab/style/resicolor.png"));
		imgClienteResizer = new ImageViewResizer(imgCliente, 126, 70);
		imgClienteResizer.setLayoutX(16);
		imgClienteResizer.setLayoutY(6);
		imgExitResizer = new ImageViewResizer(imgExit, 70, 71);
		imgExitResizer.setLayoutX(50);
		imgExitResizer.setLayoutY(633);
		mainPane.getChildren().addAll(imgClienteResizer, imgExitResizer);

		mainContainer.loadScreenAndController(screenInicialID, screenInicialFile, inicialController);
		mainContainer.loadScreenAndController(screen1ID, screen1File, screen1Controller);
		mainContainer.loadScreenAndController(screen2ID, screen2File, screen2Controller);
		mainContainer.loadScreenAndController(screen3ID, screen3File, screen3Controller);
		mainContainer.loadScreen(screenConsultaID, screenConsultaFile);

		CurrentScreenProperty.setScreen(screenInicialID);
		mainContainer.setScreen(screenInicialID);
		centralPane.getChildren().addAll(mainContainer);

		initListeners();

	}

	@FXML
	private void openScreenInicial() {
		mainContainer.setScreen(screenInicialID);
	}

	@FXML
	private void openScreen1() {
		mainContainer.setScreen(screen1ID);
	}

	@FXML
	private void openScreen2() {
		mainContainer.setScreen(screen2ID);
	}

	@FXML
	private void openScreen3() {
		mainContainer.setScreen(screen3ID);
	}

	@FXML
	private void openConsulta() {
		mainContainer.setScreen(screenConsultaID);
	}

	@FXML
	private void handleImgClienteAction() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/ClienteInfo.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Informações sobre o cliente");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(imgCliente.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
	}

	@FXML
	private void exit() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente sair do sistema?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			Platform.exit();
		}
	}

	private void initListeners() {
		EnsaioProperty.ensaioTemp1Property().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				screen1Controller.plotTemp(inicialController.getTemp());
			}
		});

		EnsaioProperty.ensaioTemp2Property().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				screen2Controller.plotTemp(inicialController.getTemp());
			}
		});

		EnsaioProperty.ensaioTemp3Property().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				screen3Controller.plotTemp(inicialController.getTemp());
			}
		});

		EnsaioProperty.ensaioClear1Property().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				screen1Controller.clearLineChart();
			}
		});

		EnsaioProperty.ensaioClear2Property().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				screen2Controller.clearLineChart();
			}
		});

		EnsaioProperty.ensaioClear3Property().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				screen3Controller.clearLineChart();
			}
		});
	}

}
