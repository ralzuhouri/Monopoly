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
public class ClientConnector extends Connector
{
    Node node;
    String serverIp;
    RMIServer server;
    int index;
    
    public ClientConnector(String name, String serverIp) throws UnknownHostException
    {
        String ip = InetAddress.getLocalHost().getHostAddress();
        node = new Node(name, ip, 1099);
        this.serverIp = serverIp;
    }
    
    public void connectToServer() throws RemoteException
    {
        RMIClient client = new RMIClient();
        client.connectServer(serverIp, 1099);
        client.rmi.registerNode(node);
        
        server = new RMIServer();
        server.clientConnector = this;
        Registry reg = LocateRegistry.createRegistry(1099);
        reg.rebind("Launcher",server);
    }
    
    public void registerNodes(int index, ArrayList<Node> nodes) throws RemoteException
    {
        if(index == 1){
            RMIClient client = new RMIClient();
            client.connectServer(nodes.get(0).ip,1099);
            client.rmi.registerNodes(0, nodes);
        }
        this.index = index;
        this.nodes = nodes;
        if(delegate != null) 
        {
            delegate.done(index,nodes);
        }
        
    }
}
