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
public abstract class Connector {
    ConnectorDelegate delegate = null;
    ArrayList<Node> nodes = null;
}
