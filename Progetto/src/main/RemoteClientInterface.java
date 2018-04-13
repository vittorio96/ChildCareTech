package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClientInterface extends Remote {
    public void clientPrint(String s) throws RemoteException;

}
