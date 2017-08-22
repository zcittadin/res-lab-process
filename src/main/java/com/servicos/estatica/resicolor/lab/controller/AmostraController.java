package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.property.AmostraProperty;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AmostraController implements Initializable {

	@FXML
	private Rectangle  rect1;
	@FXML
	private TextField txtHorario;
	@FXML
	private TextField txtTemp;
	@FXML
	private TextField txtSetPoint;
	
	private static DateTimeFormatter dataHoraFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		rect1.setFill(Color.TRANSPARENT);
		txtHorario.setText(dataHoraFormatter.format(LocalDateTime.now()));
		txtTemp.setText(AmostraProperty.getTemp().toString());
		txtSetPoint.setText(AmostraProperty.getSetPoint().toString());

	}

}
