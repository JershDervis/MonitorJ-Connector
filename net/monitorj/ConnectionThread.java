/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.monitorj;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import static net.monitorj.MonitorJ.CHECKIN_URL;
import static net.monitorj.MonitorJ.CHECKIN_URL_DEBUG;
import static net.monitorj.MonitorJ.DEBUG_MODE;
import net.monitorj.task.Task;
import net.monitorj.task.TaskManager;
import net.monitorj.util.DeviceUtil;
import net.monitorj.util.OSUtil;
import net.monitorj.util.WebUtil;

/**
 *
 * @author Josh
 */
public class ConnectionThread implements Runnable {
    
    //In seconds
    public static final int CHECKIN_DELAY = 10;
    
    private final String controlKey;
    private final ArrayList<String> extraData;
    
    private TaskManager taskManager;
    
    public ConnectionThread(String controlKey, ArrayList<String> extraData) {
        this.controlKey = controlKey;
        this.extraData = extraData;
        
        this.taskManager = new TaskManager();
    }

    @Override
    public void run() {
        while(true) {
            try {
                String getRequestUrl = (DEBUG_MODE ? CHECKIN_URL_DEBUG : CHECKIN_URL) +  "?key=" + controlKey
                        + "&name=" + DeviceUtil.getComputerName()
                        + "&user=" + DeviceUtil.getUsername()
                        + "&os=" + OSUtil.getOperatingSystemType().name();
                
                System.out.println("==== Sending GET Request ====");
                ArrayList<String> lines = WebUtil.getUrlSource(getRequestUrl);
                
                for(String curLine : lines) {
                    System.out.println(curLine);
                    String[] lineSplit = curLine.split("=");
                    
                    if(lineSplit[0].equalsIgnoreCase("ENTRY") && lineSplit[1].equalsIgnoreCase("SUCCESS")) {
                        continue;
                    } else if(lineSplit[0].equalsIgnoreCase("ENTRY") && lineSplit[1].equalsIgnoreCase("FAILED")) {
                        break;
                    } else if(lineSplit[0].equalsIgnoreCase("TASK")) {
                        for(Task t : this.taskManager.tasks) {
                            if(t.getTaskName().equalsIgnoreCase(lineSplit[1]))
                                t.runTask();
                        }
                    }
                }
            } catch (UnknownHostException ex) {
                Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            try {
                Thread.sleep(CHECKIN_DELAY * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
