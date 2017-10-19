/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.util.ArrayList;
import monopoly.model.GameStateChange;

/**
 *
 * @author ramy
 */
public interface CommunicatorDelegate 
{
    public void receiveChange(GameStateChange change);
    public void failedToDeliver(Message msg);
    public void playersDisconnected(ArrayList<String> disconnectedPlayers);
}
