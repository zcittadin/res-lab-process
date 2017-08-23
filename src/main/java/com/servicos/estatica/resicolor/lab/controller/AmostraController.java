package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.dao.AmostraDAO;
import com.servicos.estatica.resicolor.lab.model.Amostra;
import com.servicos.estatica.resicolor.lab.property.AmostraProperty;
import com.servicos.estatica.resicolor.lab.util.NumberUtil;

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
import javafx.scene.control.TextFormatter;
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
	private static DecimalFormat decimalFormat = new DecimalFormat("#.0");

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rectangle.setFill(Color.TRANSPARENT);
		prepareFields();
		configTable();
		configNumberFields();
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
				amostra = new Amostra(null, AmostraProperty.getProva(), new Date(),
						Integer.parseInt(NumberUtil.adjustDecimal(txtTemp.getText(), ",", ".")),
						Integer.parseInt(NumberUtil.adjustDecimal(txtSetPoint.getText(), ",", ".")),
						Double.parseDouble(NumberUtil.adjustDecimal(txtIAsobreNV.getText(), ",", ".")),
						txtViscGardner.getText(), txtCorGardner.getText(),
						Double.parseDouble(NumberUtil.adjustDecimal(txtNV.getText(), ",", ".")),
						Integer.parseInt(NumberUtil.adjustDecimal(txtGelTime.getText(), ",", ".")),
						Double.parseDouble(NumberUtil.adjustDecimal(txtAgua.getText(), ",", ".")),
						Double.parseDouble(NumberUtil.adjustDecimal(txtAmostra.getText(), ",", ".")),
						Double.parseDouble(NumberUtil.adjustDecimal(txtPH.getText(), ",", ".")),
						txtDescricao.getText());
				amostraDAO.saveAmostra(amostra);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@SuppressWarnings("unchecked")
			@Override
			public void handle(WorkerStateEvent arg0) {
				txtDescricao.setText(null);
				txtIAsobreNV.setText(null);
				txtViscGardner.setText(null);
				txtCorGardner.setText(null);
				txtNV.setText(null);
				txtGelTime.setText(null);
				txtAgua.setText(null);
				txtAmostra.setText(null);
				txtPH.setText(null);
				amostras = amostraDAO.findByProva(AmostraProperty.getProva());
				amostrasItens = FXCollections.observableArrayList(amostras);
				tblAmostra.setItems(amostrasItens);
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {

			}
		});
		Thread t = new Thread(saveTask);
		t.start();
	}

	@FXML
	public void selectAmostra() {
		amostra = (Amostra) tblAmostra.getSelectionModel().getSelectedItem();
		if (amostra == null) {
			tblAmostra.getSelectionModel().clearSelection();
			return;
		}

		txtDescricao.setText(amostra.getDescricao());
		txtIAsobreNV.setText(NumberUtil.adjustDecimal(new Double(amostra.getIaSobreNv()).toString(), ".", ","));
		txtViscGardner.setText(amostra.getViscGardner());
		txtCorGardner.setText(amostra.getCorGardner());
		txtNV.setText(NumberUtil.adjustDecimal(new Double(amostra.getPercentualNv()).toString(), ".", ","));
		txtGelTime.setText(NumberUtil.adjustDecimal(new Integer(amostra.getGelTime()).toString(), ".", ","));
		txtAgua.setText(NumberUtil.adjustDecimal(new Double(amostra.getAgua()).toString(), ".", ","));
		txtAmostra.setText(NumberUtil.adjustDecimal(new Double(amostra.getAmostra()).toString(), ".", ","));
		txtPH.setText(NumberUtil.adjustDecimal(new Double(amostra.getPh()).toString(), ".", ","));
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

		colAgua.setStyle("-fx-alignment: CENTER;");
		colAmostra.setStyle("-fx-alignment: CENTER;");
		colCorGardner.setStyle("-fx-alignment: CENTER;");
		colDescricao.setStyle("-fx-alignment: CENTER;");
		colGelTime.setStyle("-fx-alignment: CENTER;");
		colHora.setStyle("-fx-alignment: CENTER;");
		colIAsobreNV.setStyle("-fx-alignment: CENTER;");
		colNv.setStyle("-fx-alignment: CENTER;");
		colPh.setStyle("-fx-alignment: CENTER;");
		colSetPoint.setStyle("-fx-alignment: CENTER;");
		colTemp.setStyle("-fx-alignment: CENTER;");
		colViscGardner.setStyle("-fx-alignment: CENTER;");
	}

	private void configNumberFields() {
		formatNumberField(txtIAsobreNV);
		formatNumberField(txtNV);
		formatNumberField(txtAgua);
		formatNumberField(txtAmostra);
		formatNumberField(txtPH);

		txtGelTime.textProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue == null)
				return;
			if (!newValue.matches("\\d*")) {
				txtGelTime.setText(newValue.replaceAll("[^\\d]", ""));
			}
		});
	}

	private void formatNumberField(TextField txtField) {
		txtField.setTextFormatter(new TextFormatter<>(c -> {
			if (c.getControlNewText().isEmpty()) {
				return c;
			}
			ParsePosition parsePosition = new ParsePosition(0);
			Object object = decimalFormat.parse(c.getControlNewText(), parsePosition);

			if (object == null || parsePosition.getIndex() < c.getControlNewText().length()) {
				return null;
			} else {
				return c;
			}
		}));
	}

}
