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
import model.entities.Treino;
import model.services.TreinoService;

public class TreinoListController implements Initializable {
	
	private TreinoService service;
	
	@FXML
	private TableView<Treino> tableViewTreino;
	
	@FXML
	private Button btnNovoTreino;
	
	@FXML
	private TableColumn<Treino, Integer> tableColumnIdTreino;
	
	@FXML
	private TableColumn<Treino, String> tableColumnNomeTreino;
	
	private ObservableList<Treino> obsList;
	
	@FXML
	public void onBtnNovoTreinoAction() {
		System.out.println("Botão Novo Treino");
	}
	
	public void setTreinoService(TreinoService service) {
		this.service = service;
	}
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();		
	}


	private void initializeNodes() {
		tableColumnIdTreino.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNomeTreino.setCellValueFactory(new PropertyValueFactory<>("nome"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewTreino.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("O serviço não existe.");
		}
		List<Treino> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewTreino.setItems(obsList);
	}
	

}
