package com.servicos.estatica.resicolor.lab.controller;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.servicos.estatica.resicolor.lab.dao.ProjetoDAO;
import com.servicos.estatica.resicolor.lab.model.Projeto;
import com.servicos.estatica.resicolor.lab.property.ProvaProperty;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
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
import javafx.stage.Stage;
import javafx.util.Callback;

@SuppressWarnings("rawtypes")
public class ProjetosController implements Initializable {

	@FXML
	private Rectangle rect1;
	@FXML
	private Rectangle rect2;
	@FXML
	private TableView<Projeto> tblProjetos;
	@FXML
	private TableColumn colNome;
	@FXML
	private TableColumn colData;
	@FXML
	private TableColumn colExclusao;
	@FXML
	private TextField txtBuscar;
	@FXML
	private TextField txtNovo;
	@FXML
	private TextField txtSelecionado;
	@FXML
	private ProgressIndicator progProjetos;
	@FXML
	private Button btBuscar;
	@FXML
	private Button btSalvar;
	@FXML
	private Button btOk;

	private static ProjetoDAO projetoDAO = new ProjetoDAO();
	private static Projeto projeto;

	private static ObservableList<Projeto> projetos = FXCollections.observableArrayList();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rect1.setFill(Color.TRANSPARENT);
		rect2.setFill(Color.TRANSPARENT);

		tblProjetos.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			if (newSelection != null) {
				selectProjeto(null);
			}
		});

		findProjetos();
	}

	@FXML
	private void saveProjeto(ActionEvent event) {

		if (txtNovo.getText() == null || "".equals(txtNovo.getText().trim())) {
			makeAlert(AlertType.WARNING, "Atenção", "Informe um nome para o novo projeto.");
			txtNovo.requestFocus();
			return;
		}
		fetch(true);
		Task<Void> saveTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				projeto = new Projeto(null, null, txtNovo.getText(), Calendar.getInstance().getTime());
				projetoDAO.saveProjeto(projeto);
				return null;
			}
		};
		saveTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				fetch(false);
				txtNovo.setText(null);
				projetos.add(projeto);
				populateTable();
			}
		});
		saveTask.setOnFailed(new EventHandler<WorkerStateEvent>() {
			@Override
			public void handle(WorkerStateEvent arg0) {
				fetch(false);
				makeAlert(AlertType.ERROR, "Erro", "Ocorreu uma falha ao salvar o projeto.");
			}
		});
		Thread t = new Thread(saveTask);
		t.start();
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
								Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle("Confirmar cancelamento");
								alert.setHeaderText("Os dados referentes a este processo serão perdidos. Confirmar?");
								Optional<ButtonType> result = alert.showAndWait();
								if (result.get() == ButtonType.OK) {
									Projeto projeto = getTableView().getItems().get(getIndex());
									Task<Void> exclusionTask = new Task<Void>() {
										@Override
										protected Void call() throws Exception {
											// leituraDAO.removeLeituras(projeto);
											// processoDAO.removeProcesso(projeto);
											// processos.remove(projeto);
											// tblConsulta.refresh();
											System.out.println("Excluindo: " + projeto.getNome());
											return null;
										}
									};

									exclusionTask.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
										@Override
										public void handle(WorkerStateEvent arg0) {
											if (!projetos.isEmpty()) {
												// makeToast("Processo removido
												// com sucesso.");
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
		colExclusao.setCellFactory(cellExcluirFactory);
		colExclusao.setStyle("-fx-alignment: CENTER;");

		colData.setSortType(TableColumn.SortType.DESCENDING);
		tblProjetos.getSortOrder().add(colData);

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
		btSalvar.setDisable(b);
		btOk.setDisable(b);
		txtBuscar.setDisable(b);
		txtNovo.setDisable(b);
		txtSelecionado.setDisable(b);
	}

	private void makeAlert(AlertType type, String title, String message) {
		Alert alert = new Alert(type);
		alert.setTitle(title);
		alert.setHeaderText(message);
		alert.showAndWait();
	}

}
