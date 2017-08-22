package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.dao.AmostraDAO;
import com.servicos.estatica.resicolor.lab.model.Amostra;
import com.servicos.estatica.resicolor.lab.property.AmostraProperty;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class AmostraController implements Initializable {

	@FXML
	private Rectangle rectangle;
	@FXML
	private TextField txtHorario;
	@FXML
	private TextField txtTemp;
	@FXML
	private TextField txtSetPoint;
	@FXML
	private TextArea txtDescricao;
	@FXML
	private TextField txtIAsobreNV;
	@FXML
	private TextField txtViscGardner;
	@FXML
	private TextField txtCorGardner;
	@FXML
	private TextField txtNV;
	@FXML
	private TextField txtGelTime;
	@FXML
	private TextField txtAgua;
	@FXML
	private TextField txtAmostra;
	@FXML
	private TextField txtPH;

	private static Amostra amostra;
	private static AmostraDAO amostraDAO = new AmostraDAO();

	private static DateTimeFormatter dataHoraFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		rectangle.setFill(Color.TRANSPARENT);
		prepareFields();

	}

	@FXML
	private void novaAmostra() {
		prepareFields();
	}

	@FXML
	private void saveAmostra() {

		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				// prog1.setVisible(true);
				// btSaveBalao1.setDisable(true);
				// btAddProjeto1.setDisable(true);
				// txtProduto1.setDisable(true);
				amostra = new Amostra(null, null, null, Integer.parseInt(txtTemp.getText()),
						Integer.parseInt(txtSetPoint.getText()), Double.parseDouble(txtIAsobreNV.getText()),
						txtViscGardner.getText(), txtCorGardner.getText(), Double.parseDouble(txtNV.getText()),
						Integer.parseInt(txtGelTime.getText()), Double.parseDouble(txtAgua.getText()),
						Double.parseDouble(txtAmostra.getText()), Double.parseDouble(txtPH.getText()),
						txtDescricao.getText());
				amostraDAO.saveAmostra(amostra);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				// prog1.setVisible(false);
				// btPlay1.setDisable(false);
				// btAmostra1.setDisable(false);
				// isBalaoReady1 = true;
				// makeToast("Prova registrada com sucesso.");
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				// prog1.setVisible(true);
				// isBalaoReady1 = false;
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	private void prepareFields() {
		txtHorario.setText(dataHoraFormatter.format(LocalDateTime.now()));
		txtTemp.setText(AmostraProperty.getTemp().toString());
		txtSetPoint.setText(AmostraProperty.getSetPoint().toString());
		txtDescricao.setText(null);
		txtIAsobreNV.setText(null);
		txtViscGardner.setText(null);
		txtCorGardner.setText(null);
		txtNV.setText(null);
		txtGelTime.setText(null);
		txtAgua.setText(null);
		txtAmostra.setText(null);
		txtPH.setText(null);

	}

}
