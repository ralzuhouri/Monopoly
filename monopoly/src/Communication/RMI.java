/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Communication;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author ramy
 */
public interface RMI extends Remote
{
    public void getData(Message msg) throws RemoteException;
}
