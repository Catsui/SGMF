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
	public void onMenuItemCadastroAlunoAction() {
		System.out.println("Menu Item Cadastro Aluno");
	}

	@FXML
	public void onMenuItemCadastroTreinoAction() {
		System.out.println("Menu Item Cadastro Treino");
	}

	@FXML
	public void onMenuItemCadastroAulaAction() {
		System.out.println("Menu Item Cadastro Aula");
	}
	
	@FXML
	public void onMenuItemConsultaAlunoAction() {
		System.out.println("Menu Item Consulta Aluno");
	}

	@FXML
	public void onMenuItemConsultaTreinoAction() {
		System.out.println("Menu Item Consulta Treino");
	}
	
	@FXML
	public void onMenuItemConsultaAulaAction() {
		System.out.println("Menu Item Consulta Aula");
	}
	
	@FXML
	public void onMenuItemAjudaSobreAction() {
		System.out.println("Menu Item Ajuda Sobre");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

}
