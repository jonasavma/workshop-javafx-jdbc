package gui.utils;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Util {
	
	
	public static Stage currentStage(ActionEvent event) {
		
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
		
	}
	
	//Metodo statico ´para converter inteiro para string 
	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
			
		}catch (NumberFormatException e) {
			return null;
		}
	}

}
