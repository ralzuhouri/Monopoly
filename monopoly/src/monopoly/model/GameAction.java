/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.model;

import java.io.Serializable;
import java.util.function.BiConsumer;

/**
 *
 * @author ramy
 */
public class GameAction implements Serializable {
    public final String description;
    public final SerializableBiConsumer<Player,Game> action;
    public static final long serialVersionUID = 45472389024756354L;
    
    public GameAction(String description, SerializableBiConsumer<Player,Game> action) {
        this.description = description;
        this.action = action;
    }
    
    public void invoke(Player player, Game game) {
        action.accept(player,game);
    }
}
