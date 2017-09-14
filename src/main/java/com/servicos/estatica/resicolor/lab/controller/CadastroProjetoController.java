package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.dao.ProjetoDAO;
import com.servicos.estatica.resicolor.lab.model.Projeto;

import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CadastroProjetoController implements Initializable {

	@FXML
	private TextField txtNomeProjeto;
	@FXML
	private TextField txtTeorSolidos;
	@FXML
	private TextField txtViscosidade;
	@FXML
	private TextField txtCorGardner;
	@FXML
	private TextField txtIndiceAcidez;
	@FXML
	private TextField txtTeorOh;
	@FXML
	private TextField txtPh;
	@FXML
	private TextArea txtDadosAdd;
	@FXML
	private ProgressIndicator progProjetos;
	@FXML
	private Button btSalvar;

	private static ProjetoDAO projetoDAO = new ProjetoDAO();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	private void saveProjeto() {
		if (txtNomeProjeto.getText() == null || "".equals(txtNomeProjeto.getText().trim())) {
			Alert alert = new Alert(AlertType.WARNING);
			alert.setTitle("Atenção");
			alert.setHeaderText("Informe um nome para o novo projeto.");
			alert.showAndWait();
			txtNomeProjeto.requestFocus();
			return;
		}
		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				fetch(true);
				Projeto projeto = new Projeto(null, null, txtNomeProjeto.getText(), Calendar.getInstance().getTime(),
						txtTeorSolidos.getText(), txtViscosidade.getText(), txtCorGardner.getText(),
						txtIndiceAcidez.getText(), txtTeorOh.getText(), txtPh.getText(), txtDadosAdd.getText());
				projetoDAO.saveProjeto(projeto);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				fetch(false);
				Stage stage = (Stage) txtNomeProjeto.getScene().getWindow();
				stage.close();
			}

		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				fetch(false);
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Erro");
				alert.setHeaderText("Ocorreu uma falha ao salvar o novo projeto.");
				alert.showAndWait();
				Stage stage = (Stage) txtNomeProjeto.getScene().getWindow();
				stage.close();
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	private void fetch(Boolean b) {
		progProjetos.setVisible(b);
		btSalvar.setDisable(b);
		txtCorGardner.setDisable(b);
		txtDadosAdd.setDisable(b);
		txtIndiceAcidez.setDisable(b);
		txtNomeProjeto.setDisable(b);
		txtPh.setDisable(b);
		txtTeorOh.setDisable(b);
		txtTeorSolidos.setDisable(b);
		txtViscosidade.setDisable(b);

	}

}
