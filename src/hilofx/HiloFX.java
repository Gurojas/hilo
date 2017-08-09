/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hilofx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author Usuario
 */
public class HiloFX extends Application {
    
    private Stage stage;
    
    @Override
    public void start(Stage primaryStage) {
       
        
        this.stage = primaryStage;
        
        
        BorderPane root = new BorderPane();
        VBox panel = this.panelPrincipal();
        root.setCenter(panel);
        
        
        Scene scene = new Scene(root, 800, 600);
        
       
        
        this.stage.setTitle("Hello World!");
        this.stage.setScene(scene);
        this.stage.show();
    }
    
    private VBox panelPrincipal(){
        VBox panel = new VBox(5);
        panel.setPadding(new Insets(10));
        GridPane gestor1 = this.panelGestor();
        GridPane gestor2 = this.panelGestor();
        panel.getChildren().addAll(gestor1,gestor2);
        return panel;
    }
    
    private GridPane panelGestor(){
        GridPane panel = new GridPane();
        panel.setGridLinesVisible(false);
        panel.setHgap(10);
        panel.setVgap(10);
        
        TextField fieldPath = new TextField();
        fieldPath.setEditable(false);
        Button buttonBrowse = new Button("Browse");
        buttonBrowse.setPrefWidth(90);
        
        ProgressBar pbar = new ProgressBar(0);

        pbar.setPrefWidth(300);
        pbar.setPrefHeight(30);
        ProgressIndicator pind = new ProgressIndicator(0);
        pind.setPrefHeight(70);
        
        Label labelTime = new Label("Time lapsed: ");
        
        Button buttonStart = new Button("Start");
        buttonStart.setPrefWidth(100);
        Button buttonStop = new Button("Stop");
        buttonStop.setPrefWidth(100);
        
        panel.add(fieldPath,0,0);
        panel.add(buttonBrowse,1,0);
        panel.add(pbar,0,1);
        GridPane.setHalignment(pbar, HPos.CENTER);
        GridPane.setValignment(pind, VPos.BOTTOM);
        panel.add(pind,1,1);
        panel.add(labelTime,0,2);
        
        HBox panelButton = new HBox(5);
        panelButton.getChildren().addAll(buttonStart,buttonStop);
        
        panel.add(panelButton, 0,3);
        
        buttonBrowse.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                String path = filePath();
                fieldPath.setText(path);
                
            }
        });
        
        buttonStart.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                try {
                    Task task = Process(fieldPath.getText());
                    pbar.progressProperty().unbind();
                    pbar.progressProperty().bind(task.progressProperty());
                    pind.progressProperty().unbind();
                    pind.progressProperty().bind(task.progressProperty());
                    Thread thread = new Thread(task);
                    thread.start();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(HiloFX.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        return panel;
    }
    
    private String filePath(){
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(this.stage);
        if (file == null){
            return "";
        }
        return file.getPath();
    }
    
    private Task Process(String filePath) throws FileNotFoundException{
        Scanner scanner = new Scanner(new File(filePath));
        int max = Integer.valueOf(scanner.nextLine());
        System.out.println(max);
        return new Task() {
            @Override
            protected Object call() throws Exception {
                int i = 0;
                while(scanner.hasNextLine()){
                    Thread.sleep(50);
                    scanner.nextLine();
                    updateProgress(i+1, max);
                    i++;
                }
                return null;
            }
        };
        
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
}
