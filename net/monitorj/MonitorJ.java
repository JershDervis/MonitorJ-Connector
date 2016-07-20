/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.monitorj;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import net.monitorj.util.DeviceUtil;
import net.monitorj.util.OSUtil;
import net.monitorj.util.WebUtil;

/**
 *
 * @author Josh
 */
public class MonitorJ extends Application {
    
    public static final boolean DEBUG_MODE = true;
    
    public static final String CHECKIN_URL = "http://monitorj.net/panel/connection/gate.php";
    public static final String CHECKIN_URL_DEBUG = "https://monitorjmm-jershdervis.c9users.io/panel/connection/gate.php";
    
    private ArrayList<String> extraData = new ArrayList<String>();
    
    @Override
    public void start(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        //Headding
        Text scenetitle = new Text("MonitorJ Device Connection");
        scenetitle.setFont(Font.font("Source Sans Pro", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);
        
        //Control Key Label
        Label controlKeyLbl = new Label("Control Key:");
        grid.add(controlKeyLbl, 0, 1);
        
        //Control Key TextField
        TextField controlKeyTf = new TextField();
        grid.add(controlKeyTf, 1, 1);
        
        //Submit Button
        Button btn = new Button();
        String originalBtnTxt = "Connect Device";
        btn.setText(originalBtnTxt);
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                btn.setText("Connecting...");
                
                try {
                    if(!beginConnection(controlKeyTf.getText())) {
                        //TODO: Popup box with error

                    }
                    btn.setText(originalBtnTxt);
                } catch (IOException ex) {
                    Logger.getLogger(MonitorJ.class.getName()).log(Level.SEVERE, null, ex);
                    btn.setText(originalBtnTxt);
                }
            }
        });
        btn.setMaxWidth(Double.MAX_VALUE);
        grid.setColumnSpan(btn, GridPane.REMAINING);
        grid.add(btn, 0, 2);
        
        StackPane root = new StackPane();
        root.getChildren().add(grid);
        
        Scene scene = new Scene(root, 400, 250);
        
        primaryStage.setTitle("MonitorJ");
        primaryStage.setScene(scene);
        scene.getStylesheets().add(MonitorJ.class.getResource("css/MonitorJForm.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * 
     * @param controlKey
     * @return 
     */
    private boolean beginConnection(String controlKey) throws IOException {
        String getRequestUrl = (DEBUG_MODE ? CHECKIN_URL_DEBUG : CHECKIN_URL) +  "?key=" + controlKey 
                + "&name=" + DeviceUtil.getComputerName() 
                + "&user=" + DeviceUtil.getUsername()
                + "&os=" + OSUtil.getOperatingSystemType().name();
        ArrayList<String> lines = WebUtil.getUrlSource(getRequestUrl);
        boolean connectionSuccess = false;
        for(String curLine : lines) {
            String[] lineSplit = curLine.split("=");
            
            //Check if connection was accepted successfully
            if(lineSplit[0].equalsIgnoreCase("ENTRY") && lineSplit[1].equalsIgnoreCase("SUCCESS")) {
                connectionSuccess = true;
                continue;
            } else if(lineSplit[0].equalsIgnoreCase("ENTRY") && lineSplit[1].equalsIgnoreCase("FAILED")) {
                break;
            }
            
            extraData.add(curLine);
        }
        
        (new Thread(new ConnectionThread(controlKey, extraData))).start();
        
        return connectionSuccess;
    }
}
