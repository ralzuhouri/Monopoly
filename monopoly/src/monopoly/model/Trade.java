/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author ramy
 */
public class Trade implements Serializable 
{
    public static final long serialVersionUID = 4535489724756314L;
    
    private final String player1Name;
    public String getPlayer1Name() {
        return player1Name;
    }
    
    private final String player2Name;
    public String getPlayer2Name() {
        return player2Name;
    }
    
    private ArrayList<String> player1PropertyNames = new ArrayList<>();
    public ArrayList<String> getPlayer1PropertyNames() {
        return player1PropertyNames;
    }
    public void setPlayer1PropertyNames(ArrayList<String> player1PropertyNames) {
        this.player1PropertyNames = player1PropertyNames;
    }
    
    public void setPlayer1Properties(ArrayList<Property> player1Properties)
    {
        ArrayList<String> player1PropertyNames = new ArrayList<>();
        for(Property p:player1Properties) {
            player1PropertyNames.add(p.name);
        }
        this.setPlayer1PropertyNames(player1PropertyNames);
    }
    
    public void addPlayer1PropertyName(String pName) 
    {
        player1PropertyNames.add(pName);
    }
    public void removePlayer1Property(String pName) throws IllegalArgumentException {
        if(player1PropertyNames.remove(pName)) {
            ; // Property removed from the trade
        } else {
            throw new IllegalArgumentException("Trying to remove from a trade a property which is not owned by the player");
        }
    }
    
    private ArrayList<String> player2PropertyNames = new ArrayList<>();
    public ArrayList<String> getPlayer2PropertyNames() {
        return player2PropertyNames;
    }
    public void addPlayer2PropertyName(String pName) 
    {
        player2PropertyNames.add(pName);
    }
    public void setPlayer2PropertyNames(ArrayList<String> player2PropertyNames) 
    {
        this.player2PropertyNames = player2PropertyNames;
    }
    public void setPlayer2Properties(ArrayList<Property> player2Properties)
    {
        ArrayList<String> player2PropertyNames = new ArrayList<>();
        for(Property p:player2Properties) {
            player2PropertyNames.add(p.name);
        }
        this.setPlayer2PropertyNames(player2PropertyNames);
    }
    public void removePlayer2PropertyName(Property pName) throws IllegalArgumentException {
        if(player2PropertyNames.remove(pName)) {
            ; // Property removed from the trade
        } else {
            throw new IllegalArgumentException("Trying to remove from a trade a property which is not owned by the player");
        }
    }
    
    private int player1Money = 0;
    public int getPlayer1Money() {
        return player1Money;
    }
    public void setPlayer1Money(int player1Money) throws IllegalArgumentException 
    {
        this.player1Money = player1Money;
    }
    
    private int player2Money = 0;
    public int getPlayer2Money() {
        return player2Money;
    }
    public void setPlayer2Money(int player2Money) throws IllegalArgumentException 
    {   
        this.player2Money = player2Money;
    }
    
    public Trade(String player1Name, String player2Name) {
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }
    
    public String getDescription()
    {
        StringBuilder builder = new StringBuilder("Affare: { ");
        builder.append(this.player1Name).append(": ");
        
        for(int i=0; i<this.getPlayer1PropertyNames().size();i++) 
        {
            String pName = this.player1PropertyNames.get(i);
            builder.append(pName).append(", ");
        }
        builder.replace(builder.length() - 2, builder.length() - 2, " ");
        builder.append(this.player2Name);
        builder.append(": ");
        
        for(int i=0; i<this.player2PropertyNames.size(); i++) 
        {
            String pName = this.player2PropertyNames.get(i);
            builder.append(pName).append(", ");
        }
        
        builder.delete(builder.length() - 2, builder.length() - 2);
        builder.append("}");
        return builder.toString();
    }
}
