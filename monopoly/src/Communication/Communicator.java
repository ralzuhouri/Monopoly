/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Communication;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import monopoly.model.GameStateChange;
import javax.swing.Timer;
import monopoly.controller.Monopoly;

/**
 *
 * @author ramy
 */
public class Communicator 
{
    private RMIClient client = null;
    private RMIServer server = null;
    public ArrayList<Node> nodes = new ArrayList<>();
    public int self;
    public CommunicatorDelegate delegate = null;
    public Timer timer;
    public long timerCounter = 0;
    
    public Communicator(ArrayList<Node> nodes, int self) throws RemoteException, InterruptedException
    {
        this.nodes = nodes;
        this.self = self;
        server = new RMIServer();
        server.communicator = this;
        Registry reg = LocateRegistry.createRegistry(nodes.get(self).port);
        reg.rebind("Server",server);
        
        timer = new Timer(5000, new ActionListener() 
        {
            @Override
            public void actionPerformed(ActionEvent e) 
            {
                try 
                {
                    if((timerCounter % nodes.size()) == self) {
                        if(nodes.get(self).isActive && !Monopoly.game.isGameOver()) {
                            sendActivePlayers();
                        }
                    }
                    timerCounter++;
                    if(timerCounter < 0) {
                        timerCounter = 0;
                    }
                } 
                catch (RemoteException ex) 
                {
                    Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        timer.start();
    }
    
    public int numberOfActiveNodes()
    {
        int counter = 0;
        for(Node node:nodes) {
            if(node.isActive) {
                counter++;
            }
        }
        return counter;
    }
    
    public int nextIndex()
    {
        Node next = next();
        for(int i=0; i<nodes.size() && next!=null; i++) {
            Node node = getNode(i);
            if(next.name.equals(node.name)) {
                return i;
            }
        }
        
        println("No next index, returning -1");
        return -1;
    }
    
    public Node next()
    {   
        int hop = 1;
        Node next = null;
        
        do
        {
            next = getNode((self+hop) % nodes.size());
            hop++;
        }while(!next.isActive);
        
        if(next == null) {
            println("Null next node");
        }
        
        return next;
    }
    
    public void connectServer() 
    {  
        int index = nextIndex();
        client = new RMIClient();
        client.id = index;
        client.connectServer(next().ip,next().port);
    }
    
    public void send(Message msg) throws RemoteException
    {
        msg.receiver = nextIndex();
        msg.hops++;
        
        if(msg.hops > this.numberOfActiveNodes()) {
            println("Maximum number of hops reached, discarding message");
            return;
        }
        
        if(nextIndex() != client.id) 
        {
            println("nextIndex differs from the message receiver");
        }
        
        try
        {
            
            client.rmi.getData(msg);
        }
        catch(ConnectException | NullPointerException e)
        {
            println(nodes.get(self).name + " received a connect exception: " + e);
            try 
            {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException ex) 
            {
                Logger.getLogger(Communicator.class.getName()).log(Level.SEVERE, null, ex);
            }
            try
            {
                client.rmi.getData(msg);
            }
            catch(ConnectException | NullPointerException e2)
            {
                println(nodes.get(self).name + " received a connect exception: " + e2);
                callFailedToDeliver(msg);
            }
        }
    }
    
    public void callFailedToDeliver(Message msg)
    {
        Platform.runLater(() -> 
        {
            println("Failed to deliver");
            delegate.failedToDeliver(msg);
        });
    }
    
    public void sendGameStateChange(GameStateChange change, ArrayList<String> activePlayers) throws RemoteException
    {
        println("Sending game state change to server");
        Message msg = new Message(change.description);
        msg.activePlayers = activePlayers;
        msg.sender = self;
        msg.receiver = nextIndex();
        msg.change = change;
        this.send(msg);
    }
    
    public void receive(Message msg) throws RemoteException
    {
        println("Received game state change from client");
        
        ArrayList<String> disconnectedPlayers = new ArrayList<>();
        for(Node n:nodes) {
            if(n.isActive && !msg.activePlayers.contains(n.name)) {
                n.isActive = false;
                disconnectedPlayers.add(n.name);
            }
        }
        
        if(msg.sender != self)
        {
            if(msg.change != null && this.delegate != null)
            {
                Platform.runLater(() -> 
                {
                    println("Sending game state change");
                    delegate.receiveChange(msg.change);
                    
                    if(!disconnectedPlayers.isEmpty()) {
                        println("Sending disconnected players to the delegate");
                        delegate.playersDisconnected(disconnectedPlayers);
                    } 
                });
            }
            else
            {
                if(!disconnectedPlayers.isEmpty()) {
                    println("Sending disconnected players to the delegate");
                    delegate.playersDisconnected(disconnectedPlayers);
                } 
            }
            this.send(msg);
        }
        else if(!disconnectedPlayers.isEmpty())
        {
             println("Sending disconnected players to the delegate");
             delegate.playersDisconnected(disconnectedPlayers);
             sendActivePlayers();
        }
    }
    
    public void sendActivePlayers() throws RemoteException
    {
        Message newMsg = new Message("Players disconnected");
        newMsg.sender = self;
        
        ArrayList<String> activePlayers = new ArrayList<>();
        for(Node n:nodes) {
            if(n.isActive) {
                activePlayers.add(n.name);
            }
        }
        
        newMsg.activePlayers = activePlayers;
        send(newMsg);
    }
    
    public Node getNode(int index)
    {
        return nodes.get(index);
    }
    
    public Node getNode(String name)
    {
        for(Node node:nodes)
        {
            if(node.name.equals(name)) {
                return node;
            }
        }
        return null;
    }
    
    public void println(String str)
    {
        System.err.println(nodes.get(self).name + "> " + str);
    }
}
