/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.monitorj.task.tasks;

import net.monitorj.task.Task;

/**
 *
 * @author Josh
 */
public class TaskShutdown extends Task {

    public TaskShutdown() {
        super("shutdown");
    }

    @Override
    public void runTask() {
        System.out.println("Shutting down system...");
    }
    
}
