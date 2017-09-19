package com.servicos.estatica.resicolor.lab.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.app.ControlledScreen;
import com.servicos.estatica.resicolor.lab.dao.ProvaDAO;
import com.servicos.estatica.resicolor.lab.model.Prova;
import com.servicos.estatica.resicolor.lab.property.AmostraProperty;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

@SuppressWarnings("rawtypes")
public class AmostraEdicaoController implements Initializable, ControlledScreen {

	@FXML
	private Rectangle rect1;
	@FXML
	private Rectangle rect2;
	@FXML
	private TableView tblProvas;
	@FXML
	private TableColumn colNome;
	@FXML
	private TableColumn colProjeto;
	@FXML
	private TableColumn colObjetivo;
	@FXML
	private TableColumn colExecutor;
	@FXML
	private TableColumn colBalao;
	@FXML
	private TableColumn colNumero;
	@FXML
	private TableColumn colFinalizado;
	@FXML
	private RadioButton rdProjeto;
	@FXML
	private RadioButton rdPeriodo;
	@FXML
	private RadioButton rdNomeProva;
	@FXML
	private TextField txtProjeto;
	@FXML
	private TextField txtNomeProva;
	@FXML
	private DatePicker dtpInicio;
	@FXML
	private DatePicker dtpFim;
	@FXML
	private Button btSearch;
	@FXML
	private Button btAmostras;
	@FXML
	private ProgressIndicator progDados;
	@FXML
	private ProgressIndicator progTable;
	@FXML
	private Label lblProvaSelecionada;

	private static ObservableList<Prova> provas = FXCollections.observableArrayList();
	ToggleGroup groupForm = new ToggleGroup();

	private static Prova prova;

	private static ProvaDAO provaDAO = new ProvaDAO();
	ScreensController myController;

	@Override
	public void setScreenParent(ScreensController screenPage) {
		myController = screenPage;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		rect1.setFill(Color.TRANSPARENT);
		rect2.setFill(Color.TRANSPARENT);
		rdProjeto.setToggleGroup(groupForm);
		rdPeriodo.setToggleGroup(groupForm);
		rdNomeProva.setToggleGroup(groupForm);
		retrieveLastProvas();
	}

	@FXML
	private void consultar() {
		initFetch();
		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				prova = null;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lblProvaSelecionada.setText("Nenhuma prova selecionada");
						btAmostras.setDisable(true);
					}
				});

				if (rdProjeto.isSelected()) {
					provas = FXCollections.observableList((List<Prova>) provaDAO.findByProjeto(txtProjeto.getText()));
					return null;
				}
				if (rdPeriodo.isSelected()) {
					provas = FXCollections.observableList((List<Prova>) provaDAO
							.findByPeriodo(dtpInicio.getValue().toString(), dtpFim.getValue().toString()));
					return null;
				}
				if (rdNomeProva.isSelected()) {
					provas = FXCollections.observableList((List<Prova>) provaDAO.findByNome(txtNomeProva.getText()));
					return null;
				}
				return null;
			}
		};

		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				endFetch();
				populateTable();
			}
		});
		searchTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				endFetch();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Falha");
				alert.setHeaderText("Ocorreu um erro ao consultar os dados.");
				alert.showAndWait();
			}
		});
		Thread t = new Thread(searchTask);
		t.start();
	}

	@FXML
	private void openAmostra() throws IOException {
		AmostraProperty.tempProperty().set(0);
		AmostraProperty.setPointProperty().set(0);
		AmostraProperty.provaProperty().set(prova);
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/Amostra.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Gerenciamento de amostras");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();
	}

	@FXML
	private void selectByProjeto() {
		txtProjeto.setDisable(false);
		dtpInicio.getEditor().clear();
		dtpFim.getEditor().clear();
		txtNomeProva.clear();
		dtpInicio.setDisable(true);
		dtpFim.setDisable(true);
		txtNomeProva.setDisable(true);
		txtProjeto.requestFocus();
	}

	@FXML
	private void selectByNomeProva() {
		txtProjeto.clear();
		dtpInicio.getEditor().clear();
		dtpFim.getEditor().clear();
		txtNomeProva.setDisable(false);
		txtProjeto.setDisable(true);
		dtpInicio.setDisable(true);
		dtpFim.setDisable(true);
		txtNomeProva.requestFocus();
	}

	@FXML
	private void selectByPeriodo() {
		txtProjeto.clear();
		txtNomeProva.clear();
		txtProjeto.setDisable(true);
		txtNomeProva.setDisable(true);
		dtpInicio.setDisable(false);
		dtpFim.setDisable(false);
	}

	@FXML
	public void selectProva() {
		prova = (Prova) tblProvas.getSelectionModel().getSelectedItem();
		if (prova == null) {
			tblProvas.getSelectionModel().clearSelection();
			btAmostras.setDisable(true);
			return;
		}
		lblProvaSelecionada.setText(prova.getNomeProva());
		btAmostras.setDisable(false);
	}

	private void retrieveLastProvas() {
		initFetch();
		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				provas = FXCollections.observableList((List<Prova>) provaDAO.findLastFinalizados(13));
				return null;
			}
		};
		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				endFetch();
				populateTable();
			}
		});
		searchTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				endFetch();
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Falha");
				alert.setHeaderText("Ocorreu um erro ao consultar os dados.");
				alert.showAndWait();
			}
		});
		Thread t = new Thread(searchTask);
		t.start();

	}

	@SuppressWarnings("unchecked")
	private void populateTable() {
		tblProvas.setItems(provas);
		colNome.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Prova, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Prova, String> cell) {
						final Prova p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getNomeProva());
						return simpleObject;
					}
				});
		colProjeto.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Prova, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Prova, String> cell) {
						final Prova p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getProjeto().getNome());
						return simpleObject;
					}
				});
		colObjetivo.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Prova, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Prova, String> cell) {
						final Prova p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getObjetivo());
						return simpleObject;
					}
				});
		colExecutor.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Prova, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Prova, String> cell) {
						final Prova p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getExecutor());
						return simpleObject;
					}
				});
		colBalao.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Prova, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Prova, String> cell) {
						final Prova p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getBalao());
						return simpleObject;
					}
				});
		colNumero.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Prova, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Prova, String> cell) {
						final Prova p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getProduto());
						return simpleObject;
					}
				});
		colFinalizado.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Prova, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Prova, String> cell) {
						final Prova p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getDhFinal() == null ? "Não finalizada"
										: new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(p.getDhFinal()));
						return simpleObject;
					}
				});
		colNome.setStyle("-fx-alignment: CENTER;");
		colProjeto.setStyle("-fx-alignment: CENTER;");
		colObjetivo.setStyle("-fx-alignment: CENTER;");
		colExecutor.setStyle("-fx-alignment: CENTER;");
		colBalao.setStyle("-fx-alignment: CENTER;");
		colNumero.setStyle("-fx-alignment: CENTER;");
		colFinalizado.setStyle("-fx-alignment: CENTER;");
		tblProvas.getColumns().setAll(colNome, colProjeto, colObjetivo, colExecutor, colBalao, colNumero,
				colFinalizado);

	}

	private void initFetch() {
		progDados.setVisible(true);
		progTable.setVisible(true);
		rdNomeProva.setDisable(true);
		rdProjeto.setDisable(true);
		rdPeriodo.setDisable(true);
		txtProjeto.setDisable(true);
		txtNomeProva.setDisable(true);
		dtpInicio.setDisable(true);
		dtpFim.setDisable(true);
		btSearch.setDisable(true);
		tblProvas.setDisable(true);
	}

	private void endFetch() {
		progDados.setVisible(false);
		progTable.setVisible(false);
		rdNomeProva.setDisable(false);
		rdProjeto.setDisable(false);
		rdPeriodo.setDisable(false);
		btSearch.setDisable(false);
		tblProvas.setDisable(false);
		if (rdProjeto.isSelected()) {
			txtProjeto.setDisable(false);
		}
		if (rdNomeProva.isSelected()) {
			txtNomeProva.setDisable(false);
		}
		if (rdPeriodo.isSelected()) {
			dtpInicio.setDisable(false);
			dtpFim.setDisable(false);
		}
	}

}
