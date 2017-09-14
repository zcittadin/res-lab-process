package com.servicos.estatica.resicolor.lab.controller;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.dao.ProjetoDAO;
import com.servicos.estatica.resicolor.lab.model.Projeto;
import com.servicos.estatica.resicolor.lab.property.ProvaProperty;
import com.servicos.estatica.resicolor.lab.property.UsedProjetosMap;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

@SuppressWarnings("rawtypes")
public class ProjetosController implements Initializable {

	@FXML
	private Rectangle rect1;
	@FXML
	private TableView<Projeto> tblProjetos;
	@FXML
	private TableColumn colNome;
	@FXML
	private TableColumn colData;
	@FXML
	private TableColumn colTeorSolidos;
	@FXML
	private TableColumn colViscosidade;
	@FXML
	private TableColumn colCorGardner;
	@FXML
	private TableColumn colIndiceAcidez;
	@FXML
	private TableColumn colTeorOh;
	@FXML
	private TableColumn colPh;
	@FXML
	private TableColumn colDadosAdd;
	@FXML
	private TableColumn colExclusao;
	@FXML
	private TextField txtBuscar;
	@FXML
	private TextField txtSelecionado;
	@FXML
	private ProgressIndicator progProjetos;
	@FXML
	private Button btBuscar;
	@FXML
	private Button btOk;

	private static ProjetoDAO projetoDAO = new ProjetoDAO();
	private static Projeto projeto;

	private static ObservableList<Projeto> projetos = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rect1.setFill(Color.TRANSPARENT);

		tblProjetos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectProjeto(null);
			}
		});

		findProjetos();
	}

	@FXML
	private void addProjeto() throws IOException {
		Stage stage;
		Parent root;
		stage = new Stage();
		root = FXMLLoader.load(getClass().getResource("/com/servicos/estatica/resicolor/lab/app/CadastroProjeto.fxml"));
		stage.setScene(new Scene(root));
		stage.setTitle("Novo projeto");
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(tblProjetos.getScene().getWindow());
		stage.setResizable(Boolean.FALSE);
		stage.showAndWait();

		findProjetos();
	}

	@FXML
	private void findProjetoByNome(ActionEvent event) {
		if (txtBuscar.getText() == null || "".equals(txtBuscar.getText().trim())) {
			findProjetos();
		}
		fetch(true);

		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				projetos = FXCollections
						.observableList((List<Projeto>) projetoDAO.findProjetoByNome(txtBuscar.getText()));
				return null;
			}
		};
		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				fetch(false);
				populateTable();
			}
		});
		searchTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				fetch(false);
				makeAlert(AlertType.ERROR, "Erro", "Ocorreu uma falha ao consultar os projetos existentes.");
			}
		});
		Thread t = new Thread(searchTask);
		t.start();
	}

	@FXML
	private void ok(ActionEvent event) {
		Stage stage = (Stage) txtBuscar.getScene().getWindow();
		stage.close();
	}

	private void findProjetos() {
		fetch(true);

		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				projetos = FXCollections.observableList((List<Projeto>) projetoDAO.findLastProjetos());
				return null;
			}
		};
		searchTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				fetch(false);
				populateTable();
			}
		});
		searchTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				fetch(false);
				makeAlert(AlertType.ERROR, "Erro", "Ocorreu uma falha ao consultar os projetos existentes.");
			}
		});
		Thread t = new Thread(searchTask);
		t.start();
	}

	@SuppressWarnings("unchecked")
	private void populateTable() {

		tblProjetos.setItems(projetos);

		colNome.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Projeto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Projeto, String> cell) {
						final Projeto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(p.getNome());
						return simpleObject;
					}
				});

		colData.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Projeto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Projeto, String> cell) {
						final Projeto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject;
						if (p.getDtCriacao() != null) {
							simpleObject = new SimpleObjectProperty<String>(
									new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss").format(p.getDtCriacao()));
						} else {
							simpleObject = new SimpleObjectProperty<String>("Em andamento");
						}
						return simpleObject;
					}
				});

		colTeorSolidos.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Projeto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Projeto, String> cell) {
						final Projeto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getTeorSolidos());
						return simpleObject;
					}
				});

		colViscosidade.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Projeto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Projeto, String> cell) {
						final Projeto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getViscosidade());
						return simpleObject;
					}
				});
		colCorGardner.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Projeto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Projeto, String> cell) {
						final Projeto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getViscosidade());
						return simpleObject;
					}
				});
		colIndiceAcidez.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Projeto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Projeto, String> cell) {
						final Projeto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getIndiceAcidez());
						return simpleObject;
					}
				});
		colTeorOh.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Projeto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Projeto, String> cell) {
						final Projeto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getTeorOh());
						return simpleObject;
					}
				});
		colPh.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Projeto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Projeto, String> cell) {
						final Projeto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(p.getPh());
						return simpleObject;
					}
				});
		colDadosAdd.setCellValueFactory(
				new Callback<TableColumn.CellDataFeatures<Projeto, String>, ObservableValue<String>>() {
					public ObservableValue<String> call(CellDataFeatures<Projeto, String> cell) {
						final Projeto p = cell.getValue();
						final SimpleObjectProperty<String> simpleObject = new SimpleObjectProperty<String>(
								p.getDadosAdd());
						return simpleObject;
					}
				});

		Callback<TableColumn<Projeto, String>, TableCell<Projeto, String>> cellExcluirFactory = new Callback<TableColumn<Projeto, String>, TableCell<Projeto, String>>() {
			@Override
			public TableCell call(final TableColumn<Projeto, String> param) {
				final TableCell<Projeto, String> cell = new TableCell<Projeto, String>() {

					final Button btn = new Button();

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
							setText(null);
						} else {
							btn.setOnAction(event -> {
								Projeto projeto = getTableView().getItems().get(getIndex());
								if (UsedProjetosMap.isProjetoUsed(projeto)) {
									Alert alert = new Alert(AlertType.WARNING);
									alert.setTitle("Aten��o");
									alert.setHeaderText("Existe uma prova deste projeto em andamento.");
									alert.showAndWait();
									ProvaProperty.setProvaProjeto(null);
									return;
								}
								Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("Confirmar cancelamento");
								alert.setHeaderText("Os dados referentes a este processo ser�o perdidos. Confirmar?");
								Optional<ButtonType> result = alert.showAndWait();
								if (result.get() == ButtonType.OK) {
									Task<Void> exclusionTask = new Task<Void>() {
										@Override
										protected Void call() throws Exception {
											projetoDAO.removeProjeto(projeto);
											// processoDAO.removeProcesso(projeto);
											// processos.remove(projeto);
											projetos.remove(projeto);
											ProvaProperty.setProvaProjeto(null);
											populateTable();
											return null;
										}
									};

									exclusionTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
										@Override
										public void handle(WorkerStateEvent arg0) {
											if (!projetos.isEmpty()) {

											}
										}
									});
									Thread t = new Thread(exclusionTask);
									t.start();
								}
							});
							// Tooltip.install(btn, tooltipDelete);
							btn.setStyle("-fx-graphic: url('com/servicos/estatica/resicolor/lab/style/Delete.png');");
							btn.setCursor(Cursor.HAND);
							setGraphic(btn);
							setText(null);
						}
					}
				};
				return cell;
			}
		};

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				colExclusao.setCellFactory(cellExcluirFactory);
				colExclusao.setStyle("-fx-alignment: CENTER;");
				colData.setSortType(TableColumn.SortType.DESCENDING);
				colTeorSolidos.setStyle("-fx-alignment: CENTER;");
				colViscosidade.setStyle("-fx-alignment: CENTER;");
				colCorGardner.setStyle("-fx-alignment: CENTER;");
				colIndiceAcidez.setStyle("-fx-alignment: CENTER;");
				colTeorOh.setStyle("-fx-alignment: CENTER;");
				colPh.setStyle("-fx-alignment: CENTER;");
				colDadosAdd.setStyle("-fx-alignment: CENTER;");
				tblProjetos.setStyle("-fx-alignment: CENTER;");

			}
		});

	}

	private void selectProjeto(ActionEvent event) {
		projeto = (Projeto) tblProjetos.getSelectionModel().getSelectedItem();
		if (projeto == null) {
			tblProjetos.getSelectionModel().clearSelection();
			return;
		}
		txtSelecionado.setText(projeto.getNome());
		ProvaProperty.provaProjetoProperty().set(projeto);
	}

	private void fetch(Boolean b) {
		tblProjetos.setDisable(b);
		progProjetos.setVisible(b);
		btBuscar.setDisable(b);
		btOk.setDisable(b);
		txtBuscar.setDisable(b);
		txtSelecionado.setDisable(b);
	}

	private void makeAlert(AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(message);
		alert.showAndWait();
	}

}
