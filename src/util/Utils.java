package util;



import javafx.event.Event;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(Event event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}
	
	
	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		}
		catch(NumberFormatException e){
			return null;
		}
		
		
	}

}
