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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
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
	private ComboBox<Integer> comboBoxPresenca;

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

		if (dpDataNasc.getValue() == null) {
			exception.addError("birthDate", "Campo obrigatório.");
		} else {
			Instant instant = Instant.from(dpDataNasc.getValue().atStartOfDay(ZoneId.systemDefault()));
			obj.setDataNasc(Date.from(instant));
		}

		if (dpDataInicio.getValue() == null) {
			exception.addError("birthDate", "Campo obrigatório.");
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
		initializeComboBoxAttend();
	}

	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldMaxLength(txtTelefone, 20);
		Constraints.setTextFieldMaxLength(txtTelefone, 70);
		Utils.formatDatePicker(dpDataNasc, "dd/MM/yyyy");
		Utils.formatDatePicker(dpDataInicio, "dd/MM/yyyy");
	}

	private void initializeComboBoxAttend() {
		ObservableList<Integer> opcoes = FXCollections.observableArrayList(1, 0);
		comboBoxPresenca.getItems().addAll(opcoes);
		comboBoxPresenca.getSelectionModel().select(1);
		comboBoxPresenca.setCellFactory((ListView<Integer> param) -> {
			final ListCell<Integer> celulas = new ListCell<Integer>() {
				@Override
				protected void updateItem(Integer t, boolean bln) {
					super.updateItem(t, bln);

					if (t != null) {
						setText(t == 1 ? "Presente" : "Ausente");
					} else {
						setText(null);
					}
				}
			};
			return celulas;
		});

		comboBoxPresenca.valueProperty().addListener((ov, oldVal, newVal) -> {
			System.out.println("ComboBox presença mudou de " + oldVal + " para " + newVal);
		});

		comboBoxPresenca.setConverter(new StringConverter<Integer>() {
			@Override
			public String toString(Integer obj) {
				if (obj == null) {
					return null;
				} else {
					return obj == 1 ? "Presente" : "Ausente";
				}
			}

			@Override
			public Integer fromString(String str) {
				return null;
			}

		});

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

	private void setErrorMsgs(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		labelErrorNome.setText(fields.contains("nome") ? errors.get("nome") : "");
		labelErrorTelefone.setText(fields.contains("telefone") ? errors.get("telefone") : "");
		labelErrorDataNasc.setText(fields.contains("dataNasc") ? errors.get("dataNasc") : "");
		labelErrorDataInicio.setText(fields.contains("dataInicio") ? errors.get("dataInicio") : "");
		labelErrorTreino.setText(fields.contains("treino") ? errors.get("treino") : "");
	}
}
