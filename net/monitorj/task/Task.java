/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.monitorj.task;

/**
 *
 * @author Josh
 */
public abstract class Task {
    
    private final String task;
    private String[] params;
    
    public Task(String task, String[] params) {
        this.task = task;
        this.params = params;
    }
    
    public Task(String task) {
        this.task = task;
    }
    
    public abstract void runTask();
    
    public String getTaskName() {
        return this.task;
    }
    
    public String[] getTaskParams() {
        return this.params;
    }
    
}
