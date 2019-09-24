package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Plano;
import model.services.PlanoService;

public class PlanoListController implements Initializable, DataChangeListener {

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

	private ObservableList<Plano> obsList;

	@FXML
	public void onBtnNovoPlanoAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Plano obj = new Plano();
		createDialogForm(obj, "/gui/PlanoForm.fxml", parentStage);
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
		tableColumnMensalidade.setCellValueFactory(new PropertyValueFactory<>("mensalidade"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewPlano.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("O serviço não existe.");
		}
		List<Plano> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewPlano.setItems(obsList);
	}

	private void createDialogForm(Plano obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			PlanoFormController controller = loader.getController();
			controller.setPlano(obj);
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

//	private void createDialogView(Plano obj, String absoluteName, Stage parentStage) {
//		try {
//			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
//			Pane pane = loader.load();
//
//			PlanoViewController controller = loader.getController();
//			controller.setPlano(obj);
//			controller.setService(new PlanoService());
//			controller.subscribeDataChangeListener(this);
//			controller.updateFormData();
//
//			Stage dialogStage = new Stage();
//			dialogStage.setTitle("Dados do plano");
//			dialogStage.setScene(new Scene(pane));
//			dialogStage.setResizable(false);
//			dialogStage.initOwner(parentStage);
//			dialogStage.initModality(Modality.WINDOW_MODAL);
//			dialogStage.showAndWait();
//		} catch (IOException e) {
//			e.printStackTrace();
//			Alerts.showAlert("IO Exception", "Erro ao carregar nova janela", e.getMessage(), AlertType.ERROR);
//		}
//	}

	@Override
	public void onDataChanged() {
		updateTableView();
	}

}
