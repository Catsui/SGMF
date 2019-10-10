package gui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import model.services.AlunoService;
import model.services.PlanoService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemAluno;

	@FXML
	private MenuItem menuItemPlano;

	@FXML
	private MenuItem menuItemSalvarPresencas;

	@FXML
	private MenuItem menuItemAjudaSobre;

	@FXML
	private MenuItem menuItemBackupSalvarDados;
	
	@FXML
	private MenuItem menuItemBackupRecuperarDados;

	@FXML
	public void onMenuItemAlunoAction() {
		loadView("/gui/AlunoList.fxml", (AlunoListController controller) -> {
			controller.setAlunoService(new AlunoService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemPlanoAction() {
		loadView("/gui/PlanoList.fxml", (PlanoListController controller) -> {
			controller.setPlanoService(new PlanoService());
			controller.updateTableView();
		});

	}

	@FXML
	public void onMenuItemSalvarPresencasAction(ActionEvent event) {
		salvarPresencas(true, "presencas.csv");
		Platform.exit();
	}

	@FXML
	public void onMenuItemAjudaSobreAction() {
		loadView("/gui/AboutView.fxml", x -> {
		});
	}

	@FXML
	public void onMenuItemBackupSalvarDadosAction() {
		backupDados();
		Alerts.showAlert("Backup do Banco de Dados", null, "O banco de dados foi salvo com sucesso na pasta \"backups\" do programa. "
				+ "Para segurança, salve o arquivo de nome \"backup"+LocalDate.now()+"\" em outros locais.", AlertType.INFORMATION);
	}
	
	@FXML
	public void onMenuItemBackupRecuperarDadosAction() {
		FileChooser chooser = new FileChooser();
		chooser.setTitle("Indique o Banco de Dados a ser recuperado");
		File defaultDirectory = new File(System.getProperty("user.dir"));
		chooser.setInitialDirectory(defaultDirectory);
		File selectedFile = chooser.showOpenDialog(Main.getMainScene().getWindow());
		lerBackup(selectedFile.getAbsolutePath());
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

	private void salvarPresencas(Boolean presenca, String filepath) {
		AlunoListController controller = new AlunoListController();
		controller.setAlunoService(new AlunoService());
		controller.saveByPresenca(presenca, filepath);
	}

	private void backupDados() {
		AlunoListController controller = new AlunoListController();
		controller.setAlunoService(new AlunoService());
		controller.backupDados();
	}
	
	private void lerBackup(String filepath) {
		AlunoListController controller = new AlunoListController();
		controller.setAlunoService(new AlunoService());
		controller.lerBackup(filepath);
	}

}
