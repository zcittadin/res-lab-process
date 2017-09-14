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

public class Balao1Controller extends Balao2Controller {

	@FXML
	protected TextField txtProjeto1;
	@FXML
	protected TextField txtProduto1;
	@FXML
	protected TextField txtNumero1;
	@FXML
	protected TextField txtExecutor1;
	@FXML
	protected TextArea txtObjetivo1;
	@FXML
	protected CheckBox chkBalao1;
	@FXML
	protected Label lblTemp1;
	@FXML
	protected Label lblSp1;
	@FXML
	protected Label lblCrono1;
	@FXML
	protected Label lblTempMin1;
	@FXML
	protected Label lblTempMax1;
	@FXML
	protected Label lblInicio1;
	@FXML
	protected Button btAddProjeto1;
	@FXML
	protected Button btPlay1;
	@FXML
	protected Button btStop1;
	@FXML
	protected Button btAmostra1;
	@FXML
	protected Button btSaveBalao1;
	@FXML
	protected Button btExcluir1;
	@FXML
	protected ImageView imgGlass1;
	@FXML
	protected ImageView imgMola1;
	@FXML
	protected ImageView imgNovus1;
	@FXML
	protected Rectangle rect1;
	@FXML
	protected Line lnChapa1;
	@FXML
	protected ProgressIndicator prog1;

	protected static Glow glow1 = new Glow(0);
	protected static SepiaTone sepia1 = new SepiaTone(0);
	protected static StrokeTransition chapaTransition1;

	protected static Timeline tmlHeater1;
	protected static Chronometer chrono1 = new Chronometer();
	protected static Boolean isBalaoReady1 = false;
	protected static Boolean isBalaoRunning1 = false;
	protected static Boolean isBalaoFinished1 = false;
	protected static Boolean tempProva1Changed = false;
	protected static Boolean prova1Clear = false;
	protected static Double tempBalao1 = new Double(0);
	protected static Double spBalao1 = new Double(0);
	protected static Double tempMin1 = new Double(1900);
	protected static Double tempMax1 = new Double(0);
	protected static Projeto projeto1;
	protected static Prova prova1;
	protected static List<Leitura> leituras1;

	@FXML
	public void saveBalao1() {
		if (txtProduto1.getText() == null || txtProduto1.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o produto.");
			txtProduto1.requestFocus();
			return;
		}
		if (txtNumero1.getText() == null || txtNumero1.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o número do projeto.");
			txtNumero1.requestFocus();
			return;
		}
		if (txtExecutor1.getText() == null || txtExecutor1.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o executor da prova.");
			txtExecutor1.requestFocus();
			return;
		}
		if (txtObjetivo1.getText() == null || txtObjetivo1.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o objetivo da prova.");
			txtObjetivo1.requestFocus();
			return;
		}

		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				prog1.setVisible(true);
				btSaveBalao1.setDisable(true);
				btExcluir1.setDisable(true);
				btAddProjeto1.setDisable(true);
				txtProduto1.setDisable(true);
				txtNumero1.setDisable(true);
				txtExecutor1.setDisable(true);
				txtObjetivo1.setDisable(true);
				leituras1 = new ArrayList<>();
				prova1 = new Prova(null, projeto1, leituras1, null, txtProduto1.getText(), txtObjetivo1.getText(),
						txtExecutor1.getText(), txtNumero1.getText(), "Balão 1", 0, 0, null, null);
				provaDAO.saveProva(prova1);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog1.setVisible(false);
				btPlay1.setDisable(false);
				btAmostra1.setDisable(false);
				btExcluir1.setDisable(false);
				isBalaoReady1 = true;
				makeToast("Prova registrada com sucesso.");
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog1.setVisible(true);
				btSaveBalao1.setDisable(false);
				btAddProjeto1.setDisable(false);
				isBalaoReady1 = false;
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	@FXML
	private void removeProva1() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar exclusão");
		alert.setHeaderText("Deseja realmente excluir esta prova?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		if (prova1 != null) {
			provaDAO.removeProva(prova1);
			txtProjeto1.clear();
			txtProduto1.clear();
			txtNumero1.clear();
			txtExecutor1.clear();
			txtObjetivo1.clear();
			lblInicio1.setText("00:00:00");
			lblCrono1.setText("00:00:00");
			lblTempMax1.setText("000");
			lblTempMin1.setText("000");
			btAddProjeto1.setDisable(false);
			btAmostra1.setDisable(true);
			btExcluir1.setDisable(true);
			btPlay1.setDisable(true);
			ProvaProperty.provaClear1Property().set(!prova1Clear);
			prova1Clear = !prova1Clear;
			UsedProjetosMap.discardProjetoByBalao(Baloes.BALAO1);
			makeToast("Prova removida com sucesso.");
			prova1 = null;
		}
	}

	@FXML
	private void changeSp1() {
		if (isConnected) {
			String sp = FxDialogs.showNumericInput("Set-point", "Balão 1", "Digite o set-point:", "");
			if (sp != null) {
				modService.writeSingleRegister(1, 0, Integer.parseInt(sp.replace(",", "")));
				lblSp1.setText(sp.contains(",") ? sp.replace(",", ".") : sp.concat(".0"));
			}
		}
	}

	@FXML
	private void toggleBalao1() {
		if (chkBalao1.isSelected()) {
			tempBalao1 = modService.readMultipleRegisters(1, 1, 1);
			spBalao1 = modService.readMultipleRegisters(1, 0, 1);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					lblTemp1.setText(tempBalao1.toString());
					lblSp1.setText(spBalao1.toString());
				}
			});
		} else {
			lblTemp1.setText("000.0");
			lblSp1.setText("000.0");
		}
	}

	@FXML
	private void initProc1() {
		provaDAO.updateDataInicial(prova1);
		lblInicio1.setText(horaFormatter.format(LocalDateTime.now()));
		btPlay1.setDisable(true);
		btStop1.setDisable(false);
		btExcluir1.setDisable(true);
		imgGlass1.setImage(gifGlassFile);
		imgMola1.setEffect(sepia1);
		chapaTransition1.play();
		tmlHeater1.play();
		chrono1.start(lblCrono1);
		isBalaoRunning1 = true;
		isBalaoReady1 = false;
	}

	@FXML
	private void stopProc1() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente encerrar a prova em andamento?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		imgGlass1.setImage(imgGlassFile);
		chapaTransition1.stop();
		tmlHeater1.stop();
		imgMola1.setEffect(null);
		lnChapa1.setStroke(Color.RED);
		chrono1.stop();
		btAddProjeto1.setDisable(false);
		btPlay1.setDisable(true);
		btStop1.setDisable(true);
		btExcluir1.setDisable(false);
		isBalaoRunning1 = false;
		isBalaoFinished1 = true;
		provaDAO.updateDataFinal(prova1);
		makeToast("Balão 1: Prova encerrada.");
	}

	@FXML
	private void addProjeto1() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/Projetos.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Gerenciamento de projetos");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(rect1.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();

		if (ProvaProperty.provaProjetoProperty().get() != null) {
			projeto1 = ProvaProperty.provaProjetoProperty().get();
			txtProjeto1.setText(projeto1.getNome());
			UsedProjetosMap.useProjeto(Baloes.BALAO1, projeto1);
			enableForm(txtProduto1, txtNumero1, txtExecutor1, txtObjetivo1, btSaveBalao1);
		} else {
			disableForm(txtProduto1, txtNumero1, txtExecutor1, txtObjetivo1, btSaveBalao1);
		}
		tempMin1 = new Double(1900);
		tempMax1 = new Double(0);
		btAmostra1.setDisable(true);
		btExcluir1.setDisable(true);
		ProvaProperty.provaClear1Property().set(!prova1Clear);
		prova1Clear = !prova1Clear;

		txtProduto1.setText(null);
		txtNumero1.setText(null);
		txtExecutor1.setText(null);
		txtObjetivo1.setText(null);
		lblInicio1.setText("00:00:00");
		lblCrono1.setText("00:00:00");
		lblTempMax1.setText("000");
		lblTempMin1.setText("000");
		txtProduto1.requestFocus();
		// ensaio1 = null;
		// isBalaoFinished1 = false;
	}

	@FXML
	protected void addOrManageAmostra1() throws IOException {
		AmostraProperty.tempProperty().set(tempBalao1);
		AmostraProperty.setPointProperty().set(spBalao1);
		AmostraProperty.provaProperty().set(prova1);
		openAmostra();
	}

	protected void calculaMinMax1() {
		if (tempMin1 > tempBalao1) {
			tempMin1 = tempBalao1;
			lblTempMin1.setText(tempMin1.toString());
			prova1.setTempMin(tempMin1);
			provaDAO.updateTemperaturaMin(prova1);
		}
		if (tempMax1 < tempBalao1) {
			tempMax1 = tempBalao1;
			lblTempMax1.setText(tempMax1.toString());
			prova1.setTempMax(tempMax1);
			provaDAO.updateTemperaturaMax(prova1);
		}
	}

	public Double getTemp1() {
		return tempBalao1;
	}

}
