/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basiclauncher;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author giacomosantarelli
 */
public class RMIClient 
{
    
    public RMI rmi = null;
    public int id = -1;
    
    public void connectServer(String serverIP,int port)
    {
        try
        {
            Registry reg = LocateRegistry.getRegistry(serverIP, port);
            rmi = (RMI) reg.lookup("Launcher");
            System.out.println("Connected to server");
        }
        catch(RemoteException | NotBoundException e)
        {
            System.out.println(e);
        }
    }
}

