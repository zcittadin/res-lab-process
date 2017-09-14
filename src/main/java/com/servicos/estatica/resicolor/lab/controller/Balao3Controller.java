package com.servicos.estatica.resicolor.lab.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.servicos.estatica.resicolor.lab.model.Leitura;
import com.servicos.estatica.resicolor.lab.model.Projeto;
import com.servicos.estatica.resicolor.lab.model.Prova;
import com.servicos.estatica.resicolor.lab.property.AmostraProperty;
import com.servicos.estatica.resicolor.lab.property.ProvaProperty;
import com.servicos.estatica.resicolor.lab.property.UsedProjetosMap;
import com.servicos.estatica.resicolor.lab.util.Baloes;
import com.servicos.estatica.resicolor.lab.util.Chronometer;
import com.servicos.estatica.resicolor.lab.util.FxDialogs;

import javafx.animation.StrokeTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.Glow;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Balao3Controller extends BaseController {

	@FXML
	protected TextField txtProjeto3;
	@FXML
	protected TextField txtProduto3;
	@FXML
	protected TextField txtNumero3;
	@FXML
	protected TextField txtExecutor3;
	@FXML
	protected TextArea txtObjetivo3;
	@FXML
	protected CheckBox chkBalao3;
	@FXML
	protected Label lblTemp3;
	@FXML
	protected Label lblSp3;
	@FXML
	protected Label lblCrono3;
	@FXML
	protected Label lblTempMin3;
	@FXML
	protected Label lblTempMax3;
	@FXML
	protected Label lblInicio3;
	@FXML
	protected Button btAddProjeto3;
	@FXML
	protected Button btPlay3;
	@FXML
	protected Button btStop3;
	@FXML
	protected Button btAmostra3;
	@FXML
	protected Button btSaveBalao3;
	@FXML
	protected Button btExcluir3;
	@FXML
	protected ImageView imgGlass3;
	@FXML
	protected ImageView imgNovus3;
	@FXML
	protected ImageView imgMola3;
	@FXML
	protected Rectangle rect3;
	@FXML
	protected Line lnChapa3;
	@FXML
	protected ProgressIndicator prog3;

	protected static Glow glow3 = new Glow(0);
	protected static SepiaTone sepia3 = new SepiaTone(0);
	protected static StrokeTransition chapaTransition3;

	protected static Timeline tmlHeater3;
	protected static Chronometer chrono3 = new Chronometer();
	protected static Boolean isBalaoReady3 = false;
	protected static Boolean isBalaoRunning3 = false;
	protected static Boolean isBalaoFinished3 = false;
	protected static Boolean tempProva3Changed = false;
	protected static Boolean prova3Clear = false;
	protected static Double tempBalao3 = new Double(0);
	protected static Double spBalao3 = new Double(0);
	protected static Double tempMin3 = new Double(1900);
	protected static Double tempMax3 = new Double(0);
	protected static Projeto projeto3;
	protected static Prova prova3;
	protected static List<Leitura> leituras3;

	@FXML
	public void saveBalao3() {
		if (txtProduto3.getText() == null || txtProduto3.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o produto.");
			txtProduto3.requestFocus();
			return;
		}
		if (txtNumero3.getText() == null || txtNumero3.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o número do projeto.");
			txtNumero3.requestFocus();
			return;
		}
		if (txtExecutor3.getText() == null || txtExecutor3.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o executor da prova.");
			txtExecutor3.requestFocus();
			return;
		}
		if (txtObjetivo3.getText() == null || txtObjetivo3.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o objetivo da prova.");
			txtObjetivo3.requestFocus();
			return;
		}

		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				prog3.setVisible(true);
				btSaveBalao3.setDisable(true);
				btExcluir3.setDisable(true);
				btAddProjeto3.setDisable(true);
				txtProduto3.setDisable(true);
				txtNumero3.setDisable(true);
				txtExecutor3.setDisable(true);
				txtObjetivo3.setDisable(true);
				leituras3 = new ArrayList<>();
				prova3 = new Prova(null, projeto3, leituras3, null, txtProduto3.getText(), txtObjetivo3.getText(),
						txtExecutor3.getText(), txtNumero3.getText(), "Balão 3", 0, 0, null, null);
				provaDAO.saveProva(prova3);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog3.setVisible(false);
				btPlay3.setDisable(false);
				btAmostra3.setDisable(false);
				btExcluir3.setDisable(false);
				isBalaoReady3 = true;
				makeToast("Prova registrada com sucesso.");
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog3.setVisible(false);
				btSaveBalao3.setDisable(false);
				btAddProjeto3.setDisable(false);
				isBalaoReady3 = false;
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	@FXML
	private void removeProva3() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar exclusão");
		alert.setHeaderText("Deseja realmente excluir esta prova?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		if (prova3 != null) {
			provaDAO.removeProva(prova3);
			txtProjeto3.clear();
			txtProduto3.clear();
			txtNumero3.clear();
			txtExecutor3.clear();
			txtObjetivo3.clear();
			lblInicio3.setText("00:00:00");
			lblCrono3.setText("00:00:00");
			lblTempMax3.setText("000");
			lblTempMin3.setText("000");
			btAddProjeto3.setDisable(false);
			btAmostra3.setDisable(true);
			btExcluir3.setDisable(true);
			btPlay3.setDisable(true);
			ProvaProperty.provaClear3Property().set(!prova3Clear);
			prova3Clear = !prova3Clear;
			UsedProjetosMap.discardProjetoByBalao(Baloes.BALAO3);
			makeToast("Prova removida com sucesso.");
			prova3 = null;
		}
	}

	@FXML
	private void changeSp3() {
		if (isConnected) {
			String sp = FxDialogs.showNumericInput("Set-point", "Balão 3", "Digite o set-point:", "");
			if (sp != null) {
				modService.writeSingleRegister(1, 0, Integer.parseInt(sp.replace(",", "")));
				lblSp3.setText(sp.contains(",") ? sp.replace(",", ".") : sp.concat(".0"));
			}
		}
	}

	@FXML
	private void toggleBalao3() {
		if (chkBalao3.isSelected()) {
			tempBalao3 = modService.readMultipleRegisters(3, 1, 1);
			spBalao3 = modService.readMultipleRegisters(3, 0, 1);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblTemp3.setText(tempBalao3.toString());
					lblSp3.setText(spBalao3.toString());
				}
			});
		} else {
			lblTemp3.setText("000.0");
			lblSp3.setText("000.0");
		}
	}

	@FXML
	private void initProc3() {
		provaDAO.updateDataInicial(prova3);
		lblInicio3.setText(horaFormatter.format(LocalDateTime.now()));
		btPlay3.setDisable(true);
		btStop3.setDisable(false);
		btExcluir3.setDisable(true);
		imgGlass3.setImage(gifGlassFile);
		imgMola3.setEffect(sepia3);
		chapaTransition3.play();
		tmlHeater3.play();
		chrono3.start(lblCrono3);
		isBalaoRunning3 = true;
		isBalaoReady3 = false;
	}

	@FXML
	private void stopProc3() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente encerrar a prova em andamento?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		imgGlass3.setImage(imgGlassFile);
		tmlHeater3.stop();
		chapaTransition3.stop();
		imgMola3.setEffect(null);
		lnChapa3.setStroke(Color.RED);
		chrono3.stop();
		btAddProjeto3.setDisable(false);
		btPlay3.setDisable(true);
		btStop3.setDisable(true);
		btExcluir3.setDisable(false);
		isBalaoRunning3 = false;
		isBalaoFinished3 = true;
		provaDAO.updateDataFinal(prova3);
		makeToast("Balão 3: Prova encerrada.");
	}

	@FXML
	private void addProjeto3() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/Projetos.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Gerenciamento de projetos");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(rect3.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();

		if (ProvaProperty.provaProjetoProperty().get() != null) {
			projeto3 = ProvaProperty.provaProjetoProperty().get();
			txtProjeto3.setText(projeto3.getNome());
			UsedProjetosMap.useProjeto(Baloes.BALAO3, projeto3);
			enableForm(txtProduto3, txtNumero3, txtExecutor3, txtObjetivo3, btSaveBalao3);
		} else {
			txtProjeto3.clear();
			disableForm(txtProduto3, txtNumero3, txtExecutor3, txtObjetivo3, btSaveBalao3);
		}
		tempMin3 = new Double(1900);
		tempMax3 = new Double(0);
		btAmostra3.setDisable(true);
		btExcluir3.setDisable(true);
		ProvaProperty.provaClear3Property().set(!prova3Clear);
		prova3Clear = !prova3Clear;

		txtProduto3.setText(null);
		txtNumero3.setText(null);
		txtExecutor3.setText(null);
		txtObjetivo3.setText(null);
		lblInicio3.setText("00:00:00");
		lblCrono3.setText("00:00:00");
		lblTempMax3.setText("000");
		lblTempMin3.setText("000");
		txtProduto3.requestFocus();
		// prova3 = null;
		// isBalaoFinished3 = false;
	}

	@FXML
	protected void addOrManageAmostra3() throws IOException {
		AmostraProperty.tempProperty().set(tempBalao3);
		AmostraProperty.setPointProperty().set(spBalao3);
		AmostraProperty.provaProperty().set(prova3);
		openAmostra();
	}

	protected void calculaMinMax3() {
		if (tempMin3 > tempBalao3) {
			tempMin3 = tempBalao3;
			lblTempMin3.setText(tempMin3.toString());
			prova3.setTempMin(tempMin3);
			provaDAO.updateTemperaturaMin(prova3);
		}
		if (tempMax3 < tempBalao3) {
			tempMax3 = tempBalao3;
			lblTempMax3.setText(tempMax3.toString());
			prova3.setTempMax(tempMax3);
			provaDAO.updateTemperaturaMax(prova3);
		}
	}

	public Double getTemp3() {
		return tempBalao3;
	}

}
