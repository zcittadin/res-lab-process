package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.app.ControlledScreen;

import javafx.fxml.Initializable;

public class ConsultaController implements Initializable, ControlledScreen {

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

}
