/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author ramy
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
            rmi = (RMI) reg.lookup("Server");
            System.err.println("Connected to server");
        }
        catch(RemoteException | NotBoundException e)
        {
            System.err.println(e);
            try 
            {
                Registry reg = LocateRegistry.getRegistry(serverIP, port);
                rmi = (RMI) reg.lookup("Server");
                System.out.println("Connected to server");
            }
            catch(RemoteException | NotBoundException e2)
            {
                System.err.println(e2);
            }
        }
    }
}
