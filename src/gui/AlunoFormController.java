package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.entities.Aluno;
import model.exceptions.ValidationException;
import model.services.AlunoService;

public class AlunoFormController implements Initializable {
	
	private Aluno entity;
	
	private AlunoService service;
	
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
	private Button btnSave;
	
	@FXML
	private Button btnCancel;

	
	public void setAluno(Aluno entity) {
		this.entity = entity;
	}
	
	public void setServices(AlunoService service) {
		this.service = service;
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula");
		}
		if (service == null) {
			throw new IllegalStateException("Servi�o nulo");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		} catch (DBException e) {
			Alerts.showAlert("Erro ao salvar o objeto", null, e.getMessage(), AlertType.ERROR);
		} catch (ValidationException e) {
			setErrorMsgs(e.getErrors());
		}
		
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener:dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Aluno getFormData() {
		Aluno obj = new Aluno();
		ValidationException exception = new ValidationException("Erro de valida��o");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtNome.getText()==null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "Campo obrigat�rio.");
		}
		obj.setNome(txtNome.getText());
		
		if (txtTelefone.getText()==null || txtTelefone.getText().trim().equals("")) {
			exception.addError("email", "Campo obrigat�rio.");
		}
		obj.setTelefone(txtTelefone.getText());
		
		if (dpDataNasc.getValue()==null) {
			exception.addError("birthDate", "Campo obrigat�rio.");
		} else {
			Instant instant = Instant.from(dpDataNasc.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataNasc(Date.from(instant));
		}
		
		if (dpDataInicio.getValue()==null) {
			exception.addError("birthDate", "Campo obrigat�rio.");
		} else {
			Instant instant = Instant.from(dpDataInicio.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataInicio(Date.from(instant));
		}
		
		obj.setTreino(txtTreino.getText());

		return obj;
	}

	@FXML
	public void onBtnCancelAction(ActionEvent event) {
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
	}
	
//	private void initializeComboBoxDepartment() {
//		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department> () {
//			@Override
//			protected void updateItem(Department item, boolean empty) {
//				super.updateItem(item,empty);
//				setText(empty? "":item.getNome());
//			}
//		};
//		
//		comboBoxDepartment.setCellFactory(factory);
//		comboBoxDepartment.setButtonCell(factory.call(null));
//	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula.");
		}
		txtId.setText(entity.getId()==null? "": String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtTelefone.setText(entity.getTelefone());
		if(entity.getDataNasc()!=null) {
			dpDataNasc.setValue(LocalDate.ofInstant(entity.getDataNasc().toInstant(), ZoneId.systemDefault()));
		}
	}

	
	private void setErrorMsgs(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorNome.setText(fields.contains("nome")? errors.get("nome"):"");
		labelErrorTelefone.setText(fields.contains("email")? errors.get("email"):"");
		labelErrorDataNasc.setText(fields.contains("birthDate")? errors.get("birthDate"):"");
		labelErrorDataInicio.setText(fields.contains("baseSalary")? errors.get("baseSalary"):"");		
	}
//	
//	public void loadAssociatedObjects() {
//		if(departmentService == null) {
//			throw new IllegalStateException("Servi�o de departamento est� nulo.");
//		}
//		List<Department> list = departmentService.findAll();
//		obsList = FXCollections.observableArrayList(list);
//		comboBoxDepartment.setItems(obsList);
//	}

}
