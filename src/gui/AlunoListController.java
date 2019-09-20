package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Aluno;
import model.services.AlunoService;

public class AlunoListController implements Initializable, DataChangeListener {

	private AlunoService service;

	@FXML
	private TableView<Aluno> tableViewAluno;

	@FXML
	private TableColumn<Aluno, Integer> tableColumnId;

	@FXML
	private TableColumn<Aluno, String> tableColumnNome;

	@FXML
	private TableColumn<Aluno, Date> tableColumnDataNasc;

	@FXML
	private TableColumn<Aluno, Date> tableColumnDataInicio;

	@FXML
	private TableColumn<Aluno, String> tableColumnTelefone;

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

	private ObservableList<Aluno> obsList;

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

	public void setAlunoService(AlunoService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnDataNasc.setCellValueFactory(new PropertyValueFactory<>("dataNasc"));
		Utils.formatTableColumnDate(tableColumnDataNasc, "dd/MM/yyyy");
		tableColumnDataInicio.setCellValueFactory(new PropertyValueFactory<>("dataInicio"));
		Utils.formatTableColumnDate(tableColumnDataInicio, "dd/MM/yyyy");
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewAluno.prefHeightProperty().bind(stage.heightProperty());
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setPrefWidth(53);
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
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Aluno obj) {
		Optional<ButtonType> confirm = Alerts.showConfirmation("Confirmação de exclusão",
				"Tem certeza que deseja excluir o aluno?");
		if (confirm.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serviço nulo");
			}
			try {
				service.remove(obj);
				updateTableView();
			} catch (DBIntegrityException e) {
				Alerts.showAlert("Erro ao remover o objeto", null, e.getMessage(), AlertType.ERROR);
			}

		}
	}

	public void findByName() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		if (txtPesquisaNome.getText().length() > 0) {
			obsList = FXCollections.observableArrayList(
					service.findByName(txtPesquisaNome.getText(), txtPesquisaNome.getText().length()));
		} else {
			obsList = FXCollections.observableArrayList(service.findAll());
		}
		tableViewAluno.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		initViewButtons();
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		obsList = FXCollections.observableArrayList(service.findAll());
		tableViewAluno.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
		initViewButtons();
	}

	public void createDialogForm(Aluno obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			AlunoFormController controller = loader.getController();
			controller.setAluno(obj);
			controller.setServices(new AlunoService());
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
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	public void createDialogView(Aluno obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			AlunoViewController controller = loader.getController();
			controller.setAluno(obj);
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
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

}
