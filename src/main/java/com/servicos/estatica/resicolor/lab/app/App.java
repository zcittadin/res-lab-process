package com.servicos.estatica.resicolor.lab.app;

import java.util.Optional;

import com.servicos.estatica.resicolor.lab.property.EnsaioStatusManager;
import com.servicos.estatica.resicolor.lab.util.HibernateUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Wtech Qu�mica");
		stage.setMaximized(false);
		stage.getIcons().add(new Image("/com/servicos/estatica/resicolor/lab/style/256_colors.png"));
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				event.consume();
				if (EnsaioStatusManager.verifyProvaRunning())
					return;
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmar encerramento");
				alert.setHeaderText("Deseja realmente sair do sistema?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get() == ButtonType.OK) {
					Platform.exit();
				}
			}
		});
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		HibernateUtil.closeSessionFactory();
	}

	public static void main(String[] args) {
		launch();
	}

}
