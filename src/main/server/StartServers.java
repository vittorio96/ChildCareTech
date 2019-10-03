package main.server;

import main.server.rmi_server.RMIServerLauncher;
import main.server.socket_server.SocketServer;

import java.rmi.RemoteException;

public class StartServers {

        public static void main(String[] args) throws RemoteException, InterruptedException {
            RMIServerLauncher.main(null);
            SocketServer.main(null);
        }
}
