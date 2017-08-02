package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.app.ControlledScreen;
import com.servicos.estatica.resicolor.lab.util.EstaticaInfoUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class Screen3Controller implements Initializable, ControlledScreen {

	@FXML
	private LineChart<String, Number> chartTemp;
	@FXML
	private CategoryAxis xAxis;
	@FXML
	private NumberAxis yAxis;

	private static XYChart.Series<String, Number> tempSeries;
	private static DateTimeFormatter dataHoraFormatter = DateTimeFormatter.ofPattern("HH:mm:ss - dd/MM/yy");

	final ObservableList<XYChart.Series<String, Number>> plotValuesList = FXCollections.observableArrayList();

	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		configLineChart();
	}

	private void configLineChart() {
		yAxis.setAutoRanging(false);
		yAxis.setLowerBound(0);
		yAxis.setUpperBound(150);
		yAxis.setTickUnit(15);

		tempSeries = new XYChart.Series<String, Number>();
		plotValuesList.add(tempSeries);
		chartTemp.setData(plotValuesList);

	}

	public void plotTemp(Double temp) {
		final XYChart.Data<String, Number> data = new XYChart.Data<>(dataHoraFormatter.format(LocalDateTime.now()),
				temp);
		tempSeries.getData().add(data);
	}

}
