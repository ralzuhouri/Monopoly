/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basiclauncher;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 *
 * @author giacomosantarelli
 */
public class ServerConnector extends Connector
{    
    RMIServer server;
    
    public ServerConnector(String name) throws UnknownHostException, RemoteException
    {
        super();
        nodes = new ArrayList<>();
        
        String ip = InetAddress.getLocalHost().getHostAddress();
        Node node = new Node(name, ip, 1099);
        nodes.add(node);
        
        server = new RMIServer();
        server.serverConnector = this;
        Registry reg = LocateRegistry.createRegistry(1099);
        reg.rebind("Launcher",server);
    }
    
    public void registerNode(Node node)
    {
        System.err.println("Added node " + node.name);
        nodes.add(node);
        
        if(delegate != null) {
            delegate.nodeWasAdded(node);
        }
    }
    
    public void start() throws RemoteException
    {
        for(int i=1; i<nodes.size(); i++)
        {
            Node node = nodes.get(i);
            RMIClient client = new RMIClient();
            client.connectServer(node.ip, 1099);
            client.rmi.registerNodes(i,nodes);
        }
        
        if(delegate != null) {
            delegate.done(0, nodes);
        }
    }
}
