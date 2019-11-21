package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
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
import model.services.PlanoService;

public class FinancListController implements Initializable, DataChangeListener {

	private AlunoService service;

	@FXML
	private TableView<Aluno> tableViewAluno;

	@FXML
	private TableColumn<Aluno, Integer> tableColumnId;

	@FXML
	private TableColumn<Aluno, String> tableColumnNome;

	@FXML
	private TableColumn<Aluno, String> tableColumnTelefone;

	@FXML
	private TableColumn<Aluno, Date> tableColumnVencimento;

	@FXML
	private TableColumn<Aluno, Aluno> tableColumnPAY;

	@FXML
	private TableColumn<Aluno, Aluno> tableColumnEDIT;

	@FXML
	private Button btnPesquisaNome;

	@FXML
	private Button btnLimpaNome;

	@FXML
	private TextField txtPesquisaNome;

	@FXML
	private Button btnMostrarTodos;

	@FXML
	private Button btnMostrarPendentes;

	private ObservableList<Aluno> obsList;

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
	public void onBtnMostrarTodosAction() {
		findAll();
	}

	@FXML
	public void onBtnMostrarPendentesAction() {
		findByVencimento(java.sql.Date.valueOf(LocalDate.now()));
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
		tableColumnVencimento.setCellValueFactory(new PropertyValueFactory<>("vencimento"));
		Utils.formatTableColumnDate(tableColumnVencimento, "dd/MM/yyyy");
		tableColumnTelefone.setCellValueFactory(new PropertyValueFactory<>("telefone"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewAluno.prefHeightProperty().bind(stage.heightProperty());
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
				button.setOnAction(event -> createDialogFinancForm(obj, "/gui/FinancForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initPayButtons() {
		tableColumnPAY.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnPAY.setPrefWidth(84);
		tableColumnPAY.setStyle("-fx-alignment: CENTER");
		tableColumnPAY.setCellFactory(param -> new TableCell<Aluno, Aluno>() {
			private final Button button = new Button("Pagamento");

			@Override
			protected void updateItem(Aluno obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> createDialogPayForm(obj, "/gui/PayForm.fxml", Utils.currentStage(event)));
			}
		});
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
		initPayButtons();
		initEditButtons();
	}

	public void findAll() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		obsList = FXCollections.observableArrayList(service.findAll());

		tableViewAluno.setItems(obsList);
		initPayButtons();
		initEditButtons();
	}
	
	public void findByVencimento(Date data) {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		obsList = FXCollections.observableArrayList(service.findByVencimento(data));
		
		tableViewAluno.setItems(obsList);
		initPayButtons();
		initEditButtons();
		
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
		}
		obsList = FXCollections.observableArrayList(service.findAll());
		tableViewAluno.setItems(obsList);
		initPayButtons();
		initEditButtons();
	}

	public void createDialogPayForm(Aluno obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			PayFormController controller = loader.getController();
			controller.setAluno(obj);
			controller.setServices(new AlunoService(), new PlanoService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Pagamento: Informe a quantidade de mensalidades");
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
	
	public void createDialogFinancForm(Aluno obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			FinancFormController controller = loader.getController();
			controller.setAluno(obj);
			controller.setServices(new AlunoService(), new PlanoService());
			controller.loadAssociatedObjects();
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Pagamento: Editar informações de vencimento");
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
}