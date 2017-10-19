/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basiclauncher;

import java.io.Serializable;

/**
 *
 * @author giacomosantarelli
 */
public class Node implements Serializable {
    
    public String name;
    public String ip;
    public int port;
    public boolean isActive = true;
    public static final long serialVersionUID = 95476467217565156L;
    
    public Node(String name, String ip, int port){
        this.name = name;
        this.ip = ip;
        this.port = port;           
    }
    
}
