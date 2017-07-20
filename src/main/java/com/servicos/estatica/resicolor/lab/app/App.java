package com.servicos.estatica.resicolor.lab.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	@Override
	public void start(Stage stage) throws Exception {

		Parent root = FXMLLoader.load(getClass().getResource("App.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Resicolor - Divisão de resinas");
		stage.setMaximized(true);
		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}

}
