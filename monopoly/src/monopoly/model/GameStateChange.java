package monopoly.model;

import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ramy
 */
public class GameStateChange implements Serializable
{
    public static final long serialVersionUID = 4547646724354748L;
    
    public String player1Name = null;
    public String player2Name = null;
    public Game oldState = null;
    public Game newState = null;
    public String description = null;
    public Trade trade = null;
    
    public enum Type
    {
        Manage, Move, Pass, TradeProposal, TradeAccepted, TradeRefused, Build, Deconstruct, Mortgage, Unmortgage, ExitFromPrison, BuyProperty, Bankrupt
    }
    
    public Type type = null;
    
    public GameStateChange(Type type)
    {
        this.type = type;
    }
    
}
