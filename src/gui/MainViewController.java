package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemCadastroAluno;

	@FXML
	private MenuItem menuItemCadastroTreino;

	@FXML
	private MenuItem menuItemCadastroAula;
	
	@FXML
	private MenuItem menuItemConsultaAluno;
	
	@FXML
	private MenuItem menuItemConsultaTreino;
	
	@FXML
	private MenuItem menuItemConsultaAula;

	@FXML
	private MenuItem menuItemAjudaSobre;

	@FXML
	public void onMenuItemCadastroAluno() {
		System.out.println("Menu Item Cadastro Aluno");
	}

	@FXML
	public void onMenuItemCadastroTreino() {
		System.out.println("Menu Item Cadastro Treino");
	}

	@FXML
	public void onMenuItemCadastroAula() {
		System.out.println("Menu Item Cadastro Aula");
	}
	
	@FXML
	public void onMenuItemConsultaAluno() {
		System.out.println("Menu Item Consulta Aluno");
	}

	@FXML
	public void onMenuItemConsultaTreino() {
		System.out.println("Menu Item Consulta Treino");
	}
	
	@FXML
	public void onMenuItemConsultaAula() {
		System.out.println("Menu Item Consulta Aula");
	}
	
	@FXML
	public void onMenuItemAjudaSobre() {
		System.out.println("Menu Item Ajuda Sobre");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

}
