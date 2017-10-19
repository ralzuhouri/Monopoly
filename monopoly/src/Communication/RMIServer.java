/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author ramy
 */
public class RMIServer extends UnicastRemoteObject implements RMI
{      
    public Communicator communicator = null;
    
    public RMIServer() throws RemoteException
    {
        super();
    }

    /**
     *
     * @param msg
     * @return
     * @throws RemoteException
     */
    @Override
    public void getData(Message msg) throws RemoteException
    {
        System.out.println("Message Received!!");
        if(communicator != null)
        {
            communicator.receive(msg);
        }
    }
    
}
