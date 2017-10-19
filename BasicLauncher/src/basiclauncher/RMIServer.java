/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basiclauncher;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javax.swing.Timer;
/**
 *
 * @author giacomosantarelli
 */
public class RMIServer extends UnicastRemoteObject implements RMI
{      
    ServerConnector serverConnector = null;
    ClientConnector clientConnector = null;
    
    public RMIServer() throws RemoteException
    {
        super();
    }
    
    @Override
    public void registerNode(Node node) throws RemoteException
    {
        if(serverConnector != null) {
            
            serverConnector.registerNode(node);

            /*
            Timer timer = new Timer(10, new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    serverConnector.registerNode(node);
                }
            });
            timer.setRepeats(false);
            timer.start();*/
        }
    }
    
    @Override
    public void registerNodes(int index, ArrayList<Node> nodes) throws RemoteException
    {
        if(clientConnector != null) 
        {

            Timer timer = new Timer(10, new ActionListener() 
            {
                @Override
                public void actionPerformed(ActionEvent e) 
                {
                    try {
                        clientConnector.registerNodes(index,nodes);
                    } catch (RemoteException ex) {
                        Logger.getLogger(RMIServer.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            timer.setRepeats(false);
            timer.start();
        }
    }
}

