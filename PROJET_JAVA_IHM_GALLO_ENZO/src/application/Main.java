package application;
	

import java.io.IOException;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;

import data.CsvFileReader;

public class Main extends Application {
	

	
	@Override
	public void start(Stage primaryStage) {
		
    
		
		
		try {
			Parent content = FXMLLoader.load(getClass().getResource("GuiAnomalies.fxml"));
			Scene scene = new Scene(content);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Anomalies");
			primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		


		
		
	}
	
	public static void main(String[] args) {
		CsvFileReader.readCSV();
		launch(args);
	}
	
	
	
	
	
	
   
    
}
