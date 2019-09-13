package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.AlunoService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemAlunoConsulta;

	@FXML
	private MenuItem menuItemAlunoNovo;

	@FXML
	private MenuItem menuItemAjudaSobre;

	@FXML
	public void onMenuItemAlunoConsultaAction() {
		loadView("/gui/AlunoList.fxml", (AlunoListController controller)-> {
			controller.setAlunoService(new AlunoService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAjudaSobreAction() {
		loadView("/gui/AboutView.fxml", x-> {});
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	private synchronized <T> void loadView(String absoluteName, Consumer<T> initializingAction) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());
			
			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.showAlert("Erro ao carregar a página", null, "Houve um erro ao carregar a página: " + e.getMessage(),
					AlertType.ERROR);
		}

	}

}
