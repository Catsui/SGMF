package gui;

import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Plano;
import model.exceptions.ValidationException;
import model.services.PlanoService;

public class PlanoFormController implements Initializable {

	private Plano entity;
	private PlanoService service;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtMensalidade;

	@FXML
	private Label labelErrorNome;

	@FXML
	private Label labelErrorMensalidade;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	public void setPlano(Plano entity) {
		this.entity = entity;
	}

	public void setService(PlanoService service) {
		this.service = service;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	

	@FXML
	public void onBtnSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula.");
		}
		if (service == null) {
			throw new IllegalStateException("Serviço nulo.");
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
	
	@FXML
	public void onBtnCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();		
	}

	private void setErrorMsgs(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorNome.setText(fields.contains("nome") ? errors.get("nome") : "");
		labelErrorMensalidade.setText(fields.contains("mensalidade") ? errors.get("mensalidade") : "");
	}

	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	private Plano getFormData() {
		Plano obj = new Plano();
		ValidationException exception = new ValidationException("Erro de validação.");

		obj.setId(Utils.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("nome", "Campo Obrigatório.");
		}
		obj.setNome(txtNome.getText());

		if (txtMensalidade.getText() == null || txtMensalidade.getText().trim().equals("")) {
			exception.addError("mensalidade", "Campo Obrigatório.");
		}
		obj.setMensalidade(Utils.tryParseToDouble(txtMensalidade.getText()));

		return obj;
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeNodes();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldDouble(txtMensalidade);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula.");
		}
		txtId.setText(entity.getId() == null ? "" : String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtMensalidade.setText(entity.getMensalidade() == null ? "" : String.valueOf(entity.getMensalidade()));
	}

}
