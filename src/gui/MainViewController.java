package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;
import model.services.VendedorService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemVendedor;
	@FXML
	private MenuItem menuItemDepartamento;
	@FXML
	private MenuItem menuItemAbout;

	@FXML
	public void onMenuItemVendedorAction() {
		loadView("/gui/vendedorList.fxml", (VendedorListController controller) -> {
			controller.setvendedorvendedorService(new VendedorService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemDepartamentoAction() {
		loadView("/gui/DepartamentoList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml", x -> {
		});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {

	}

	// Consumer é uma interface funcional
	/*
	 * Retorna um {@code Consumer} composto que executa, em sequência, este operação
	 * seguida pela operação {@code after}. Se estiver executando qualquer um
	 * operação lança uma exceção, ela é retransmitida para o chamador do operação
	 * composta. Se a execução desta operação gerar uma exceção, a operação {@code
	 * after} não será executada.
	 * 
	 * @param após a operação a ser executada após esta operação
	 * 
	 * @return um {@code Consumer} composto que executa em sequência isso operação
	 * seguida pela operação {@code after}
	 * 
	 * @throws NullPointerException se {@code after} for nulo
	 */
	private synchronized <T> void loadView(String absolutename, Consumer<T> initializingAction) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(absolutename));
		try {
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node meanMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(meanMenu);
			mainVBox.getChildren().addAll(newVBox.getChildren());

			// As duas linhas abaixo executam a função que for passada como argumento onMenuItemDepartamentoAction na linha 37
			T controller = loader.getController();
			initializingAction.accept(controller);

		} catch (IOException e) {
			Alerts.mostrarAlerta("IO Excepition", "Error load view ", e.getMessage(), AlertType.ERROR);
		}

	}

}
