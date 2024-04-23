package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entites.Departamento;

public class DepartmentFormController implements Initializable {

	private Departamento entity;

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

	public void setEntity(Departamento entity) {
		this.entity = entity;
	}

	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}

	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
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
