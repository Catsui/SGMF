package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Plano;
import model.services.PlanoService;

public class PlanoListController implements Initializable {
	
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
	public void onBtnNovoPlanoAction() {
		System.out.println("Botão Novo Plano");
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
	

}
