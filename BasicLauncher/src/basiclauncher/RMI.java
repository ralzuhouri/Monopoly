/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package basiclauncher;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author giacomosanterelli
 */
public interface RMI extends Remote
{
    public void registerNode(Node node) throws RemoteException;
    public void registerNodes(int index, ArrayList<Node> nodes) throws RemoteException;
}
