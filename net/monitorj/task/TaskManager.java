/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.monitorj.task;

import java.util.ArrayList;
import net.monitorj.task.tasks.*;

/**
 *
 * @author Josh
 */
public class TaskManager {
    
    public ArrayList<Task> tasks = new ArrayList<Task>();
    
    public TaskManager() {
        tasks.add(new TaskShutdown());
    }
    
}
