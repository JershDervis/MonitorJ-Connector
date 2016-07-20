/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.monitorj.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Josh
 */
public class DeviceUtil {
    
    public static String getComputerName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }

    public static String getUsername() {
        return System.getProperty("user.name");
    }
    
}
