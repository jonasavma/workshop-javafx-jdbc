package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.utils.Util;
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
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entites.Departamento;
import model.entites.Vendedor;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.VendedorService;

public class VendedorFormController implements Initializable {

	private Vendedor entity;

	private VendedorService vendedorService;

	// Atributo para criar um combox
	private DepartmentService departmentService;

	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private TextField txtEmail;

	@FXML
	private DatePicker dpBirthDate;

	@FXML
	private TextField txtBaseSalary;

	@FXML
	private ComboBox<Departamento> comboBoxDepartment;

	@FXML
	private Label labelErrorNome;

	@FXML
	private Label labelErrorEmail;

	@FXML
	private Label labelErrorBirthDate;

	@FXML
	private Label labelErrorBaseSalary;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	private ObservableList<Departamento> obsList;

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	public void setEntity(Vendedor entity) {
		this.entity = entity;
	}

	public VendedorService getVendedorService() {
		return vendedorService;
	}

	// Aterado para auxiliar combox vai injetar dos serviços
	public void setServices(VendedorService vendedorService, DepartmentService departmentService) {
		this.vendedorService = vendedorService;
		this.departmentService = departmentService;
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade Nula!");
		}

		if (vendedorService == null) {
			throw new IllegalStateException("Serviços nulo!");
		}

		try {
			entity = getFormData();
			vendedorService.saveOrUpadade(entity);
			notifyDataChangeListener();
			// Comando para fecar a janel apos salvar
			Util.currentStage(event).close();

		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {

			Alerts.mostrarAlerta("Error ao salbar objeto", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyDataChangeListener() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	private Vendedor getFormData() {
		Vendedor obj = new Vendedor();

		ValidationException exception = new ValidationException("Validation erros:");

		// O getTxtId esta no formato de string para converter para inteiro
		obj.setId(Util.tryParseToInt(txtId.getText()));

		if (txtNome.getText() == null || txtNome.getText().trim().equals("")) {
			exception.addError("Nome", "Campo não pode ser vazio!");
		}
		obj.setName(txtNome.getText());

		if (exception.getErrors().size() > 0) {
			throw exception;
		}

		return obj;
	}

	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Util.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void initializeNodes() {

		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtNome, 70);
		Constraints.setTextFieldDouble(txtBaseSalary);
		Constraints.setTextFieldMaxLength(txtEmail, 60);
		Util.formatDatePicker(dpBirthDate, "dd/MM/yyyy");

		initializeComboBoxDepartment();

	}

	// Converter inteiro para string
	public void updateFormData() {

		// Fazendo programação defensiva deve fazer uma verificação
		if (entity == null) {
			throw new IllegalStateException("Entidade nula ");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getName());
		txtEmail.setText(entity.getEmail());
		// Converter double para string
		Locale.setDefault(Locale.US);
		txtBaseSalary.setText(String.format("%2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			dpBirthDate.setValue(LocalDate.ofInstant(entity.getBirthDate().toInstant(), ZoneId.systemDefault()));
		}

		if (entity.getDepartamento() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();

		} else {
			comboBoxDepartment.setValue(entity.getDepartamento());

		}

	}

	public void loadAssociatedObjects() {

		if (departmentService == null) {
			throw new IllegalStateException("Departamentoservice esta nulo ");
		}

		List<Departamento> list = departmentService.findAll();
		obsList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(obsList);

	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("Nome")) {
			labelErrorNome.setText(errors.get("Nome"));
		}
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Departamento>, ListCell<Departamento>> factory = lv -> new ListCell<Departamento>() {
			@Override
			protected void updateItem(Departamento item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getNome());
			}
		};

		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}
}
