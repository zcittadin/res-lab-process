package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.app.ControlledScreen;
import com.servicos.estatica.resicolor.lab.util.EstaticaInfoUtil;

import javafx.fxml.Initializable;

public class Screen2Controller extends EstaticaInfoUtil implements Initializable, ControlledScreen {

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initEstaticaInfo();
	}

}
