/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basiclauncher;

import java.util.ArrayList;

/**
 *
 * @author giacomosantarelli
 */
public interface ConnectorDelegate 
{
    public void done(int index, ArrayList<Node> nodes);
    public void nodeWasAdded(Node node);
}

