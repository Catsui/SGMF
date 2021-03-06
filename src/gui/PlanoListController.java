package gui;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Plano;
import model.services.PlanoService;

public class PlanoListController implements Initializable, DataChangeListener {
	
	private String tabelaPlano;
	
	private String telaPlanoForm;

	private PlanoService service;

	@FXML
	private TableView<Plano> tableViewPlano;

	@FXML
	private Button btnNovoPlano;

	@FXML
	private TableColumn<Plano, Integer> tableColumnIdPlano;

	@FXML
	private TableColumn<Plano, String> tableColumnNomePlano;

	@FXML
	private TableColumn<Plano, Double> tableColumnMensalidade;
	
	@FXML
	private TableColumn<Plano, Plano> tableColumnEDIT;
	
	@FXML
	private TableColumn<Plano, Plano> tableColumnREMOVE;

	private ObservableList<Plano> obsList;
	
	public void setTabelas(String tabelaPlano) {
		this.tabelaPlano = tabelaPlano;
		if (tabelaPlano.equals("PLANO")) {
			telaPlanoForm = "/gui/PlanoAdultoForm.fxml";
		}
		if (tabelaPlano.equals("PLANOCRIANCA")) {
			telaPlanoForm = "/gui/PlanoCriancaForm.fxml";
		}
	}
	
	@FXML
	public void onBtnNovoPlanoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Plano obj = new Plano();
		createDialogForm(obj, telaPlanoForm, parentStage);
	}

	public void setPlanoService(PlanoService service) {
		this.service = service;
	}


	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tableColumnIdPlano.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNomePlano.setCellValueFactory(new PropertyValueFactory<>("nome"));
		tableColumnNomePlano.setPrefWidth(150);
		tableColumnMensalidade.setCellValueFactory(new PropertyValueFactory<>("mensalidade"));
		tableColumnMensalidade.setStyle("-fx-alignment: center-right");

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewPlano.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("O servi�o n�o existe.");
		}
		List<Plano> list = service.findAll(tabelaPlano);
		obsList = FXCollections.observableArrayList(list);
		tableViewPlano.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Plano obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			PlanoFormController controller = loader.getController();
			controller.setPlano(obj);
			controller.setTabelas(tabelaPlano);
			controller.setService(new PlanoService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Informe os dados do plano");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Erro ao carregar nova janela", e.getMessage(), AlertType.ERROR);
		}

	}
	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<> (param.getValue()));
		tableColumnEDIT.setPrefWidth(53);
		tableColumnEDIT.setStyle("-fx-alignment: CENTER");
		tableColumnEDIT.setCellFactory(param -> new TableCell<Plano, Plano>() {
			private final Button button = new Button("Editar");
			
			@Override
			protected void updateItem(Plano obj, boolean empty) {
				super.updateItem(obj, empty);
				
				if (obj ==  null) {
					setGraphic(null);
					return;
				}
				
				setGraphic(button);
				button.setOnAction(event -> createDialogForm(obj, "/gui/PlanoForm.fxml", Utils.currentStage(event)));
			}
		});
	}
	
	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<> (param.getValue()));
		tableColumnREMOVE.setPrefWidth(70);
		tableColumnREMOVE.setStyle("-fx-alignment: CENTER");
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Plano, Plano>() {
			private final Button button = new Button("Remover");
			
			@Override
			protected void updateItem(Plano obj, boolean empty) {
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
	

	private void removeEntity(Plano obj) {
		Optional<ButtonType> confirm = Alerts.showConfirmation("Confirma��o de Exclus�o",
				"Tem certeza de que deseja excluir o plano?");
		if (confirm.get()==ButtonType.YES) {
			if (service == null) {
				throw new IllegalStateException ("Servi�o nulo.");
			}
			
			try {
				service.remove(obj, tabelaPlano);
				updateTableView();
			} catch (DBIntegrityException e) {
				Alerts.showAlert("Erro ao remover o objeto", null, 
						"Existem alunos vinculados ao plano. Exclus�o n�o realizada.", 
						AlertType.ERROR);
			}
		}
	}


	@Override
	public void onDataChanged() {
		updateTableView();
	}

}
