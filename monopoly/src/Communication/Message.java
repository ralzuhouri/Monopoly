/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.io.Serializable;
import java.util.ArrayList;
import monopoly.model.GameStateChange;

/**
 *
 * @author ramy
 */
public class Message implements Serializable
{ 
    private static final long serialVersionUID = 329859475839479837L;
    public int sender;
    public int receiver;
    public GameStateChange change = null;
    public ArrayList<String> activePlayers = null;
    public int hops = 0;
    
    public String description;
   
    public Message(String description)
    {
        this.description = description;
    }
    
    @Override
    public boolean equals(Object other){
        if(other != null && other instanceof Message) {
            Message otherM = (Message)other;
            return this.sender == otherM.sender && this.receiver == otherM.receiver && 
                    this.description.equals (otherM.description);
        }
        return false;
    }
    
    @Override
    public int hashCode(){
        return description.hashCode();
    }
    
    public Message(Message msg)
    {
        this.sender = msg.sender;
        this.receiver = msg.receiver;
        this.change = msg.change;
        this.description = msg.description;
        this.hops = msg.hops;
    }
}
