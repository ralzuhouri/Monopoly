/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.model;

import java.io.Serializable;

/**
 *
 * @author ramy
 */
public class CustomElement implements Element, Serializable
{
    public GameAction action;
    public static final long serialVersionUID = 454244671247563754L;
    
    public CustomElement(GameAction action) {
        this.action = action;
    }
    
    public String getName() {
        return action.description;
    }
    
    public String land(Player player, Game game) {
        action.invoke(player, game);
        return action.description;
    }
}
