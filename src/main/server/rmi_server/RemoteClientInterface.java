package main.server.rmi_server;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClientInterface extends Remote {
    public void update() throws RemoteException;
}
