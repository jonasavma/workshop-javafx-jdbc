package gui;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.utils.Util;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entites.Vendedor;
import model.services.VendedorService;


public class VendedorListController implements Initializable, DataChangeListener {

	private VendedorService vendedorService;

	@FXML
	private TableView<Vendedor> tableViewVendedor;

	@FXML
	private TableColumn<Vendedor, Integer> tableColumnId;

	@FXML
	private TableColumn<Vendedor, String> tableColumnNome;
	
	@FXML
	private TableColumn<Vendedor, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Vendedor, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Vendedor, Double> tableColumnSalario;

	@FXML
	TableColumn<Vendedor, Vendedor> tableColumnEDIT;

	@FXML
	private TableColumn<Vendedor, Vendedor> tableColumnREMOVE;

	@FXML
	private Button btNome;

	private ObservableList<Vendedor> obsList;

	@FXML
	public void onBtNovoAction(ActionEvent event) {

		Stage parentStage = Util.currentStage(event);
		Vendedor obj = new Vendedor();
		// nesta caso tem que colocar um parametro a mais obj
		//createDialogForm(obj, "/gui/vendedortForm.fxml", parentStage);
	}

	public void setvendedorvendedorService(VendedorService vendedorService) {
		this.vendedorService = vendedorService;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {

		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnNome.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Util.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		tableColumnSalario.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Util.formatTableColumnDouble(tableColumnSalario, 2);
		

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewVendedor.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (vendedorService == null) {
			throw new IllegalStateException("vendedorService was null");
		}

		List<Vendedor> list = vendedorService.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewVendedor.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	// Recebendo o terceiro argumento Vendedor obj
	/*
	 * public void createDialogForm(Vendedor obj, String absoluteName, Stage
	 * parentStage) { try {
	 * 
	 * FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
	 * Pane pane = loader.load();
	 * 
	 * // pegando uma referencia para o controlador DepartmentFormController
	 * departmentFormController = loader.getController();
	 * departmentFormController.setEntity(obj);
	 * departmentFormController.setvendedorvendedorService(new
	 * vendedorvendedorService());
	 * departmentFormController.subscribeDataChangeListener(this);
	 * departmentFormController.updateFormData();
	 * 
	 * Stage dialogStage = new Stage(); dialogStage.setTitle("Enter Vendedor data");
	 * dialogStage.setScene(new Scene(pane)); dialogStage.setResizable(false);
	 * dialogStage.initOwner(parentStage);
	 * dialogStage.initModality(Modality.WINDOW_MODAL); dialogStage.showAndWait();
	 * 
	 * } catch (Exception e) { Alerts.mostrarAlerta("IO Exception",
	 * "Error loading view ", e.getMessage(), AlertType.ERROR); } }
	 */

	@Override
	public void onDataChanged() {
		updateTableView();

	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				
				/*
				 * setGraphic(button); button.setOnAction( event -> createDialogForm(obj,
				 * "/gui/DepartmentForm.fxml", Util.currentStage(event)));
				 */
				 
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Vendedor, Vendedor>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Vendedor obj, boolean empty) {
				super.updateItem(obj, empty);

				if (obj == null) {
					setGraphic(null);
					return;
				}

				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Vendedor obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Tem que voçê quer deletar?");

		if (result.get() == ButtonType.OK) {
			if (vendedorService == null) {
				throw new IllegalStateException("vendedorService was null");
			}
			try {
				vendedorService.remove(obj);
				updateTableView();
			} catch (DbIntegrityException e) {
				Alerts.mostrarAlerta("Error removing objetc ", null, e.getMessage(), AlertType.ERROR);
				// TODO: handle  exception
			}
		}

	}
}
