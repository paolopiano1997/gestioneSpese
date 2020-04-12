package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class LoadingController implements Initializable {

	@FXML
	private StackPane rootPane;
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		new SpeseFXML().start();
	}
	
	class SpeseFXML extends Thread {
		
		@Override
		public void run() {
		
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						try {
						Parent root;
						
						root = FXMLLoader.load(getClass().getResource("Spese.fxml"));
					
						Scene scene = new Scene(root);
						Stage stage = new Stage();
						stage.getIcons().add(new Image("file:resources/Spese.png"));
						stage.setResizable(false);
						stage.setScene(scene);
						stage.show();
						
						rootPane.getScene().getWindow().hide();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
				
			
		}
			
	}

}
