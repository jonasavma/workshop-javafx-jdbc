package gui.utils;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Util {
	
	
	public static Stage currentStage(ActionEvent event) {
		
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
		
	}

}
