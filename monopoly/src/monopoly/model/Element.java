/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monopoly.model;

import monopoly.model.Game;
import monopoly.model.Player;

/**
 *
 * @author ramy
 */
public interface Element {
    public String getName();
    public String land(Player player, Game game);
}
