package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DBIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.Aluno;
import model.entities.Plano;
import model.services.AlunoService;
import model.services.PlanoService;

public class AlunoListController implements Initializable, DataChangeListener {

	private String tabelaAluno;
	
	private String tabelaPlano;

	private AlunoService service;

	private PlanoService planoService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	private ObservableList<Plano> obsListPlano;

	@FXML
	private TableView<Aluno> tableViewAluno;

	@FXML
	private TableColumn<Aluno, Aluno> tableColumnATIVO;

	@FXML
	private TableColumn<Aluno, String> tableColumnNome;

	@FXML
	private TableColumn<Aluno, Date> tableColumnDataNasc;

	@FXML
	private TableColumn<Aluno, Date> tableColumnDataInicio;

	@FXML
	private TableColumn<Aluno, String> tableColumnTelefone;

	@FXML
	private TableColumn<Aluno, Aluno> tableColumnATTEND;

	@FXML
	private TableColumn<Aluno, Aluno> tableColumnVIEW;

	@FXML
	private TableColumn<Aluno, Aluno> tableColumnEDIT;

	@FXML
	private TableColumn<Aluno, Aluno> tableColumnREMOVE;

	@FXML
	private Button btnNovo;

	@FXML
	private Button btnPesquisaNome;

	@FXML
	private Button btnLimpaNome;

	@FXML
	private TextField txtPesquisaNome;

	@FXML
	private ComboBox<Plano> comboBoxPlano;

	@FXML
	private Button btnPesqusaPlano;

	@FXML
	private Button btnMostrarTodos;

	@FXML
	private Button btnMostrarPresentes;

	@FXML
	private Button btnMostrarAusentes;

	@FXML
	private Button btnMostrarInativos;

	@FXML
	private Label labelTotalAtivos;

	private ObservableList<Aluno> obsList;

	public String getTabelaAluno() {
		return tabelaAluno;
	}
	
	public void setTabelas(String tabelaAluno, String tabelaPlano) {
		this.tabelaAluno = tabelaAluno;
		this.tabelaPlano = tabelaPlano;
	}

	public String getTabelaPlano() {
		return tabelaPlano;
	}

	@FXML
	public void onBtnNovoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Aluno obj = new Aluno();
		createDialogForm(obj, "/gui/AlunoForm.fxml", parentStage);		
	}

	@FXML
	public void onBtnPesquisaNomeAction() {
		findByName();
	}

	@FXML
	public void onBtnLimpaNomeAction() {
		txtPesquisaNome.setText("");
		findByName();
	}

	@FXML
	public void onBtnPesquisaPlanoAction() {
		if (comboBoxPlano.getValue().getId() != null) {
			findByPlano(comboBoxPlano.getValue().getId());

		} else {
			findAll();
		}
	}

	@FXML
	public void onBtnMostrarTodosAction() {
		findAll();
	}

	@FXML
	public void onBtnMostrarPresentesAction() {
		findByPresenca(true);
	}

	@FXML
	public void onBtnMostrarAusentesAction() {
		findByPresenca(false);
	}

	@FXML
	public void onBtnMostrarInativosAction() {
		findByAtivos(false);
	}

	public void setAlunoService(AlunoService service) {
		this.service = service;
	}

	public void setPlanoService(PlanoService planoService) {
		this.planoService = planoService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

//	private void notifyDataChangeListeners() {
//		for (DataChangeListener listener : dataChangeListeners) {
//			listener.onDataChanged();
//		}
//	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
		initializeComboBoxPlano();
	}

	private void initializeNodes() {
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnDataNasc.setCellValueFactory(new PropertyValueFactory<>("dataNasc"));
		Utils.formatTableColumnDate(tableColumnDataNasc, "dd/MM/yyyy");
		tableColumnDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
		Utils.formatTableColumnDate(tableColumnDataInicio, "dd/MM/yyyy");
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

		initializeComboBoxPlano();

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewAluno.prefHeightProperty().bind(stage.heightProperty());

	}

	private void initializeComboBoxPlano() {
		Callback<ListView<Plano>, ListCell<Plano>> factory = lv -> new ListCell<Plano>() {
			@Override
			protected void updateItem(Plano item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};

		comboBoxPlano.setCellFactory(factory);
		comboBoxPlano.setButtonCell(factory.call(null));
	}

	public void loadAssociatedObjects(String tabelaPlano) {
		if (planoService == null) {
			throw new IllegalStateException("Serviço de plano está nulo.");
		}
		tabelaPlano = this.tabelaPlano;
		List<Plano> list = planoService.findAll(tabelaPlano);
		Plano todos = new Plano(null, "Todos", null, null);
		list.add(0, todos);
		obsListPlano = FXCollections.observableArrayList(list);
		comboBoxPlano.setItems(obsListPlano);
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setPrefWidth(53);
		tableColumnEDIT.setStyle("-fx-alignment: CENTER");
		tableColumnEDIT.setCellFactory(param -> new TableCell<Aluno, Aluno>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(Aluno obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/AlunoForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initViewButtons() {
		tableColumnVIEW.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnVIEW.setPrefWidth(72);
		tableColumnVIEW.setStyle("-fx-alignment: CENTER");
		tableColumnVIEW.setCellFactory(param -> new TableCell<Aluno, Aluno>() {
			private final Button button = new Button("Visualizar");

			@Override
			protected void updateItem(Aluno obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogView(obj, "/gui/AlunoView.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setPrefWidth(70);
		tableColumnREMOVE.setStyle("-fx-alignment: CENTER");
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Aluno, Aluno>() {
			private final Button button = new Button("Remover");

			@Override
			protected void updateItem(Aluno obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> {
					removeEntity(obj);
				});
			}
		});
	}

	private void initCheckBoxesAtivo() {
		tableColumnATIVO.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnATIVO.setPrefWidth(58);
		tableColumnATIVO.setStyle("-fx-alignment: CENTER");
		tableColumnATIVO.setCellFactory(param -> new TableCell<Aluno, Aluno>() {
			private final CheckBox ativo = new CheckBox();

			@Override
			protected void updateItem(Aluno obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(ativo);
				ativo.setSelected(obj.getAtivo());
				ativo.setOnAction(event -> {
					updateAtivo(obj, ativo.isSelected());
					labelTotalAtivos.setText(service.contarAlunos(tabelaAluno, tabelaPlano).toString());
				});

			}

		});
	}

	private void initCheckBoxesAttend() {
		tableColumnATTEND.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnATTEND.setPrefWidth(58);
		tableColumnATTEND.setStyle("-fx-alignment: CENTER");
		tableColumnATTEND.setCellFactory(param -> new TableCell<Aluno, Aluno>() {
			private final CheckBox presenca = new CheckBox();

			@Override
			protected void updateItem(Aluno obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(presenca);
				presenca.setSelected(obj.getPresenca());
				presenca.setOnAction(event -> updateAttend(obj, presenca.isSelected()));
			}
		});
	}

	private void removeEntity(Aluno obj) {
		Optional<ButtonType> confirm = Alerts.showConfirmation("Confirmação de exclusão",
				"Tem certeza que deseja excluir o aluno?");
		if (confirm.get() == ButtonType.YES) {
			if (service == null) {
				throw new IllegalStateException("Serviço nulo");
			}
			try {
				service.remove(obj, tabelaAluno);
				updateTableView();
			} catch (DBIntegrityException e) {
				Alerts.showAlert("Erro ao remover o objeto", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}

	private void updateAtivo(Aluno obj, Boolean ativo) {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			obj.setAtivo(ativo);
			service.updateAtivo(obj, tabelaAluno);
		} catch (DBIntegrityException e) {
			Alerts.showAlert("Erro ao atualizar o estado do aluno", null, e.getMessage(), AlertType.ERROR);
		}
	}

	private void updateAttend(Aluno obj, Boolean presenca) {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			obj.setPresenca(presenca);
			service.updatePresenca(obj, tabelaAluno);
		} catch (DBIntegrityException e) {
			Alerts.showAlert("Erro ao atualizar a presença", null, e.getMessage(), AlertType.ERROR);
		}
	}

	public void findByName() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		if (txtPesquisaNome.getText().length() > 0) {
			obsList = FXCollections.observableArrayList(service.findByName(txtPesquisaNome.getText(),
					txtPesquisaNome.getText().length(), tabelaAluno, tabelaPlano));
		} else {
			obsList = FXCollections.observableArrayList(service.findAll(tabelaAluno, tabelaPlano));
		}
		tableViewAluno.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		initViewButtons();
		initCheckBoxesAtivo();
		initCheckBoxesAttend();
	}

	public void findByPlano(Integer planoId) {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		if (comboBoxPlano.getValue() != null) {
			obsList = FXCollections.observableArrayList(
					service.findByPlano(comboBoxPlano.getValue().getId(), tabelaAluno, tabelaPlano));
		} else {
			obsList = FXCollections.observableArrayList(service.findAll(tabelaAluno, tabelaPlano));
		}
		tableViewAluno.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		initViewButtons();
		initCheckBoxesAtivo();
		initCheckBoxesAttend();
	}

	public void findByPresenca(Boolean presenca) {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		obsList = FXCollections.observableArrayList(service.findByPresenca(presenca, tabelaAluno, tabelaPlano));
		tableViewAluno.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		initViewButtons();
		initCheckBoxesAtivo();
		initCheckBoxesAttend();
	}

	public void findByAtivos(Boolean ativo) {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}

		obsList = FXCollections.observableArrayList(service.findByAtivo(ativo, tabelaAluno, tabelaPlano));
		tableViewAluno.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		initViewButtons();
		initCheckBoxesAtivo();
		initCheckBoxesAttend();
	}

	public void saveByPresenca(Boolean presenca, String filepath) {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		service.saveByPresenca(presenca, filepath);
	}

	public void findAll() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		obsList = FXCollections.observableArrayList(service.findAll(tabelaAluno, tabelaPlano));

		tableViewAluno.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		initViewButtons();
		initCheckBoxesAtivo();
		initCheckBoxesAttend();
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		obsList = FXCollections.observableArrayList(service.findAll(tabelaAluno, tabelaPlano));
		tableViewAluno.setItems(obsList);
		labelTotalAtivos.setText(service.contarAlunos(tabelaAluno, tabelaPlano).toString());
		initEditButtons();
		initRemoveButtons();
		initViewButtons();
		initCheckBoxesAtivo();
		initCheckBoxesAttend();
	}

	public void createDialogForm(Aluno obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			AlunoFormController controller = loader.getController();
			controller.setAluno(obj);
			controller.setTabelas(tabelaAluno, tabelaPlano);
			controller.setServices(new AlunoService(), new PlanoService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Informe os dados do aluno");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro ao carregar nova janela.", e.getMessage(), AlertType.ERROR);
		}
	}

	public void createDialogView(Aluno obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			AlunoViewController controller = loader.getController();
			controller.setAluno(obj);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Dados do aluno");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

	public void backupDados() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		service.backupDados();
	}

	public void lerBackup(String filepath) {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		service.lerBackup(filepath);
	}

}
