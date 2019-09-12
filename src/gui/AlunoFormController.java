package gui;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DBException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.Callback;
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
	private Label labelErrorName;
	
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
			throw new IllegalStateException("Serviço nulo");
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
		ValidationException exception = new ValidationException("Erro de validação");
		
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		if (txtName.getText()==null || txtName.getText().trim().equals("")) {
			exception.addError("name", "Campo não deve ser vazio.");
		}
		obj.setNome(txtName.getText());
		if (txtEmail.getText()==null || txtEmail.getText().trim().equals("")) {
			exception.addError("email", "Campo não deve ser vazio.");
		}
		obj.set(txtEmail.getText());
		
		if (dpDataNasc.getValue()==null) {
			exception.addError("birthDate", "Campo não deve ser vazio.");
		} else {
			Instant instant = Instant.from(dpDataNasc.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataNasc(Date.from(instant));
		}
		
		
		if (txtBaseSalary.getText()==null || txtBaseSalary.getText().trim().equals("")) {
			exception.addError("baseSalary", "Campo não deve ser vazio.");
		}
		obj.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
		if (exception.getErrors().size()>0) {
			throw exception;
		}
		
		obj.setDepartment(comboBoxDepartment.getValue());
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
		Constraints.setTextFieldMaxLength(txtName, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 70);
		Utils.formatDatePicker(dpDataNasc, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}
	
	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department> () {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item,empty);
				setText(empty? "":item.getName());
			}
		};
		
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula.");
		}
		txtId.setText(entity.getId()==null? "": String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		if(entity.getDataNasc()!=null) {
			dpDataNasc.setValue(LocalDate.ofInstant(entity.getDataNasc().toInstant(), ZoneId.systemDefault()));
		}
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(entity.getBaseSalary()==null? "":String.format("%.2f",entity.getBaseSalary()));
		if(entity.getDepartment()==null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		} else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}	
	
	private void setErrorMsgs(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		labelErrorName.setText(fields.contains("name")? errors.get("name"):"");
		labelErrorEmail.setText(fields.contains("email")? errors.get("email"):"");
		labelErrorDataNasc.setText(fields.contains("birthDate")? errors.get("birthDate"):"");
		labelErrorBaseSalary.setText(fields.contains("baseSalary")? errors.get("baseSalary"):"");		
	}
	
	public void loadAssociatedObjects() {
		if(departmentService == null) {
			throw new IllegalStateException("Serviço de departamento está nulo.");
		}
		List<Department> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);
	}

}
