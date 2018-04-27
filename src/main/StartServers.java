package main;

import java.rmi.RemoteException;

public class StartServers {

        public static void main(String[] args) throws RemoteException, InterruptedException {
            RMIServerLauncher.main(null);
            SocketServer.main(null);
        }
}
