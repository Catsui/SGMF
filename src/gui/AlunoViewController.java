package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import gui.listeners.DataChangeListener;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entities.Aluno;

public class AlunoViewController implements Initializable {

	private Aluno entity;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private DatePicker dpDataNasc;

	@FXML
	private TextField txtTelefone;

	@FXML
	private DatePicker dpDataInicio;

	@FXML
	private TextArea txtTreino;

	@FXML
	private Label labelErrorNome;

	@FXML
	private Label labelErrorTelefone;

	@FXML
	private Label labelErrorDataNasc;

	@FXML
	private Label labelErrorDataInicio;

	@FXML
	private Label labelErrorTreino;

	@FXML
	private Button btnBack;

	public void setAluno(Aluno entity) {
		this.entity = entity;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}	

	@FXML
	public void onBtnBackAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldMaxLength(txtTelefone, 20);
		Constraints.setTextFieldMaxLength(txtTelefone, 70);
		Utils.formatDatePicker(dpDataNasc, "dd/MM/yyyy");
		Utils.formatDatePicker(dpDataInicio, "dd/MM/yyyy");
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula.");
		}
		txtId.setText(entity.getId() == null ? "" : String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtTelefone.setText(entity.getTelefone());
		if (entity.getDataNasc() != null) {
			java.util.Date dataNasc = new Date(entity.getDataNasc().getTime());
			dpDataNasc.setValue(LocalDate.ofInstant(dataNasc.toInstant(), ZoneId.systemDefault()));
		}
		if (entity.getDataInicio() != null) {
			java.util.Date dataInicio = new Date(entity.getDataInicio().getTime());
			dpDataInicio.setValue(dataInicio.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		txtTreino.setText(entity.getTreino());
	}

}
