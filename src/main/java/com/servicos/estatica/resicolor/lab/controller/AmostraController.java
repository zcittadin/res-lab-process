package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.dao.AmostraDAO;
import com.servicos.estatica.resicolor.lab.model.Amostra;
import com.servicos.estatica.resicolor.lab.property.AmostraProperty;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Callback;

@SuppressWarnings("rawtypes")
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
	@FXML
	private TableView tblAmostra;
	@FXML
	private TableColumn colHora;
	@FXML
	private TableColumn colTemp;
	@FXML
	private TableColumn colSetPoint;
	@FXML
	private TableColumn colIAsobreNV;
	@FXML
	private TableColumn colViscGardner;
	@FXML
	private TableColumn colCorGardner;
	@FXML
	private TableColumn colNv;
	@FXML
	private TableColumn colGelTime;
	@FXML
	private TableColumn colAgua;
	@FXML
	private TableColumn colAmostra;
	@FXML
	private TableColumn colPh;
	@FXML
	private TableColumn colDescricao;

	private static Amostra amostra;
	private List<Amostra> amostras;
	private static ObservableList<Amostra> amostrasItens = FXCollections.observableArrayList();
	private static AmostraDAO amostraDAO = new AmostraDAO();

	private static DateTimeFormatter dataHoraFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rectangle.setFill(Color.TRANSPARENT);
		initListeners();
		prepareFields();
		configTable();
		amostras = amostraDAO.findByProva(AmostraProperty.getProva());
		amostrasItens = FXCollections.observableArrayList(amostras);
		tblAmostra.setItems(amostrasItens);
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
				amostra = new Amostra(null, AmostraProperty.getProva(), new Date(),
						txtTemp.getText() != null && txtTemp.getText().trim() != ""
								? Integer.parseInt(txtTemp.getText()) : 0,
						txtSetPoint.getText() != null && txtSetPoint.getText().trim() != ""
								? Integer.parseInt(txtSetPoint.getText()) : 0,
						txtIAsobreNV.getText() != null && txtIAsobreNV.getText().trim() != ""
								? Double.parseDouble(txtIAsobreNV.getText()) : 0,
						txtViscGardner.getText(), txtCorGardner.getText(),
						txtNV.getText() != null && txtNV.getText().trim() != "" ? Double.parseDouble(txtNV.getText())
								: 0,
						txtGelTime.getText() != null && txtGelTime.getText().trim() != ""
								? Integer.parseInt(txtGelTime.getText()) : 0,
						txtAgua.getText() != null && txtAgua.getText().trim() != ""
								? Double.parseDouble(txtAgua.getText()) : 0,
						txtAmostra.getText() != null && txtAmostra.getText().trim() != ""
								? Double.parseDouble(txtAmostra.getText()) : 0,
						txtPH.getText() != null && txtPH.getText().trim() != "" ? Double.parseDouble(txtPH.getText())
								: 0,
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

	@FXML
	public void voltar() {
		Stage stage = (Stage) tblAmostra.getScene().getWindow();
		stage.close();
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

	@SuppressWarnings("unchecked")
	private void configTable() {
		colHora.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Amostra, String> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<String> simpleObject;
						simpleObject = new SimpleObjectProperty<String>(
								new SimpleDateFormat("HH:mm:ss").format(a.getHorario()));
						return simpleObject;
					}
				});
		colTemp.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<Amostra, Integer> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<Integer> simpleObject = new SimpleObjectProperty<Integer>(
								a.getTemp());
						return simpleObject;
					}
				});
		colSetPoint.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<Amostra, Integer> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<Integer> simpleObject = new SimpleObjectProperty<Integer>(
								a.getSetPoint());
						return simpleObject;
					}
				});
		colIAsobreNV.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, Double>, ObservableValue<Double>>() {
					public ObservableValue<Double> call(CellDataFeatures<Amostra, Double> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<Double> simpleObject = new SimpleObjectProperty<Double>(
								a.getIaSobreNv());
						return simpleObject;
					}
				});
		colViscGardner.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Amostra, String> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								a.getViscGardner());
						return simpleObject;
					}
				});
		colCorGardner.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Amostra, String> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								// "");
								a.getCorGardner());
						return simpleObject;
					}
				});
		colNv.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, Double>, ObservableValue<Double>>() {
					public ObservableValue<Double> call(CellDataFeatures<Amostra, Double> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<Double> simpleObject = new SimpleObjectProperty<Double>(
								a.getPercentualNv());
						return simpleObject;
					}
				});
		colGelTime.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, Integer>, ObservableValue<Integer>>() {
					public ObservableValue<Integer> call(CellDataFeatures<Amostra, Integer> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<Integer> simpleObject = new SimpleObjectProperty<Integer>(
								a.getGelTime());
						return simpleObject;
					}
				});
		colAgua.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, Double>, ObservableValue<Double>>() {
					public ObservableValue<Double> call(CellDataFeatures<Amostra, Double> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<Double> simpleObject = new SimpleObjectProperty<Double>(a.getAgua());
						return simpleObject;
					}
				});
		colAmostra.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, Double>, ObservableValue<Double>>() {
					public ObservableValue<Double> call(CellDataFeatures<Amostra, Double> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<Double> simpleObject = new SimpleObjectProperty<Double>(
								a.getAmostra());
						return simpleObject;
					}
				});
		colPh.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, Double>, ObservableValue<Double>>() {
					public ObservableValue<Double> call(CellDataFeatures<Amostra, Double> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<Double> simpleObject = new SimpleObjectProperty<Double>(a.getPh());
						return simpleObject;
					}
				});
		colDescricao.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Amostra, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Amostra, String> cell) {
						final Amostra a = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								a.getDescricao());
						return simpleObject;
					}
				});
	}

	private void initListeners() {
		/*
		 * txtIAsobreNV.textProperty().addListener((observable, oldValue,
		 * newValue) -> { if
		 * (!newValue.matches("[0-9]{1,3}(,([0-9]{3}))*(.[0-9]+)?")) {
		 * txtIAsobreNV.setText(newValue.replaceAll("[^\\d]", "")); } });
		 * txtNV.textProperty().addListener((observable, oldValue, newValue) ->
		 * { if (!newValue.matches("\\d*")) {
		 * txtNV.setText(newValue.replaceAll("[^\\d]", "")); } });
		 * txtGelTime.textProperty().addListener((observable, oldValue,
		 * newValue) -> { if (!newValue.matches("\\d*")) {
		 * txtGelTime.setText(newValue.replaceAll("[^\\d]", "")); } });
		 * txtAgua.textProperty().addListener((observable, oldValue, newValue)
		 * -> { if (!newValue.matches("\\d*")) {
		 * txtAgua.setText(newValue.replaceAll("[^\\d]", "")); } });
		 * txtAmostra.textProperty().addListener((observable, oldValue,
		 * newValue) -> { if (!newValue.matches("\\d*")) {
		 * txtAmostra.setText(newValue.replaceAll("[^\\d]", "")); } });
		 * txtPH.textProperty().addListener((observable, oldValue, newValue) ->
		 * { if (!newValue.matches("\\d*")) {
		 * txtPH.setText(newValue.replaceAll("[^\\d]", "")); } });
		 */

	}

}
