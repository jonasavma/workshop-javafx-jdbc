package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.utils.Util;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entites.Departamento;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {

	private Departamento entity;

	private DepartmentService departmentService;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;

	@FXML
	private TextField txtNome;

	@FXML
	private Label labelErrorNome;

	@FXML
	private Button btSave;

	@FXML
	private Button btCancel;

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	public void setEntity(Departamento entity) {
		this.entity = entity;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entidade Nula!");
		}

		if (departmentService == null) {
			throw new IllegalStateException("Serviços nulo!");
		}

		try {
			entity = getFormData();
			departmentService.saveOrUpadade(entity);
			notifyDataChangeListener();
			// Comando para fecar a janel apos salvar
			Util.currentStage(event).close();

		} catch (DbException e) {

			Alerts.mostrarAlerta("Error ao salbar objeto", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyDataChangeListener() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}

	}

	private Departamento getFormData() {
		Departamento obj = new Departamento();

		// O getTxtId esta no formato de string para converter para inteiro
		obj.setId(Util.tryParseToInt(txtId.getText()));
		obj.setNome(txtNome.getText());

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
		Constraints.setTextFieldMaxLength(txtNome, 30);

	}

	// Converter inteiro para string
	public void updateFormData() {

		// Fazendo programação defensiva deve fazer uma verificação
		if (entity == null) {
			throw new IllegalStateException("Entidade nula ");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());

	}
}
