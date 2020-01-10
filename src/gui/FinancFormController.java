package gui;

import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.robot.Robot;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.entities.Aluno;
import model.exceptions.ValidationException;
import model.services.AlunoService;
import model.services.PlanoService;

public class FinancFormController implements Initializable {

	private Aluno entity;

	private AlunoService service;
	
	private PlanoService planoService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtTelefone;
	
	@FXML
	private DatePicker dpPagamento;
	
	@FXML
	private DatePicker dpReferencia;
	
	@FXML
	private DatePicker dpVencimento;

	@FXML
	private Button btnConfirm;

	@FXML
	private Button btnCancel;

	public void setAluno(Aluno entity) {
		this.entity = entity;
	}

	public void setServices(AlunoService service, PlanoService planoService) {
		this.service = service;
		this.planoService = planoService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	public void onBtnConfirmAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo");
		}
		try {
			entity = getFormData();
			service.updatePagamento(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DBException e) {
			Alerts.showAlert("Erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			e.printStackTrace();
		}

	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Aluno getFormData() {
		Aluno obj = new Aluno();
		ValidationException exception = new ValidationException("Erro de validação");

		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "Campo obrigatório.");
		}
		obj.setNome(txtNome.getText());

		if (txtTelefone.getText() == null || txtTelefone.getText().trim().equals("")) {
			exception.addError("email", "Campo obrigatório.");
		}
		obj.setTelefone(txtTelefone.getText());

		if (dpPagamento.getValue() == null) {
			exception.addError("birthDate", "Campo obrigatório.");
		} else {
			Instant instant = Instant.from(dpPagamento.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setPagamento(Date.from(instant));
		}
		
		if (dpReferencia.getValue() == null) {
			exception.addError("referencia", "Campo obrigatório");
		} else {
			Instant instant = Instant.from(dpReferencia.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setReferencia(Date.from(instant));
		}
		
		if (dpVencimento.getValue() == null) {
			exception.addError("vencimento", "Campo obrigatório");
		} else {
			Instant instant = Instant.from(dpVencimento.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setVencimento(Date.from(instant));
		}
		
		return obj;
	}
	
	@FXML
	private void validateDatePickers() {
		Robot robot = new Robot();
		dpPagamento.requestFocus();
		robot.keyType(KeyCode.ENTER);
		robot.keyType(KeyCode.TAB);
		robot.keyType(KeyCode.ENTER);
		robot.keyType(KeyCode.TAB);
		robot.keyType(KeyCode.ENTER);
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldMaxLength(txtTelefone, 20);
		Constraints.setTextFieldMaxLength(txtTelefone, 70);
		Utils.enhanceDatePickers(dpPagamento, dpReferencia, dpVencimento);
		Utils.formatDatePicker(dpPagamento, "dd/MM/yyyy");
		Utils.formatDatePicker(dpReferencia, "dd/MM/yyyy");
		Utils.formatDatePicker(dpVencimento, "dd/MM/yyyy");		
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula.");
		}
		txtId.setText(entity.getId() == null ? "" : String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtTelefone.setText(entity.getTelefone());
		if (entity.getPagamento() != null) {
			java.util.Date vencimento = new Date(entity.getPagamento().getTime());
			dpPagamento.setValue(vencimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
	}
	
	public void loadAssociatedObjects() {
		if (planoService == null) {
			throw new IllegalStateException("Serviço de plano está nulo.");
		}
	}
}
