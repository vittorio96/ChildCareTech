package main.server.rmi_server;


import main.server.rmi_server.RemoteServerInterface;
import main.server.rmi_server.ServerRMI;

import java.rmi.Naming;

public class RMIServerLauncher {

    public static void main(String[] args) {
        try {
            //System.setSecurityManager(new RMISecurityManager());
            java.rmi.registry.LocateRegistry.createRegistry(1099);

            RemoteServerInterface server =new ServerRMI();
            //RemoteServerInterface server =new ServerRMI_SQLIF();
            Naming.rebind("rmi://127.0.0.1/stub", server);//Name binding
            System.out.println("[System] main.server.rmi_server.ServerRMI is ready.");
        }catch (Exception e) {
            System.out.println("main.server.rmi_server.ServerRMI failed: " + e);
        }
    }
}
