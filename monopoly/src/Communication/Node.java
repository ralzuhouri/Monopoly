/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

/**
 *
 * @author giacomosantarelli
 */
public class Node {
    
    public String name;
    public String ip;
    public int port;
    public boolean isActive = true;
    
    public Node(String name, String ip, int port){
        this.name = name;
        this.ip = ip;
        this.port = port;           
    }
    
}
