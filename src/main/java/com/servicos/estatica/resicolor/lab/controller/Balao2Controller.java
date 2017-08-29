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

public class Balao2Controller extends Balao3Controller {

	@FXML
	protected TextField txtProjeto2;
	@FXML
	protected TextField txtProduto2;
	@FXML
	protected TextField txtNumero2;
	@FXML
	protected TextField txtExecutor2;
	@FXML
	protected TextArea txtObjetivo2;
	@FXML
	protected Label lblTemp2;
	@FXML
	protected Label lblSp2;
	@FXML
	protected Label lblCrono2;
	@FXML
	protected Label lblTempMin2;
	@FXML
	protected Label lblTempMax2;
	@FXML
	protected Label lblInicio2;
	@FXML
	protected Button btAddProjeto2;
	@FXML
	protected Button btPlay2;
	@FXML
	protected Button btStop2;
	@FXML
	protected Button btAmostra2;
	@FXML
	protected Button btSaveBalao2;
	@FXML
	protected Button btExcluir2;
	@FXML
	protected Button btSp2;
	@FXML
	protected ImageView imgGlass2;
	@FXML
	protected ImageView imgMola2;
	@FXML
	protected ImageView imgNovus2;
	@FXML
	protected Rectangle rect2;
	@FXML
	protected Line lnChapa2;
	@FXML
	protected ProgressIndicator prog2;

	protected static Glow glow2 = new Glow(0);
	protected static SepiaTone sepia2 = new SepiaTone(0);
	protected static StrokeTransition chapaTransition2;

	protected static Timeline tmlHeater2;
	protected static Chronometer chrono2 = new Chronometer();
	protected static Boolean isBalaoReady2 = false;
	protected static Boolean isBalaoRunning2 = false;
	protected static Boolean isBalaoFinished2 = false;
	protected static Boolean tempProva2Changed = false;
	protected static Boolean prova2Clear = false;
	protected static Double tempBalao2 = new Double(0);
	protected static Double spBalao2 = new Double(0);
	protected static Double tempMin2 = new Double(1900);
	protected static Double tempMax2 = new Double(0);
	protected static Projeto projeto2;
	protected static Prova prova2;
	protected static List<Leitura> leituras2;

	@FXML
	public void saveBalao2() {
		if (txtProduto2.getText() == null || txtProduto2.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o produto.");
			txtProduto2.requestFocus();
			return;
		}
		if (txtNumero2.getText() == null || txtNumero2.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o número do projeto.");
			txtNumero2.requestFocus();
			return;
		}
		if (txtExecutor2.getText() == null || txtExecutor2.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o executor da prova.");
			txtExecutor2.requestFocus();
			return;
		}
		if (txtObjetivo2.getText() == null || txtObjetivo2.getText().trim().equals("")) {
			FxDialogs.makeAlert(AlertType.WARNING, "Atenção", "Informe o objetivo da prova.");
			txtObjetivo2.requestFocus();
			return;
		}

		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				prog2.setVisible(true);
				btSaveBalao2.setDisable(true);
				btExcluir2.setDisable(true);
				btAddProjeto2.setDisable(true);
				txtProduto2.setDisable(true);
				txtNumero2.setDisable(true);
				txtExecutor2.setDisable(true);
				txtObjetivo2.setDisable(true);
				leituras2 = new ArrayList<>();
				prova2 = new Prova(null, projeto2, leituras2, null, txtProduto2.getText(), txtObjetivo2.getText(),
						txtExecutor2.getText(), txtNumero2.getText(), "Balão 2", 0, 0, null, null);
				provaDAO.saveProva(prova2);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog2.setVisible(false);
				btPlay2.setDisable(false);
				btAmostra2.setDisable(false);
				btExcluir2.setDisable(false);
				isBalaoReady2 = true;
				makeToast("Prova registrada com sucesso.");
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				prog2.setVisible(false);
				btSaveBalao2.setDisable(false);
				btAddProjeto2.setDisable(false);
				isBalaoReady2 = false;
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	@FXML
	private void removeProva2() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar exclusão");
		alert.setHeaderText("Deseja realmente excluir esta prova?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		if (prova2 != null) {
			provaDAO.removeProva(prova2);
			txtProjeto2.clear();
			txtProduto2.clear();
			txtNumero2.clear();
			txtExecutor2.clear();
			txtObjetivo2.clear();
			lblInicio2.setText("00:00:00");
			lblCrono2.setText("00:00:00");
			lblTempMax2.setText("000");
			lblTempMin2.setText("000");
			btAddProjeto2.setDisable(false);
			btAmostra2.setDisable(true);
			btExcluir2.setDisable(true);
			btPlay2.setDisable(true);
			ProvaProperty.provaClear2Property().set(!prova2Clear);
			prova2Clear = !prova2Clear;
			UsedProjetosMap.discardProjetoByBalao(Baloes.BALAO2);
			makeToast("Prova removida com sucesso.");
			prova2 = null;
		}
	}

	@FXML
	private void changeSp2() {
		if (isConnected) {
			String sp = FxDialogs.showNumericInput("Set-point", "Balão 2", "Digite o set-point:", "");
			if (sp != null) {
				modService.writeSingleRegister(1, 0, Integer.parseInt(sp.replace(",", "")));
				lblSp2.setText(sp.contains(",") ? sp.replace(",", ".") : sp.concat(".0"));
			}
		}
	}

	@FXML
	private void initProc2() {
		provaDAO.updateDataInicial(prova2);
		lblInicio2.setText(horaFormatter.format(LocalDateTime.now()));
		btPlay2.setDisable(true);
		btStop2.setDisable(false);
		btExcluir2.setDisable(true);
		imgGlass2.setImage(gifGlassFile);
		imgMola2.setEffect(sepia2);
		chapaTransition2.play();
		tmlHeater2.play();
		chrono2.start(lblCrono2);
		isBalaoRunning2 = true;
		isBalaoReady2 = false;
	}

	@FXML
	private void stopProc2() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmar encerramento");
		alert.setHeaderText("Deseja realmente encerrar a prova em andamento?");
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() != ButtonType.OK) {
			return;
		}
		imgGlass2.setImage(imgGlassFile);
		tmlHeater2.stop();
		chapaTransition2.stop();
		imgMola2.setEffect(null);
		lnChapa2.setStroke(Color.RED);
		chrono2.stop();
		btAddProjeto2.setDisable(false);
		btPlay2.setDisable(true);
		btStop2.setDisable(true);
		btExcluir2.setDisable(false);
		isBalaoRunning2 = false;
		isBalaoFinished2 = true;
		provaDAO.updateDataFinal(prova2);
		makeToast("Balão 2: Prova encerrada.");
	}

	@FXML
	private void addProjeto2() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/Projetos.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Gerenciamento de projetos");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(rect2.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();

		if (ProvaProperty.provaProjetoProperty().get() != null) {
			projeto2 = ProvaProperty.provaProjetoProperty().get();
			txtProjeto2.setText(projeto2.getNome());
			UsedProjetosMap.useProjeto(Baloes.BALAO2, projeto2);
			enableForm(txtProduto2, txtNumero2, txtExecutor2, txtObjetivo2, btSaveBalao2);
		} else {
			disableForm(txtProduto2, txtNumero2, txtExecutor2, txtObjetivo2, btSaveBalao2);
		}
		tempMin2 = new Double(1900);
		tempMax2 = new Double(0);
		btAmostra2.setDisable(true);
		btExcluir2.setDisable(true);
		ProvaProperty.provaClear2Property().set(!prova2Clear);
		prova2Clear = !prova2Clear;

		txtProduto2.setText(null);
		txtNumero2.setText(null);
		txtExecutor2.setText(null);
		txtObjetivo2.setText(null);
		lblInicio2.setText("00:00:00");
		lblCrono2.setText("00:00:00");
		lblTempMax2.setText("000");
		lblTempMin2.setText("000");
		txtProduto2.requestFocus();
		// prova2 = null;
		// isBalaoFinished2 = false;
	}

	@FXML
	protected void addOrManageAmostra2() throws IOException {
		AmostraProperty.tempProperty().set(tempBalao2);
		AmostraProperty.setPointProperty().set(spBalao2);
		AmostraProperty.provaProperty().set(prova2);
		openAmostra();
	}

	protected void calculaMinMax2() {
		if (tempMin2 > tempBalao2) {
			tempMin2 = tempBalao2;
			lblTempMin2.setText(tempMin2.toString());
			prova2.setTempMin(tempMin2);
			provaDAO.updateTemperaturaMin(prova2);
		}
		if (tempMax2 < tempBalao2) {
			tempMax2 = tempBalao2;
			lblTempMax2.setText(tempMax2.toString());
			prova2.setTempMax(tempMax2);
			provaDAO.updateTemperaturaMax(prova2);
		}
	}

	public Double getTemp2() {
		return tempBalao2;
	}

}
